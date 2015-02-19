// This is a generated file. Not intended for manual editing.
package com.intellij.perlplugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.perlplugin.psi.impl.PerlPropertyImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public interface PerlTypes {

    IElementType PROPERTY = new PerlElementType("PROPERTY");
    IElementType CRLF = new PerlTokenType("CRLF");
    IElementType KEY = new PerlTokenType("KEY");
    IElementType OPERATOR = new PerlTokenType("OPERATOR");
    IElementType BRACES = new PerlTokenType("BRACES");
    IElementType VALUE = new PerlTokenType("VALUE");
    IElementType WHITESPACE = new PerlTokenType("WHITESPACE");
    IElementType SUBROUTINE = new PerlTokenType("SUBROUTINE");
    IElementType PACKAGE = new PerlTokenType("PACKAGE");
    IElementType POINTER = new PerlTokenType("POINTER");
    IElementType LINE_COMMENT = new PerlTokenType("LINE_COMMENT");
    IElementType MARKUP = new PerlTokenType("MARKUP");
    IElementType LANG_VARIABLE = new PerlTokenType("LANG_VARIABLE");
    IElementType LANG_FUNCTION = new PerlTokenType("LANG_FUNCTION");
    IElementType LANG_SYNTAX = new PerlTokenType("LANG_SYNTAX");
    IElementType LANG_FILE_HANDLES = new PerlTokenType("LANG_FILE_HANDLES");
    IElementType ARGUMENTS = new PerlTokenType("ARGUMENTS");
    IElementType VARIABLE = new PerlTokenType("VARIABLE");
    IElementType HASH_KEY = new PerlTokenType("HASH_KEY");
    IElementType PREDICATE = new PerlTokenType("PREDICATE");

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
