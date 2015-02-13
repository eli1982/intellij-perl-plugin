package com.intellij.perlplugin.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by eli on 9-2-15.
 */
public class PerlPropertyReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    public PerlPropertyReference(PsiElement element, TextRange range, boolean soft) {
        super(element, range, soft);
    }

    public PerlPropertyReference(PsiElement element, TextRange range) {
        super(element, range);
    }

    public PerlPropertyReference(PsiElement element, boolean soft) {
        super(element, soft);
    }

    public PerlPropertyReference(@NotNull PsiElement element) {
        super(element);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @NotNull
    @Override
    public String getValue() {
        return super.getValue();
    }

    @Override
    public PsiElement getElement() {
        return super.getElement();
    }

    @Override
    public TextRange getRangeInElement() {
        return super.getRangeInElement();
    }

    @Override
    public void setRangeInElement(TextRange range) {
        super.setRangeInElement(range);
    }

    @Override
    protected TextRange calculateDefaultRangeInElement() {
        return super.calculateDefaultRangeInElement();
    }

    @NotNull
    @Override
    public String getCanonicalText() {
        return super.getCanonicalText();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return super.handleElementRename(newElementName);
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return super.bindToElement(element);
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        return super.isReferenceTo(element);
    }

    @Override
    public boolean isSoft() {
        return super.isSoft();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {
        return new ResolveResult[0];
    }
}
