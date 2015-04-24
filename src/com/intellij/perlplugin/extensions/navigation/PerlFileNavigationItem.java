package com.intellij.perlplugin.extensions.navigation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ELI-HOME on 01-Mar-15.
 */
abstract class PerlFileNavigationItem implements NavigationItem {

    protected final Project project;
    protected final String path;

    public PerlFileNavigationItem(Project project, String path) {
        this.project = project;
        this.path = path;
    }

    @Nullable
    @Override
    public String getName() {
        return path;
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
