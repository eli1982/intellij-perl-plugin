package com.intellij.perlplugin.extensions.navigation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

/**
 * Created by msakowski on 23/04/15.
 */
public class PerlGoToClassFileNavigationItem extends PerlFileNavigationItem {
    public PerlGoToClassFileNavigationItem(Project project, String path) {
        super(project, path);
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        VirtualFile vFile = ModulesContainer.getVirtualFileFromPath(project, path);
        if(vFile == null) {
            return null;
        }
        return PsiManager.getInstance(project).findFile(vFile).getPresentation();
    }
}
