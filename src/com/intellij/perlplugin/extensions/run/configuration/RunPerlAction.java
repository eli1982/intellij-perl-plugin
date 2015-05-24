package com.intellij.perlplugin.extensions.run.configuration;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.PerlCli;
import com.intellij.perlplugin.Utils;

/**
 * Created by ELI-HOME on 25-May-15.
 * handles perl run configuration from the context menu
 */
public class RunPerlAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        if (Utils.debug) {
            Utils.print("running perl script action from context...");
        }
        VirtualFile[] files = (VirtualFile[]) e.getDataContext().getData(DataConstants.VIRTUAL_FILE_ARRAY);
        if (isInvalid(e.getProject(), files)) return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getExtension().equals("pl")) {
                PerlCli.runFile(e.getProject(), files[i].getPath());
            }
        }
    }

    public boolean isInvalid(Project project, VirtualFile[] files) {
        return (project == null || files == null | files.length == 0);
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        VirtualFile[] files = (VirtualFile[]) e.getDataContext().getData(DataConstants.VIRTUAL_FILE_ARRAY);
        if (isInvalid(e.getProject(), files)) return;

        //check pl file in order to update display
        boolean visible = false;
        String perlScriptName = "";
        for (int i = 0; i < files.length; i++) {
            VirtualFile file = files[i];
            if (file.getExtension().equals("pl")) {
                visible = true;
                if (perlScriptName.isEmpty()) {
                    perlScriptName = file.getName();
                } else {
                    perlScriptName += ", " + file.getName();
                }
            }
        }

        e.getPresentation().setVisible(true);// Visibility
        e.getPresentation().setEnabled(visible);// Enable or disable
        e.getPresentation().setText("Run '" + ((perlScriptName.isEmpty()) ? "..." : perlScriptName) + "'");// Update text
    }

}
