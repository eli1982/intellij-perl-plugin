// This is a generated file. Not intended for manual editing.
package com.intellij.perlplugin.psi;

import com.intellij.perlplugin.psi.impl.PerlProperty;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

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
