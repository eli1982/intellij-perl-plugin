package com.intellij.perlplugin.extensions.contributors;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.extensions.navigation.PerlGoToClassFileNavigationItem;
import com.intellij.perlplugin.language.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by ELI-HOME on 28-Feb-15.
 */
public class PerlGoToClassContributor implements ChooseByNameContributor {
    class PackageNameNavigationItem implements NavigationItem {
        Project project;
        Package aPackage;

        public PackageNameNavigationItem(Project project, Package aPackage) {
            this.project = project;
            this.aPackage = aPackage;
        }

        private VirtualFile getPackageFile() {
            return ModulesContainer.getVirtualFileFromPath(project, aPackage.getOriginFile());
        }

        @Nullable
        @Override
        public String getName() {
            return aPackage.getPackageName();
        }

        @Nullable
        @Override
        public ItemPresentation getPresentation() {
            return new ItemPresentation() {
                @Nullable
                @Override
                public String getPresentableText() {
                    return aPackage.getPackageName();
                }

                @Nullable
                @Override
                public String getLocationString() {
                    return getPackageFile().getPresentableName();
                }

                @Nullable
                @Override
                public Icon getIcon(boolean b) {
                    return PerlIcons.PACKAGE;
                }
            };
        }

        @Override
        public void navigate(boolean b) {
            FileEditorManagerEx.getInstance(project).openFile(getPackageFile(), true);
        }

        @Override
        public boolean canNavigate() {
            return true;
        }

        @Override
        public boolean canNavigateToSource() {
            return true;
        }
    }

    @NotNull
    @Override
    public String[] getNames(Project project, boolean b) {
        ArrayList<Package> packages = ModulesContainer.getAllPackages();
        HashSet<String> symbols = new HashSet<String>();
        for (Package aPackage : packages) {
            symbols.add(aPackage.getPackageName());
        }

        return symbols.toArray(new String[symbols.size()]);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String packageName, String searchTerm, Project project, boolean b) {
        ArrayList<Package> packages = ModulesContainer.getPackageList(packageName);
        NavigationItem[] navigationItems = new NavigationItem[packages.size()];
        for (int i = 0; i < packages.size(); i++) {
            navigationItems[i] = new PackageNameNavigationItem(project, packages.get(i));
        }

        return navigationItems;
    }
}
