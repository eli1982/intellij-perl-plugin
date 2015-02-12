package com.intellij.perlplugin.bo;

import com.intellij.perlplugin.ModulesContainer;

import java.util.ArrayList;

/**
 * Created by eli on 28-11-14.
 */
public class Package {
    private String originFile;
    private String packageName;
    private int startPositionInFile;
    private int endPositionInFile;
    private Package parentPackage;
    private ArrayList<Sub> subs = new ArrayList<Sub>();
    private ArrayList<ImportedPackage> importedPackages = new ArrayList<ImportedPackage>();

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

    public ArrayList<Sub> getAllSubs(){
        ArrayList<Sub> result = new ArrayList<Sub>();
        result.addAll(getSubs());
        if(getParentPackage() != null) {
            result.addAll(getParentPackage().getAllSubs());
        }
        return result;
    }

    public void setSubs(ArrayList<Sub> subs) {
        this.subs = subs;
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

    @Override
    public String toString() {
        return "Package{" + "\n" +
                "   originFile='" + originFile + '\'' + ",\n" +
                "   startPositionInFile='" + startPositionInFile + '\'' + ",\n" +
                "   endPositionInFile='" + endPositionInFile + '\'' + ",\n" +
                "   packageName='" + packageName + '\'' + ",\n" +
                "   parentPackage='" +  ((parentPackage != null) ? parentPackage.getPackageName() : "'null'") + '\'' + ",\n" +
                "   importedPackages=" + importedPackages + "\n" +
                "   subs=" + subs + "\n" +
                '}';
    }
}
