// This is a generated file. Not intended for manual editing.
package com.intellij.perlplugin.psi.impl;

import com.intellij.perlplugin.psi.PerlNamedElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class PerlVisitor extends PsiElementVisitor {

    public void visitProperty(@NotNull PerlProperty o) {
        visitNamedElement(o);
    }

    public void visitNamedElement(@NotNull PerlNamedElement o) {
        visitPsiElement(o);
    }

    public void visitPsiElement(@NotNull PsiElement o) {
        visitElement(o);
    }

}
