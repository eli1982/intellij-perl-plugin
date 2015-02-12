package com.intellij.perlplugin.bo;

/**
 * Created by eli on 6-2-15.
 */
public class ImportedPackage {
    private String importPackage;
    private Package containingPackage;

    public ImportedPackage(String importPackage, Package containingPackage) {
        this.importPackage = importPackage;
        this.containingPackage = containingPackage;
    }

    public String getImportPackage() {
        return importPackage;
    }

    public void setImportPackage(String importPackage) {
        this.importPackage = importPackage;
    }

    public Package getContainingPackage() {
        return containingPackage;
    }

    public void setContainingPackage(Package containingPackage) {
        this.containingPackage = containingPackage;
    }

    @Override
    public String toString() {
        return "ImportPackage{" +
                "importPackage='" + importPackage + '\'' +
                ", containingPackage=" + containingPackage +
                '}';
    }
}
