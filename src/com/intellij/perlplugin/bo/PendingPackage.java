package com.intellij.perlplugin.bo;

/**
 * Created by eli on 6-2-15.
 */
public class PendingPackage{
    String parentPackage;
    Package childPackage;

    public PendingPackage(String parentPackage, Package childPackage) {
        this.parentPackage = parentPackage;
        this.childPackage = childPackage;
    }

    public String getParentPackage() {
        return parentPackage;
    }

    public Package getChildPackage() {
        return childPackage;
    }

    @Override
    public String toString() {
        return "PendingPackage{" + "\n" +
                "parentPackage='" + parentPackage + '\'' + "\n" +
                ", childPackage=" + childPackage + "\n" +
                '}';
    }
}
