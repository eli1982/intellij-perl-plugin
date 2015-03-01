package com.intellij.perlplugin.extensions;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.bo.Package;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ELI-HOME on 01-Mar-15.
 */
public class PerlNavigationItem implements NavigationItem{

    private final Project project;
    private final String path;

    public PerlNavigationItem(Project project, String path) {
        this.project = project;
        this.path = path;
    }

    @Nullable
    @Override
    public String getName() {
        return path;
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        VirtualFile vFile = ModulesContainer.getVirtualFileFromPath(project, path);
        return PsiManager.getInstance(project).findFile(vFile).getPresentation();
    }

    @Override
    public void navigate(boolean b) {
        FileEditorManagerEx.getInstance(project).openFile(ModulesContainer.getVirtualFileFromPath(project, path),true);
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
