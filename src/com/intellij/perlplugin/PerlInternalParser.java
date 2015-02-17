package com.intellij.perlplugin;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.bo.*;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.filters.FileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eli on 27-11-14.
 */
public class PerlInternalParser {
    private static int sum;

    public static void parseAllSources(Module module) {
        sum = 0;
        if (module == null) {
            //if you encounter this error - make sure to select a file in the editor before pressing ctrl+shift+G
            Utils.alert("No Module selected");//TODO: find a way to select a module without having to select a file
            return;
        }
        ModulesContainer.clear();
        if (Utils.debug) {
            Utils.print("==================");
            Utils.print("\tFirst Pass");
            Utils.print("==================");
        }
        firstPass(module);
        if (Utils.debug) {
            Utils.print("==================");
            Utils.print("\tSecond Pass");
            Utils.print("==================");
        }
        secondPass();
        if (Utils.debug) {
            Utils.print("total number of files: " + sum);
            Utils.print("==================");
            System.out.println("Problematic files:(" + ModulesContainer.totalDelays + ")");
            Utils.print("==================");
            System.out.println(ModulesContainer.getProblematicFiles());
        }
        ModulesContainer.setInitialized();
    }


    //==========================
    //  PASSES
    //==========================

    private static void firstPass(Module module) {
        VirtualFile[] sourceFolders = ProjectRootManager.getInstance(module.getProject()).getContentSourceRoots();
        if (sourceFolders.length == 0) {
            Utils.alert("No source folders found. please go to > Project Structure > Modules > Sources and add your source folders");
        }
        for (int i = 0; i < sourceFolders.length; i++) {
            File folder = new File(sourceFolders[i].getCanonicalPath());
            File[] files = folder.listFiles(new FileFilter());
            parseFiles(files);
        }
    }

    private static void secondPass() {
        //handle parent packages
        ArrayList<PendingPackage> pendingParentPackages = ModulesContainer.getPendingParentPackages();
        if (Utils.debug) {
            Utils.print("Pending Parent Packages: " + pendingParentPackages.size());
        }
        int errorsCount = 0;
        for (int i = 0; i < pendingParentPackages.size(); i++) {
            PendingPackage pendingParentPackage = pendingParentPackages.get(i);
            ArrayList<Package> parentPackageObj = ModulesContainer.getPackageList(pendingParentPackage.getParentPackage());

            if (parentPackageObj.size() > 0) {
                //parent package found - store link to each in child package
                Package child = pendingParentPackage.getChildPackage();
                child.setParentPackage(parentPackageObj.get(0));//TODO: we don't handle case where we have several packages of the same name, maybe solve this by matching paths?
            } else {
                if (Utils.debug) {
                    Utils.print("warning: the package '" + pendingParentPackage.getChildPackage().getPackageName() + "' is directing to the parent package '" + pendingParentPackage.getParentPackage() + "' that doesn't exist!");
                }
                errorsCount++;
            }
        }
        if (Utils.debug) {
            Utils.print("Total missing parent packages:" + errorsCount);
        }
    }

    //==========================
    //  METHODS
    //==========================

    private static void parseFiles(File[] files) {
        sum += files.length;
        if (Utils.debug) {
            Utils.print("Total parsed files: " + sum);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                parseFiles(file.listFiles(new FileFilter()));
            } else {
                if (Utils.debug) {
                    Utils.print(file.getAbsolutePath());
                }
                String path = file.getAbsolutePath();
                PerlInternalParser.parse(file.getAbsolutePath());
            }
        }
    }


    public static void parse(String filePath) {
        String fileContent = Utils.readFile(filePath);
//        fileContent =fileContent.replaceAll("#.*","");//TODO:: handle =head1 =cut block comments
        //fileContent.replaceAll("(=head1)(.|[\r\n])*?(=cut)","");

        float start = System.nanoTime();
        if (Utils.debug) {
            Utils.print("---------------------------------------------------");
            Utils.print("parsing file: " + filePath);
        }

        //get package
        Matcher contentSeparationRegex = Utils.applyRegex("(\\s*?package\\s+((\\w|::)+)\\s*;)", fileContent, Pattern.MULTILINE);

        //get packages start positions
        ArrayList<Integer> contentStartPositions = new ArrayList<Integer>();
        while (contentSeparationRegex.find()) {
            contentStartPositions.add(contentSeparationRegex.start(1));
        }

        int prevPos = 0;
        for (int i = 0; i < contentStartPositions.size(); i++) {
            //split packages
            int startPos = contentStartPositions.get(i);
            int endPos = (i + 1 < contentStartPositions.size()) ? contentStartPositions.get(i + 1) : fileContent.length();
            String content = fileContent.substring(startPos, endPos);

            //get package name
            Package packageObj = new Package(filePath, getPackageNameFromContent(content));

            addPendingPackageParent(packageObj, getPackageParentFromContent(content));
            addImportedPackagesFromContent(packageObj, content);
            addSubsFromContent(packageObj, content.replaceAll("#.*", ""));
            //other
            packageObj.setStartPositionInFile(fileContent.indexOf("package", prevPos));
            packageObj.setEndPositionInFile(endPos);

            if (Utils.debug) {
                Utils.print(packageObj);
                Utils.print("---------------------------------------------------");
            }
            prevPos = endPos - 1;
        }
        float end = System.nanoTime();
        float result = (end - start) / 1000000000F;
        if (Utils.debug) {
            Utils.print("time:" + result);
        }
        if (false && result > 0.5) {
            ModulesContainer.addProblematicFiles(filePath + "(" + result + ")");
            ModulesContainer.totalDelays += result;
        }
    }


    /**
     * if parent object isn't ready - we will add package parent in 2nd pass
     *
     * @param packageObj
     * @param packageParent
     */
    private static void addPendingPackageParent(Package packageObj, String packageParent) {
        if (packageParent != null) {
            ArrayList<Package> possiblePackages = ModulesContainer.getPackageList(packageParent);
            if (possiblePackages.size() > 0) {
                //try to find parent package (maybe we parsed it already)
                packageObj.setParentPackage(possiblePackages.get(0));//TODO: we don't handle case where we have several packages of the same name, maybe solve this by matching paths?
            } else {
                //we didn't find parent package try to find it on 2nd pass
                ModulesContainer.addPendingParentPackage(packageObj, packageParent);
            }
        }
    }


    private static void addImportedPackagesFromContent(Package packageObj, String content) {
        ArrayList<ImportedPackage> importedPackages = new ArrayList<ImportedPackage>();
        //use 'Bookings::Db::db_bp';
        Matcher packageNameRegex = Utils.applyRegex("(\\s*?use\\s+((\\w|::)+)\\s*;)", content);
        while (packageNameRegex.find() && !packageNameRegex.group(2).isEmpty()) {
            importedPackages.add(new ImportedPackage(packageNameRegex.group(2), packageObj));
        }
        packageObj.setImportedPackages(importedPackages);
    }

    private static void addSubsFromContent(Package packageObj, String content) {
        ArrayList<Sub> subs = new ArrayList<Sub>();
        try {
            //Matcher subsRegex = Utils.applyRegex("\\s+sub\\s+(\\w+)\\s*\\{", content);
            Matcher subsRegex = Utils.applyRegex("\\s*?sub\\s+(\\w+)\\s*\\{\\s*(my\\s+\\(\\s*((\\s*\\$\\w+\\s*\\,?\\s*)*)\\s*\\)\\s*=\\s*(@\\_|\\(.*@_.*\\))\\;)?", content);
            while (subsRegex.find()) {
                Sub sub = new Sub(packageObj, subsRegex.group(1));
                sub.setArguments(getArgumentsFromContent(subsRegex.group(3)));
                sub.setPositionInFile(subsRegex.end(1));
                subs.add(sub);
                if (Utils.debug) {
                    Utils.print(sub);
                }
            }
        } catch (Exception e) {
            Utils.print(e);
        }
        packageObj.setSubs(subs);
    }


    private static ArrayList<Argument> getArgumentsFromContent(String content) {
        ArrayList<Argument> argumentList = new ArrayList<Argument>();
        if (content != null) {
            String[] args = content.replaceAll("\\s", "").split(",");
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                argumentList.add(new Argument(arg, "", ""));
            }
        }
        return argumentList;
    }

    private static String getPackageNameFromContent(String content) {
        Matcher packageNameRegex = Utils.applyRegex("(\\s*?package\\s+((\\w|::)+)\\s*?;)", content);
        if (!packageNameRegex.find()) return "";//if this fails - then our regex for package name isn't good.
        return packageNameRegex.group(2);
    }

    private static String getPackageParentFromContent(String content) {
        //use parent 'Bookings::Db::db_bp';
        Matcher packageNameRegexNoQW = Utils.applyRegex("(\\s*?use\\s*?parent\\s*?((\\w+\\:\\:)*\\w+))", content);
        if (packageNameRegexNoQW.find() && !packageNameRegexNoQW.group(2).isEmpty()) {
            return packageNameRegexNoQW.group(2);
        }
        //use parent qw(Bookings::Db::db_bp);
        Matcher packageNameRegexWithQW = Utils.applyRegex("(\\s*?use\\s+parent\\s+qw\\s*?\\(\\s*?((\\w|::)+)\\s*?\\)\\s*?;)", content);
        if (packageNameRegexWithQW.find() && !packageNameRegexWithQW.group(2).isEmpty()) {
            return packageNameRegexWithQW.group(2);
        }
        return null;//no parent package found.
    }

}
