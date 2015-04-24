package com.intellij.perlplugin.bo;

import com.intellij.perlplugin.ModulesContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by eli on 28-11-14.
 */
public class Package{
    public static final String PACKAGE_SEPARATOR = "::";
    private String originFile;
    private String qualifiedName;
    private int startPositionInFile;
    private int endPositionInFile;
    private Package parentPackage;
    private HashSet<Package> childrenPackages = new HashSet<Package>();
    private ArrayList<Sub> subs = new ArrayList<Sub>();
    private ArrayList<ImportedPackage> importedPackages = new ArrayList<ImportedPackage>();
    private ArrayList<ImportedSub> importedSubs = new ArrayList<ImportedSub>();
    private String fileName;

    public Package(String originFile, String qualifiedName) {
        this.originFile = originFile;
        this.qualifiedName = qualifiedName;
        ModulesContainer.addPackage(this);

    }

    public Package(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getOriginFile() {
        return originFile;
    }

    public void setOriginFile(String originFile) {
        this.originFile = originFile;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String packageName) {
        this.qualifiedName = packageName;
    }

    private String[] segments() {
        return qualifiedName.split(PACKAGE_SEPARATOR);
    }

    public String getSimpleName() {
        String[] segments = segments();
        return segments[segments.length - 1];
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
                "   qualifiedName='" + qualifiedName + '\'' + ",\n" +
                "   parentPackage='" + ((parentPackage != null) ? parentPackage.getQualifiedName() : "''") + '\'' + ",\n" +
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

    /**
     *
     * @return only the file name and extensions without the full path
     */
    public String getFileName() {
        if(fileName == null){
            String[] parts = getOriginFile().split("/");
            fileName = parts[parts.length-1];
        }
        return fileName ;
    }

    /* is this file potentially a main implementation of this package?
        for now we assume that main implementation resides in a file whose name is the same as package name.
         otherwise it is probably mixin
     */
    public boolean isPotentialMainImplementation() {
        String packageName = getSimpleName();
        return getFileName().toLowerCase().startsWith(packageName.toLowerCase() + ".");
    }
}
