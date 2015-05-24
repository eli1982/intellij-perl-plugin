package com.intellij.perlplugin.extensions.run.configuration.ui;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by ELI-HOME on 22-May-15.
 */
public class PerlSettingsEditor extends SettingsEditor {

    @Override
    protected void resetEditorFrom(Object o) {
        //TODO::implement
    }

    @Override
    protected void applyEditorTo(Object o) throws ConfigurationException {
        //TODO::implement
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        JPanel jPanel = new JPanel();
        return jPanel;
    }
}
