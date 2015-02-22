package com.intellij.perlplugin.language;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PerlFileType extends LanguageFileType {
    public static final PerlFileType INSTANCE = new PerlFileType();

    private PerlFileType() {
        super(PerlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return Constants.FILE_NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return Constants.DESCRIPTION;
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return Constants.FILE_TYPE_PM;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return PerlIcons.LANGUAGE;
    }
}
