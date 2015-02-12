package com.intellij.perlplugin.psi;

import com.intellij.perlplugin.language.PerlLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PerlElementType extends IElementType {
    public PerlElementType(@NotNull @NonNls String debugName) {
        super(debugName, PerlLanguage.INSTANCE);
    }
}