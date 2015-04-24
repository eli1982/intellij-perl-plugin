package com.intellij.perlplugin.extensions.contributors;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.extensions.navigation.PerlGoToClassFileNavigationItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by ELI-HOME on 28-Feb-15.
 */
public class PerlFileChooseByNameContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean b) {
        ArrayList<Package> packages = ModulesContainer.getAllPackages();
        HashSet<String> paths = new HashSet<String>();
        for (int i = 0; i < packages.size(); i++) {
            paths.add(packages.get(i).getOriginFile());
        }

        String[] packagesResult = new String[paths.size()];
        int i=0;
        for (String path : paths) {
            packagesResult[i++] = path;
        }
        return packagesResult;
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String filePath, String searchTerm, Project project, boolean b) {
        ArrayList<Package> packages = ModulesContainer.getPackageListFromFile(filePath);
        NavigationItem[] navigationItems;
        if(packages.size() > 0){
            navigationItems = new NavigationItem[1];
            navigationItems[0] = new PerlGoToClassFileNavigationItem(project,packages.get(0).getOriginFile());
        }else{
            return new NavigationItem[0];
        }
        return navigationItems;
    }
}
