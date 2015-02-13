package com.intellij.perlplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.bo.Sub;
import com.intellij.perlplugin.ui.SubListRenderer;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by eli on 28-11-14.
 */
public class AutoCompleteAction extends AnAction {
    private int i;

    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(DataKeys.EDITOR);
        VirtualFile virtualFile = e.getData(DataKeys.VIRTUAL_FILE);
        if (editor == null || virtualFile == null) {
            return;
        }
        final ArrayList<Package> list = ModulesContainer.getPackageListFromFile(virtualFile.getCanonicalPath());
        if (list.size() == 0) {
            return;
        }

        ArrayList<Sub> allSubs = new ArrayList<Sub>();
        for (int i = 0; i < list.size(); i++) {
            allSubs.addAll(list.get(i).getAllSubs());
        }

        //create popup
        final JBList jbList = new JBList(allSubs);
        jbList.setCellRenderer(new SubListRenderer());

        jbList.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Utils.print(jbList.getSelectedValue());//we don't get on clicked event (check maybe on action will work)
            }
        });

        JBPopup popup = JBPopupFactory.getInstance().createListPopupBuilder(jbList).createPopup();

        //get absolute position of caret
        CaretModel caret = editor.getCaretModel();
        Point point = editor.visualPositionToXY(caret.getVisualPosition());
        SwingUtilities.convertPointToScreen(point, editor.getContentComponent());
        popup.setLocation(point);

        //show popup
        popup.show(editor.getContentComponent().getRootPane());
    }
}