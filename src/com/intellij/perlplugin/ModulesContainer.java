package com.intellij.perlplugin;

import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.bo.PendingPackage;
import com.intellij.perlplugin.bo.Sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by eli on 27-11-14.
 */
public class ModulesContainer{
    public static float totalDelays = 0;
    private static boolean initialized = false;
    private static HashMap<String, ArrayList<Package>> packages = new HashMap<String, ArrayList<Package>>();
    private static ArrayList<PendingPackage> pendingParentPackages = new ArrayList<PendingPackage>();
    private static HashMap<String, HashSet<Sub>> subs = new HashMap<String, HashSet<Sub>>();
    private static HashMap<String, ArrayList<Package>> filePackages = new HashMap<String, ArrayList<Package>>();
    private static ArrayList<String> problematicFiles = new ArrayList<String>();

    public static void addPackage(Package packageObj) {
        if(!packages.containsKey(packageObj.getPackageName())){
            packages.put(packageObj.getPackageName(), new ArrayList<Package>());
        }
        packages.get(packageObj.getPackageName()).add(packageObj);

        if (!filePackages.containsKey(packageObj.getOriginFile())) {
            filePackages.put(packageObj.getOriginFile(), new ArrayList<Package>());
        }
        filePackages.get(packageObj.getOriginFile()).add(packageObj);
    }

    public static ArrayList<Package> getPackageList(String packageName) {
        ArrayList<Package> packageList = new ArrayList<Package>();
        for (String key : packages.keySet()) {
            if (key.equals(packageName)) {
                packageList.addAll(packages.get(key));
            }
        }
        return packageList;
    }

    public static ArrayList<Package> getAllPackages() {
        ArrayList<Package> packageList = new ArrayList<Package>();
        for (String key : packages.keySet()) {
            packageList.addAll(packages.get(key));
        }
        return packageList;
    }

    public static ArrayList<Package> searchPackageList(String searchStr) {
        ArrayList<Package> packageList = new ArrayList<Package>();
        for (String key : packages.keySet()) {
            if (key.contains(searchStr)) {
                packageList.addAll(packages.get(key));
            }
        }
        return packageList;
    }

    public static ArrayList<Package> getPackageListFromFile(String filePath) {
        ArrayList<Package> packages = filePackages.get(filePath);
        if (packages == null) {
            packages = new ArrayList<Package>();
        }
        return packages;
    }

    public static void addSub(Sub sub) {
        if (!subs.containsKey(sub.getName())) {
            subs.put(sub.getName(), new HashSet<Sub>());
        }
        HashSet<Sub> subSet = subs.get(sub.getName());
        subSet.add(sub);
    }

    public static ArrayList<Sub> getSubList(String searchStr) {
        ArrayList<Sub> subList = new ArrayList<Sub>();
        for (String key : subs.keySet()) {
            if (key.contains(searchStr)) {
                for (Sub sub : subs.get(key)) {
                    subList.add(sub);
                }
            }
        }
        return subList;
    }

    public static void clear() {
        packages.clear();
        subs.clear();
        filePackages.clear();
        pendingParentPackages.clear();
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
    }

    public static void addProblematicFiles(String s) {
        problematicFiles.add(s);
    }

    public static ArrayList<String> getProblematicFiles() {
        return problematicFiles;
    }
}
