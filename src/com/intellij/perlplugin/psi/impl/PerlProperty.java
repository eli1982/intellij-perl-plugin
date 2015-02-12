// This is a generated file. Not intended for manual editing.
package com.intellij.perlplugin.psi.impl;

import com.intellij.perlplugin.psi.PerlNamedElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PerlProperty extends PerlNamedElement,PsiReference {

    String getKey();

    String getValue();

    String getName();

    PsiElement setName(String newName);

    PsiElement getNameIdentifier();

    ItemPresentation getPresentation();

    @NotNull
    @Override
    PsiReference[] getReferences();

    @Nullable
    @Override
    PsiReference getReference();
}

