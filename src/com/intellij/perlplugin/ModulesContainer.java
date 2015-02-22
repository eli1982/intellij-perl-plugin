package com.intellij.perlplugin;

import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent;
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
        totalDelays = 0;
    }


    public static void addPendingParentPackage(Package packageObj, String parentPackage) {
        if (packageObj != null && parentPackage != null) {
            pendingParentPackages.add(new PendingPackage(parentPackage, packageObj));
        } else {
            Utils.alert("unexpected null!");
        }
    }

    public static ArrayList<PendingPackage> getPendingParentPackages() {
        return pendingParentPackages;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void setInitialized() {
        ModulesContainer.initialized = true;
        pendingParentPackages.clear();
        problematicFiles.clear();
    }

    public static void addProblematicFiles(String s) {
        problematicFiles.add(s);
    }

    public static ArrayList<String> getProblematicFiles() {
        return problematicFiles;
    }

    public static void renameFile(String oldPath, String path) {
        if (Utils.debug) {
            Utils.print("file renamed/moved:\nold: " + oldPath + "\nnew: " + path);
        }
        if (filePathsToPackages.containsKey(oldPath)) {
            //handle renaming a file that have a package existing in the cache
            ArrayList<Package> packages = filePathsToPackages.get(oldPath);
            for (int i = 0; i < packages.size(); i++) {
                Package packageObj = packages.get(i);
                packageObj.setOriginFile(path);
                addPackage(packageObj);
            }
            filePathsToPackages.remove(oldPath);
        }else if(Utils.isValidateExtension(path)){
            //handle renaming a file that doesn't have a package existing in the cache
            PerlInternalParser.parse(path);
        }
    }

    public static void removeFile(String path) {
        if(filePathsToPackages.containsKey(path)) {
            if(Utils.debug){
                Utils.print("removing file: " + path);
            }
            filePathsToPackages.remove(path);//TODO:find all packages referring to this package and remove this package as their parent
        }
    }

    public static void createFile(String path) {
        if(Utils.debug){
            Utils.print("creating file: " + path);
        }
        PerlInternalParser.parse(path);//TODO:find all packages referring to this package and populate them
    }
}
