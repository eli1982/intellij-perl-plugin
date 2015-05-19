package com.intellij.perlplugin.extensions.module.builder;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.wizard.CommitStepException;
import com.intellij.openapi.Disposable;
import com.intellij.perlplugin.language.PerlIcons;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PerlProjectWizardStep extends ModuleWizardStep implements Disposable {
    private final WizardContext context;
    private JLabel jLabelPerlIntro;
    private JComponent myMainPanel;

    public PerlProjectWizardStep(WizardContext context) {
        this.context = context;
    }

    public void dispose() {
    }

    public JComponent getPreferredFocusedComponent() {
        return this.myMainPanel;
    }

    public void onWizardFinished() throws CommitStepException {
    }

    public JComponent getComponent() {
        if (myMainPanel == null) {
            myMainPanel = new JPanel();
            myMainPanel.setBorder(new TitledBorder("Perl"));
            myMainPanel.setPreferredSize(new Dimension(333, 364));
            jLabelPerlIntro = new JLabel();
            Font labelFont = jLabelPerlIntro.getFont();
            jLabelPerlIntro.setFont(new Font(labelFont.getName(), 0, 14));
            jLabelPerlIntro.setIcon(PerlIcons.LANGUAGE_256);
            myMainPanel.add(jLabelPerlIntro, null);

        }
        return this.myMainPanel;
    }

    public void updateDataModel() {
        //TODO:implement
    }
}
