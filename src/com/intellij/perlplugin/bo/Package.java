package com.intellij.perlplugin.bo;

import com.intellij.perlplugin.ModulesContainer;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by eli on 28-11-14.
 */
public class Package{
    private String originFile;
    private String packageName;
    private int startPositionInFile;
    private int endPositionInFile;
    private Package parentPackage;
    private HashSet<Package> childrenPackages = new HashSet<Package>();
    private ArrayList<Sub> subs = new ArrayList<Sub>();
    private ArrayList<ImportedPackage> importedPackages = new ArrayList<ImportedPackage>();
    private ArrayList<ImportedSub> importedSubs = new ArrayList<ImportedSub>();

    public Package(String originFile, String packageName) {
        this.originFile = originFile;
        this.packageName = packageName;
        ModulesContainer.addPackage(this);

    }

    public Package(String packageName) {
        this.packageName = packageName;
    }

    public String getOriginFile() {
        return originFile;
    }

    public void setOriginFile(String originFile) {
        this.originFile = originFile;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Package getParentPackage() {
        return parentPackage;
    }

    public void setParentPackage(Package parentPackage) {
        this.parentPackage = parentPackage;
        if(parentPackage != null) {
            this.parentPackage.addChild(this);
        }
    }

    public ArrayList<ImportedPackage> getImportedPackages() {
        return importedPackages;
    }

    public void setImportedPackages(ArrayList<ImportedPackage> importedPackages) {
        this.importedPackages = importedPackages;
    }

    public ArrayList<Sub> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<Sub> subs) {
        this.subs = subs;
    }

    public ArrayList<Sub> getAllSubs() {
        ArrayList<Sub> result = new ArrayList<Sub>();
        result.addAll(getSubs());
        if (getParentPackage() != null) {
            result.addAll(getParentPackage().getAllSubs());
        }
        return result;
    }

    public int getStartPositionInFile() {
        return startPositionInFile;
    }

    public void setStartPositionInFile(int startPositionInFile) {
        this.startPositionInFile = startPositionInFile;
    }

    public int getEndPositionInFile() {
        return endPositionInFile;
    }

    public void setEndPositionInFile(int endPositionInFile) {
        this.endPositionInFile = endPositionInFile;
    }

    public void addChild(Package childPackage) {
        if (childPackage != null && !this.childrenPackages.contains(childPackage)) {
            this.childrenPackages.add(childPackage);
            childPackage.setParentPackage(this);
        }
    }

    public void removeChild(Package childPackage) {
        if (childPackage != null && !this.childrenPackages.contains(childPackage)) {
            this.childrenPackages.remove(childPackage);
        }
    }

    public HashSet<Package> getChildren() {
        return childrenPackages;
    }

    @Override
    public String toString() {
        return "Package{" + "\n" +
                "   originFile='" + originFile + '\'' + ",\n" +
                "   startPositionInFile='" + startPositionInFile + '\'' + ",\n" +
                "   endPositionInFile='" + endPositionInFile + '\'' + ",\n" +
                "   packageName='" + packageName + '\'' + ",\n" +
                "   parentPackage='" + ((parentPackage != null) ? parentPackage.getPackageName() : "''") + '\'' + ",\n" +
                "   importedPackages=" + importedPackages + "\n" +
                "   subs=" + subs + "\n" +
                '}';
    }

    public void setImportedSubs(ArrayList<ImportedSub> importedSubs) {
        this.importedSubs = importedSubs;
    }

    public ArrayList<ImportedSub> getImportedSubs() {
        return importedSubs;
    }

    public Sub getSubByName(String subName) {
        if (subName.startsWith(":")) {
            subName = subName.substring(1,subName.length());
        }
        ArrayList<Sub> allSubs = getAllSubs();
        for (int i = 0; i < allSubs.size(); i++) {
            if (allSubs.get(i).getName().equals(subName)) {
                return allSubs.get(i);
            }
        }
        return null;
    }
}
