package com.intellij.perlplugin.actions;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.CreateFileAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.perlplugin.language.Constants;
import com.intellij.perlplugin.language.PerlIcons;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ELI-HOME on 22-Feb-15.
 */
public class NewFileAction extends CreateFileAction {
    @NotNull
    @Override
    protected PsiElement[] invokeDialog(Project project, PsiDirectory directory) {
        final CreateFileAction.MyValidator validator = new CreateFileAction.MyValidator(project, directory){
            @Override
            public boolean checkInput(String inputString) {
                if(!inputString.endsWith(Constants.PM_EXTENSION) && !inputString.endsWith(Constants.PL_EXTENSION)){
                    return false;
                }
                return super.checkInput(inputString);
            }
        };
        Messages.showInputDialog(project, IdeBundle.message("prompt.enter.new.file.name", new Object[0]) + " (must end with pl/pm)", IdeBundle.message("title.new.file", new Object[0]), PerlIcons.LANGUAGE, null, validator);
        return validator.getCreatedElements();
    }
}
