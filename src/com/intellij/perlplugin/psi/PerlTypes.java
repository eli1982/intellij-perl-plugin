// This is a generated file. Not intended for manual editing.
package com.intellij.perlplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.perlplugin.psi.impl.*;

public interface PerlTypes {

  IElementType PROPERTY = new PerlElementType("PROPERTY");
  IElementType COMMENT = new PerlTokenType("COMMENT");
  IElementType CRLF = new PerlTokenType("CRLF");
  IElementType KEY = new PerlTokenType("KEY");
  IElementType OPERATOR = new PerlTokenType("OPERATOR");
  IElementType BRACES = new PerlTokenType("BRACES");
  IElementType VALUE = new PerlTokenType("VALUE");
  IElementType WHITESPACE = new PerlTokenType("WHITESPACE");
  IElementType SUBROUTINE = new PerlTokenType("SUBROUTINE");
  IElementType PACKAGE = new PerlTokenType("PACKAGE");
  IElementType POINTER = new PerlTokenType("POINTER");
  IElementType ENDOFLINECOMMENT = new PerlTokenType("ENDOFLINECOMMENT");

    class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
//       if (type == PROPERTY) {
        return new PerlPropertyImpl(node);
//      }
//      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
