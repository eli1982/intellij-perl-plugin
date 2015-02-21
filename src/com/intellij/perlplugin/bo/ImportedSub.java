package com.intellij.perlplugin.bo;

/**
 * Created by eli on 6-2-15.
 */
public class ImportedSub {
    private String importSub;
    private String containingPackage;

    public ImportedSub(String importPackage, String containingPackage) {
        this.importSub = importPackage;
        this.containingPackage = containingPackage;
    }

    public String getImportSub() {
        return importSub;
    }

    public void setImportSub(String importSub) {
        this.importSub = importSub;
    }

    public String getContainingPackage() {
        return containingPackage;
    }

    public void setContainingPackage(String containingPackage) {
        this.containingPackage = containingPackage;
    }

    @Override
    public String toString() {
        return "ImportPackage{" +
                "importSub='" + importSub + '\'' +
                ", containingPackage=" + containingPackage +
                '}';
    }
}
