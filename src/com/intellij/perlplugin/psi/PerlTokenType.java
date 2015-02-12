package com.intellij.perlplugin.psi;

import com.intellij.perlplugin.language.PerlLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PerlTokenType extends IElementType {
    public PerlTokenType(@NotNull @NonNls String debugName) {
        super(debugName, PerlLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "PerlTokenType." + super.toString();
    }
}