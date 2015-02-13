package com.intellij.perlplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;

/**
 * Created by eli on 27-11-14.
 */
public class UpdateSourcesAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        Utils.print("parsing started");
        long start = System.currentTimeMillis();
        PerlInternalParser.parseAllSources(e.getData(DataKeys.MODULE));
        long end = System.currentTimeMillis();
        Utils.print("update completed in " + ((end - start) / 1000F) + "sec");
    }
}
