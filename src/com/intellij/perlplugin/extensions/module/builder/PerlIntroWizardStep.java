package com.intellij.perlplugin.extensions.module.builder;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.wizard.CommitStepException;
import com.intellij.openapi.Disposable;
import com.intellij.perlplugin.language.PerlIcons;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PerlIntroWizardStep extends ModuleWizardStep implements Disposable {
    private JLabel jLabelPerlIntro;
    private JComponent myMainPanel;

    public void dispose() {
    }

    public JComponent getPreferredFocusedComponent() {
        return this.myMainPanel;
    }

    public void onWizardFinished()
            throws CommitStepException {
    }

    public JComponent getComponent() {
        if (this.myMainPanel == null) {
            this.myMainPanel = new JPanel();

            this.myMainPanel.setBorder(new TitledBorder("Perl"));
            this.myMainPanel.setPreferredSize(new Dimension(333, 364));


            this.jLabelPerlIntro = new JLabel();
            this.jLabelPerlIntro.setIcon(PerlIcons.LANGUAGE_256);
            Font labelFont = this.jLabelPerlIntro.getFont();

            this.jLabelPerlIntro.setFont(new Font(labelFont.getName(), 0, 14));
            GroupLayout jPanel4Layout = new GroupLayout(this.myMainPanel);
            this.myMainPanel.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup().addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabelPerlIntro).addGap(0, 0, 32767)));
            jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup().addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabelPerlIntro).addContainerGap(0, 32767)));
        }
        return this.myMainPanel;
    }

    public void updateDataModel() {
    }
}
