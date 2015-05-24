package com.intellij.perlplugin.extensions.run.configuration;

import com.intellij.execution.Executor;
import com.intellij.perlplugin.language.Constants;
import com.intellij.perlplugin.language.PerlIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by ELI-HOME on 22-May-15.
 * Perl Executor for more info:
 * https://confluence.jetbrains.com/display/IDEADEV/Run+Configurations
 */

public class PerlExecutor extends Executor {
    @Override
    public String getToolWindowId() {
        return "Perl Tool Window";
    }

    @Override
    public Icon getToolWindowIcon() {
        return PerlIcons.LANGUAGE;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return PerlIcons.LANGUAGE;
    }

    @Override
    public Icon getDisabledIcon() {
        return PerlIcons.PACKAGE;
    }

    @Override
    public String getDescription() {
        return Constants.DESCRIPTION;
    }

    @NotNull
    @Override
    public String getActionName() {
        return "Perl Action";
    }

    @NotNull
    @Override
    public String getId() {
        return "Perl Executor";
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return "Perl Start";
    }

    @Override
    public String getContextActionId() {
        return "Perl Executor";
    }

    @Override
    public String getHelpId() {
        return "Perl Help";
    }
}
