package com.intellij.perlplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.perlplugin.PerlInternalParser;

/**
 * Created by eli on 27-11-14.
 */
public class UpdateSourcesAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
//        PerlInternalParser.start(e.get, e.getProject());
        Module[] modules = ModuleManager.getInstance(e.getProject()).getModules();
        for (int i = 0; i < modules.length; i++) {
            PerlInternalParser.start(modules[i]);
        }
    }
}
