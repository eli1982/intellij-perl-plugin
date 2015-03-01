package com.intellij.perlplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.bo.PendingPackage;
import com.intellij.perlplugin.bo.Sub;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by eli on 27-11-14.
 */
public class ModulesContainer {
    public static float totalDelays = 0;
    private static boolean initialized = false;
    private static HashMap<String, ArrayList<Package>> packageNamesToPackages = new HashMap<String, ArrayList<Package>>();
    private static HashMap<String, HashSet<Sub>> subNamesToSubs = new HashMap<String, HashSet<Sub>>();
    private static HashMap<String, ArrayList<Package>> filePathsToPackages = new HashMap<String, ArrayList<Package>>();
    private static HashMap<String, HashSet<String>> parentsToChildrenPackageNames = new HashMap<String, HashSet<String>>();
    private static ArrayList<PendingPackage> pendingParentPackages = new ArrayList<PendingPackage>();
    private static ArrayList<String> problematicFiles = new ArrayList<String>();

    //PACKAGES

    public static void addPackage(Package packageObj) {
        if (!packageNamesToPackages.containsKey(packageObj.getPackageName())) {
            packageNamesToPackages.put(packageObj.getPackageName(), new ArrayList<Package>());
        }
        packageNamesToPackages.get(packageObj.getPackageName()).add(packageObj);

        if (!filePathsToPackages.containsKey(packageObj.getOriginFile())) {
            filePathsToPackages.put(packageObj.getOriginFile(), new ArrayList<Package>());
        }
        filePathsToPackages.get(packageObj.getOriginFile()).add(packageObj);
    }

    public static ArrayList<Package> getPackageList(String packageName) {
        ArrayList<Package> packageList = new ArrayList<Package>();
        for (String key : packageNamesToPackages.keySet()) {
            if (key.equals(packageName)) {
                packageList.addAll(packageNamesToPackages.get(key));
            }
        }
        return packageList;
    }

    public static ArrayList<Package> getAllPackages() {
        ArrayList<Package> packageList = new ArrayList<Package>();
        for (String key : packageNamesToPackages.keySet()) {
            packageList.addAll(packageNamesToPackages.get(key));
        }
        return packageList;
    }

    public static ArrayList<Package> searchPackageList(String searchStr) {
        ArrayList<Package> packageList = new ArrayList<Package>();
        for (String key : packageNamesToPackages.keySet()) {
            if (key.contains(searchStr)) {
                packageList.addAll(packageNamesToPackages.get(key));
            }
        }
        return packageList;
    }

    public static ArrayList<Package> getPackageListFromFile(String filePath) {
        ArrayList<Package> packages = filePathsToPackages.get(filePath);
        if (packages == null) {
            packages = new ArrayList<Package>();
        }
        return packages;
    }

    //SUBS
    public static void addSub(Sub sub) {
        if (!subNamesToSubs.containsKey(sub.getName())) {
            subNamesToSubs.put(sub.getName(), new HashSet<Sub>());
        }
        HashSet<Sub> subSet = subNamesToSubs.get(sub.getName());
        subSet.add(sub);
    }

    public static ArrayList<Sub> getSubList(String searchStr) {
        ArrayList<Sub> subList = new ArrayList<Sub>();
        for (String key : subNamesToSubs.keySet()) {
            if (key.contains(searchStr)) {
                for (Sub sub : subNamesToSubs.get(key)) {
                    subList.add(sub);
                }
            }
        }
        return subList;
    }

    //OTHER
    public static void clear() {
        packageNamesToPackages.clear();
        pendingParentPackages.clear();
        subNamesToSubs.clear();
        filePathsToPackages.clear();
        problematicFiles.clear();
        parentsToChildrenPackageNames.clear();
        totalDelays = 0;
    }


    public static void addPendingParentPackage(Package packageObj, String parentPackage) {
        if (packageObj != null && parentPackage != null) {
            pendingParentPackages.add(new PendingPackage(parentPackage, packageObj));
        } else {
            Utils.alert("unexpected null!");
        }
    }

    public static void addParentChild(String parentPackageName, String childPackageName) {
        if (!parentsToChildrenPackageNames.containsKey(parentPackageName)) {
            parentsToChildrenPackageNames.put(parentPackageName, new HashSet<String>());
        }
        parentsToChildrenPackageNames.get(parentPackageName).add(childPackageName);
    }

    public static ArrayList<PendingPackage> getPendingParentPackages() {
        return pendingParentPackages;
    }

    public static void addProblematicFiles(String s) {
        problematicFiles.add(s);
    }

    public static ArrayList<String> getProblematicFiles() {
        return problematicFiles;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void setInitialized() {
        ModulesContainer.initialized = true;
        pendingParentPackages.clear();
        problematicFiles.clear();
    }

    public static void updateFile(String path, String fileContent) {
        if (Utils.debug) {
            Utils.print("updating file: " + path);
        }
        deleteFile(path);
        createFile(path,fileContent);
    }

    public static void renameFile(String oldPath, String path) {
        if (Utils.debug) {
            Utils.print("file renamed/moved:\nold: " + oldPath + "\nnew: " + path);
        }
        deleteFile(oldPath);
        if(Utils.isValidateExtension(path)) {
            createFile(path,null);
        }else{
            if (Utils.debug) {
                Utils.print("not a valid file extension - renamed file won't be parsed");
            }
        }
    }

    public static void deleteFile(String path) {
        if (filePathsToPackages.containsKey(path)) {
            if (Utils.debug) {
                Utils.print("deleting file: " + path);
            }
            ArrayList<Package> packages = filePathsToPackages.remove(path);
            for (int i = 0; i < packages.size(); i++) {
                Package packageObj = packages.get(i);

                //remove package from all it's children
                HashSet<Package> children = packageObj.getChildren();
                for (Package child : children) {
                    if (Utils.verbose) {
                        Utils.print("removing: " + packageObj.getPackageName() + " from child:" + child.getPackageName());
                    }
                    child.setParentPackage(null);
                }
                Package parentPackage = packageObj.getParentPackage();

                //remove package from it's parent
                if (parentPackage == null) {
                    if (Utils.verbose) {
                        Utils.print("no parent to remove for package: " + packageObj.getPackageName());
                    }
                } else {
                    if (Utils.verbose) {
                        Utils.print("removing: " + packageObj.getPackageName() + " from parent:" + packageObj.getParentPackage().getPackageName());
                    }
                    packageObj.getParentPackage().removeChild(packageObj);
                }

                //remove package from remaining cache - make sure file path matches
                ArrayList<Package> cachedPackages = packageNamesToPackages.get(packageObj.getPackageName());
                for (int j = 0; j < cachedPackages.size(); j++) {
                    if (cachedPackages.get(j).getOriginFile().equals(packageObj.getOriginFile())) {
                        cachedPackages.remove(cachedPackages.get(j));
                        j--;
                    }
                }
            }
        }
    }

    public static void createFile(final String path, final String fileContent) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (Utils.debug) {
                    Utils.print("creating file: " + path);
                }
                while (Utils.readFile(path).isEmpty()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //also happen when undoing a file deletion!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //create package
                PerlInternalParser.parse(path,fileContent);
                //set parent package for all inheriting children
                ArrayList<Package> packages = ModulesContainer.getPackageListFromFile(path);
                for (Package packageObj : packages) {
                    HashSet<String> children = parentsToChildrenPackageNames.get(packageObj.getPackageName());
                    if (children != null) {
                        for (String child : children) {
                            ArrayList<Package> childPackages = packageNamesToPackages.get(child);
                            for (Package childPackage : childPackages) {
                                childPackage.setParentPackage(packageObj);
                            }
                        }
                    }
                }
            }
        });
        t.start();
    }

    public static VirtualFile getVirtualFileFromPath(Project project, String filePath) {
        File file = new File(filePath);
        VirtualFile vFile = project.getBaseDir();
        VirtualFile result = vFile;

        File temp = file;
        boolean first = true;
        while(true) {
            if (result.findChild(temp.getName()) == null){
                first = false;
                temp = temp.getParentFile();
                if(temp == null){
                    //couldn't find file
                    return  null;
                }
            }else {
                result = result.findChild(temp.getName());
                if(first) {
                    //file found
                    return result;
                }else{
                    temp = file;
                }
                first = true;
            }
        }
    }
}
