package com.intellij.perlplugin.psi.impl;

import com.intellij.perlplugin.psi.PerlNamedElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class PerlNamedElementImpl extends ASTWrapperPsiElement implements PerlNamedElement {
    public PerlNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}