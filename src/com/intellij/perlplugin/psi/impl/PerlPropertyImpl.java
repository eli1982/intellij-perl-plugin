// This is a generated file. Not intended for manual editing.
package com.intellij.perlplugin.psi.impl;


import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.TextRange;
import com.intellij.perlplugin.psi.PerlPsiImplUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlPropertyImpl extends ASTWrapperPsiElement implements PerlProperty, PsiReference {

    public PerlPropertyImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof PerlVisitor) ((PerlVisitor) visitor).visitProperty(this);
        else super.accept(visitor);
    }

    @Override
    public PsiReference getReference() {
        return super.getReference();
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        return super.getReferences();
    }

    public String getKey() {
        return PerlPsiImplUtil.getKey(this);
    }

    public String getValue() {
        return PerlPsiImplUtil.getValue(this);
    }

    public String getName() {
        return PerlPsiImplUtil.getName(this);
    }

    public PsiElement setName(String newName) {
        return PerlPsiImplUtil.setName(this, newName);
    }

    public PsiElement getNameIdentifier() {
        return PerlPsiImplUtil.getNameIdentifier(this);
    }

    public ItemPresentation getPresentation() {
        return PerlPsiImplUtil.getPresentation(this);
    }

    @Override
    public PsiElement getElement() {
        return null;
    }

    @Override
    public TextRange getRangeInElement() {
        return null;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return null;
    }

    @NotNull
    @Override
    public String getCanonicalText() {
        return null;
    }

    @Override
    public PsiElement handleElementRename(String s) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceTo(PsiElement psiElement) {
        return false;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public boolean isSoft() {
        return false;
    }
}
