package com.intellij.perlplugin.extensions;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.bo.Package;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by ELI-HOME on 28-Feb-15.
 */
public class PerlFileChooseByNameContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean b) {
        ArrayList<Package> packages = ModulesContainer.getAllPackages();
        String[] packagesResult = new String[packages.size()];
        for (int i = 0; i < packages.size(); i++) {
            packagesResult[i] = packages.get(i).getOriginFile();
        }
        System.out.println(packages.size());
        return packagesResult;
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String s, String s1, Project project, boolean b) {
        System.out.println(b);
        ArrayList<Package> packages = ModulesContainer.getPackageListFromFile(s);
        NavigationItem[] navigationItems;
        if(packages.size() > 0){
            navigationItems = new NavigationItem[1];
            navigationItems[0] = new PerlNavigationItem(project,packages.get(0).getOriginFile());
        }else{
            return new NavigationItem[0];
        }
        return navigationItems;
    }
}
