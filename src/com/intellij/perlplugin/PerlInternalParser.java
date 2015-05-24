package com.intellij.perlplugin;

import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.bo.*;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.components.FileEditorManagerListenerEX;
import com.intellij.perlplugin.extensions.PerlCompletionContributor;
import com.intellij.perlplugin.extensions.module.builder.PerlModuleType;
import com.intellij.perlplugin.filters.FileFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eli on 27-11-14.
 */
public class PerlInternalParser {
    public static final double PROBLEMATIC_FILE_TIME_THRESHOLD = 0.5;
    private static int sum;
    private static FileFilter fileFilter = new FileFilter();
    private static double totalFileCount = 0;
    private static Project project;

    public static void start(Module module) {
        project = module.getProject();

        if (isValidModuleType(module)) {
            ProgressManager.getInstance().run(new Task.Backgroundable(project, "Caching Perl Modules", true) {
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    try {
                        if (Utils.debug) {
                            Utils.print("parsing started");
                        }
                        long start = System.currentTimeMillis();
                        PerlInternalParser.parseAllSources(progressIndicator);
                        long end = System.currentTimeMillis();

                        if (Utils.debug) {
                            Utils.print("update completed in " + ((end - start) / 1000) + "sec");
                        }
                    } finally {
                    }

                    //attach file editor listener
                    project.getMessageBus().connect(project).subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListenerEX(project));
                    progressIndicator.stop();
                }

                @Override
                public void onSuccess() {
                    super.onSuccess();
                    PerlCompletionContributor.initialize();
                }

                @Override
                public void onCancel() {
                    if (Utils.debug) {
                        Utils.print("Caching Canceled");
                    }
                    clear();
                    super.onCancel();
                }
            });
        }

    }

    public static boolean isValidModuleType(Module module) {
        if (module != null && module.getOptionValue("type").equals(PerlModuleType.MODULE_TYPE)) {
            return true;
        }
        return false;
    }


    public static void parseAllSources(ProgressIndicator progressIndicator) {
        clear();
        if (Utils.debug) {
            Utils.print("==================");
            Utils.print("\tFirst Pass");
            Utils.print("==================");
        }

        firstPass(progressIndicator);
        if (Utils.debug) {
            Utils.print("==================");
            Utils.print("\tSecond Pass");
            Utils.print("==================");
        }

        secondPass(progressIndicator);
        if (Utils.debug) {
            Utils.print("total number of files: " + sum);
            Utils.print("==================");
            Utils.print("Problematic files time delay: " + ModulesContainer.totalDelays);
            Utils.print(ModulesContainer.getProblematicFiles());
            Utils.print("==================");
        }
        ModulesContainer.setInitialized();
    }


    //==========================
    //  PASSES
    //==========================

    private static void firstPass(ProgressIndicator progressIndicator) {
        VirtualFile[] sourceFolders = ProjectRootManager.getInstance(project).getContentSourceRoots();
        if (sourceFolders.length == 0) {
            Utils.alert("No source folders found. please go to > Project Structure > Modules > Sources and add your source folders");
        }
        // Set the progress bar percentage and text
        progressIndicator.setFraction(0.0);
        progressIndicator.setText("estimating work...");

        //get files estimation
        for (int i = 0; i < sourceFolders.length; i++) {
            File folder = new File(sourceFolders[i].getCanonicalPath());
            totalFileCount += Utils.getFilesCount(folder, fileFilter);
        }

        progressIndicator.setText("preparing cache...");


        if (Utils.debug) {
            Utils.print("totalFileCount:" + (int) totalFileCount);
        }
        //parse files
        for (int i = 0; i < sourceFolders.length; i++) {
            File folder = new File(sourceFolders[i].getCanonicalPath());
            File[] files = folder.listFiles(fileFilter);
            parseFiles(files, progressIndicator);
        }
    }

    private static void secondPass(ProgressIndicator progressIndicator) {
        progressIndicator.setText("finishing work...");
        //handle parent packages
        ArrayList<PendingPackage> pendingParentPackages = ModulesContainer.getPendingParentPackages();
        if (Utils.verbose) {
            Utils.print("Pending Parent Packages: " + pendingParentPackages.size());
        }
        int errorsCount = 0;
        for (int i = 0; i < pendingParentPackages.size(); i++) {
            if (progressIndicator.isCanceled()) {
                clear();
                return;
            }
            PendingPackage pendingParentPackage = pendingParentPackages.get(i);
            ArrayList<Package> parentPackageObj = ModulesContainer.getPackageList(pendingParentPackage.getParentPackage());

            if (parentPackageObj.size() > 0) {
                //parent package found - store link to each in child package
                Package child = pendingParentPackage.getChildPackage();
                child.setParentPackage(parentPackageObj.get(0));//TODO: we don't handle case where we have several packages of the same name, maybe solve this by matching paths?
            } else {
                if (Utils.debug) {
                    Utils.print("warning: the package '" + pendingParentPackage.getChildPackage().getQualifiedName() + "' is directing to the parent package '" + pendingParentPackage.getParentPackage() + "' that doesn't exist!");
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

    private static void parseFiles(File[] files, ProgressIndicator progressIndicator) {
        if (progressIndicator.isCanceled()) {
//            clear();
            return;
        }
        if (Utils.verbose) {
            Utils.print("Total parsed files: " + sum);
        }
        for (File file : files) {
            if (progressIndicator.isCanceled()) {
                clear();
                return;
            }
            if (file.isDirectory()) {
                parseFiles(file.listFiles(fileFilter), progressIndicator);
            } else {
                sum++;
                if (Utils.debug) {
                    Utils.print(file.getPath());
                }
                PerlInternalParser.parse(file.getPath());
                progressIndicator.setFraction(sum / totalFileCount);
                progressIndicator.setText2(sum + "/" + (int) totalFileCount + " files parsed");
            }
        }
    }


    public static void parse(String filePath) {
        parse(filePath, null);
    }

    public static void parse(String filePath, String fileContent) {
        if (fileContent == null) {
            fileContent = Utils.readFile(filePath);
            int eof = fileContent.indexOf("__END__");
            if (eof != -1) {
                fileContent = fileContent.substring(0, eof);
            }
        }

        float start = System.nanoTime();
        if (Utils.verbose) {
            Utils.print("---------------------------------------------------");
            Utils.print("parsing file: " + filePath);
        }

        //get package
        Matcher contentSeparationRegex = Utils.applyRegex("(package\\s+\\'?((\\w|_|-|::)+)\\'?\\s*;)", fileContent, Pattern.MULTILINE);

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
            final String content = fileContent.substring(startPos, endPos);

            //get package name
            final Package packageObj = new Package(filePath.replace("\\", "/"), getPackageNameFromContent(content));

            float startInner = 0;
            float endInner = 0;

            if (Utils.verbose) {
                startInner = System.nanoTime();
            }
            addPendingPackageParent(packageObj, getPackageParentFromContent(content));
            if (Utils.verbose) {
                endInner = System.nanoTime();
                Utils.print("performance[pndprn]:" + ((endInner - startInner) / 1000000000F));
                startInner = System.nanoTime();
            }
            addImportedPackagesFromContent(packageObj, content);
            if (Utils.verbose) {
                endInner = System.nanoTime();
                Utils.print("performance[imppkg]:" + ((endInner - startInner) / 1000000000F));
                startInner = System.nanoTime();
            }
            addImportedSubsFromContent(packageObj, fileContent);
            if (Utils.verbose) {
                endInner = System.nanoTime();
                Utils.print("performance[impsub]:" + ((endInner - startInner) / 1000000000F));
                startInner = System.nanoTime();
            }

            addSubsFromContent(packageObj, content.replaceAll("#.*", ""));
            if (Utils.verbose) {
                endInner = System.nanoTime();
                Utils.print("performance[regsub]:" + ((endInner - startInner) / 1000000000F));
            }
            //other
            packageObj.setStartPositionInFile(fileContent.indexOf("package", prevPos));
            packageObj.setEndPositionInFile(endPos);

            if (Utils.verbose) {
                Utils.print(packageObj);
                Utils.print("---------------------------------------------------");
            }
            prevPos = endPos - 1;
        }
        float end = System.nanoTime();
        float result = (end - start) / 1000000000F;
        if (Utils.verbose) {
            Utils.print("performance[total][" + new File(filePath).getName() + "]:" + result);
        }
        if (result > PROBLEMATIC_FILE_TIME_THRESHOLD) {
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
        ModulesContainer.addParentChild(packageParent, packageObj.getQualifiedName());
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
        //use 'AA::BB::CC';
        Matcher packageNameRegex = Utils.applyRegex("(use\\s+((\\w|::)+)\\s{0,256};)", content);
        while (packageNameRegex.find() && !packageNameRegex.group(2).isEmpty()) {
            if (Utils.verbose) {
                Utils.print("imported package: " + packageNameRegex.group(2));
            }
            importedPackages.add(new ImportedPackage(packageNameRegex.group(2), packageObj));
        }
        packageObj.setImportedPackages(importedPackages);
    }

    private static void addImportedSubsFromContent(Package packageObj, String content) {
        ArrayList<ImportedSub> importedSubs = new ArrayList<ImportedSub>();
        //use 'AA::BB::CC qw( several methods import )';
        Matcher packageNameRegex = Utils.applyRegex("(use\\s+((\\w|::)+)\\s*qw\\s*[(/]((\\s*([\\:\\$\\@\\%A-Za-z0-9_-]+)+\\s{0,256}(#.*)?)+)[)/])+", content);
        while (packageNameRegex.find() && !packageNameRegex.group(2).isEmpty()) {
            String subContainingPackage = packageNameRegex.group(2);
            String[] subNames = packageNameRegex.group(4).trim().split("\\s+");
            for (int i = 0; i < subNames.length; i++) {
                if (Utils.verbose) {
                    Utils.print("imported sub: " + subNames[i]);
                }
                importedSubs.add(new ImportedSub(subNames[i], subContainingPackage));
            }
        }
        packageObj.setImportedSubs(importedSubs);
    }


    private static void addSubsFromContent(Package packageObj, String content) {
        final ArrayList<Sub> subs = new ArrayList<Sub>();
        try {
            Matcher subsRegex = Utils.applyRegex("sub\\s+(\\w+)\\s*(?:\\([^\\)]*\\))?\\s*\\{(\\s*my\\s+\\(?\\s*((\\s*[\\$|\\%|\\@\\&](\\w|_|-)+\\s*\\,?\\s*)*)\\s*?\\)?(\\S|\\s){0,256}?\\;)?", content);//we limit up to 256 characters to avoid stack overflow
            float start;
            while (((start = System.nanoTime()) > 0F && subsRegex.find())) {
                Sub sub = new Sub(packageObj, subsRegex.group(1));

                if ((subsRegex.group().contains("@_") || subsRegex.group().contains("shift"))) {
                    sub.setArguments(getArgumentsFromContent(subsRegex.group(3)));
                }

                sub.setPositionInFile(subsRegex.end(1));
                if (Utils.verbose) {
                    Utils.print("add sub: " + sub.getName());
                }
                subs.add(sub);
                if (Utils.verbose) {
                    Utils.print(sub);
                }
                float end = System.nanoTime();
                float result = (end - start) / 1000000000F;
                if (Utils.verbose) {
                    Utils.print("performance[persub]:" + result);
                }
                if (result > PROBLEMATIC_FILE_TIME_THRESHOLD) {
                    ModulesContainer.addProblematicFiles(packageObj.getQualifiedName() + ">>>>" + sub.getName() + "(" + result + ")");
                }
            }
        } catch (StackOverflowError e) {
            Utils.print(e);
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
        Matcher packageNameRegex = Utils.applyRegex("(package\\s+((\\w|::)+)\\s{0,256}?;)", content);
        if (!packageNameRegex.find()) return "";//if this fails - then our regex for package name isn't good.
        return packageNameRegex.group(2);
    }

    private static String getPackageParentFromContent(String content) {
        //use parent qw( AA::BB::CC );
        Matcher packageNameRegexWithQW = Utils.applyRegex("(use\\s+(?:parent|base)\\s+qw\\s*?\\(\\s*?((\\w|::)+)\\s*?\\)\\s{0,256}?;)", content);
        if (packageNameRegexWithQW.find() && !packageNameRegexWithQW.group(2).isEmpty()) {
            return packageNameRegexWithQW.group(2);
        }
        //use parent 'AA::BB::CC';
        Matcher packageNameRegexNoQW = Utils.applyRegex("(use\\s*?(?:parent|base)\\s*?\\'?((\\w+\\:\\:)*\\'?\\w+))", content);
        if (packageNameRegexNoQW.find() && !packageNameRegexNoQW.group(2).isEmpty()) {
            return packageNameRegexNoQW.group(2);
        }
        return null;//no parent package found.
    }

    public static void clear() {
        sum = 0;
        totalFileCount = 0;
        ModulesContainer.clear();
        PerlCompletionContributor.clear();
    }

}
