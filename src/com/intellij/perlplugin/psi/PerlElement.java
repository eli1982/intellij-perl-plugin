package com.intellij.perlplugin.psi;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * Created by msakowski on 23/04/15.
 */
public class PerlElement {
    PsiElement psiElement;
    PerlElement previousPerlElement = null;
    public static PerlElement fromCompletionParameter(CompletionParameters completionParameters) {
        PsiElement originalPosition = completionParameters.getOriginalPosition();
        if (originalPosition != null) {
            return new PerlElement(originalPosition);
        } else {
            return new PerlElement(completionParameters.getPosition());
        }
    }
    public PerlElement(PsiElement psiElement) {
        this.psiElement = psiElement;
    }

    public boolean is(IElementType elementType) {
        return is(psiElement, elementType);
    }
    private boolean is(PsiElement psiElement, IElementType elementType) {
        return psiElement != null && psiElement.getNode().getElementType().equals(elementType);
    }
    public boolean isAny(IElementType... elementTypes) {
        for (IElementType iElementType: elementTypes) {
            if (is(iElementType)) return true;
        }
        return false;
    }
    public PerlElement previous() {
        if (previousPerlElement == null) {
            previousPerlElement = new PerlElement(getPreviousPsiElement());
        }
        return previousPerlElement;
    }
    public int getTextLength() {
        return psiElement != null ? psiElement.getTextLength() : 0;
    }
    public String getText() {
        return psiElement != null ? psiElement.getText() : null;
    }
    private PsiElement getPreviousPsiElement() {
        PsiElement result = psiElement;
        if (result != null) {
            if (result.getPrevSibling() != null) {
                //get sibling
                result = result.getPrevSibling();
                while (is(result, GeneratedParserUtilBase.DUMMY_BLOCK)) {
                    result = result.getLastChild();
                }
            } else if (result.getParent() != null && result.getParent().getPrevSibling() != null) {
                //get sibling from previous parent
                result = result.getParent().getPrevSibling();
                while (is(result, GeneratedParserUtilBase.DUMMY_BLOCK)) {
                    result = result.getLastChild();
                }
            } else {
                //we have no sibling - no point to continue
                return null;
            }
        }
        return result;
    }
}
