package com.intellij.perlplugin.language;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PerlFile extends PsiFileBase {
    public PerlFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, PerlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return PerlFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return Constants.FILE_NAME;
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}