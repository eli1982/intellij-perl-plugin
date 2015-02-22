package com.intellij.perlplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.perlplugin.PerlInternalParser;

/**
 * Created by eli on 27-11-14.
 */
public class UpdateSourcesAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        PerlInternalParser.start(e.getProject());
    }
}
