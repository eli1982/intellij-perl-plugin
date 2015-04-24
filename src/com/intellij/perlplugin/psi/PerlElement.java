package com.intellij.perlplugin.psi;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.perlplugin.Utils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;

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
    public PerlElement previousIgnoring(IElementType... ignoredElements) {
        PerlElement previous = previous();
        while (previous.isAny(ignoredElements)) {
            previous = previous.previous();
        }
        return previous;
    }
    public int getTextLength() {
        return psiElement != null ? psiElement.getTextLength() : 0;
    }
    public String getText() {
        return psiElement != null ? psiElement.getText() : null;
    }
    private PsiElement getPreviousPsiElement() {
        if (psiElement != null) {
            if (psiElement.getPrevSibling() != null) {
                //get sibling
                return PsiTreeUtil.getDeepestLast(psiElement.getPrevSibling());
            } else {
                PsiElement parent = psiElement.getParent();
                while (parent != null && parent.getPrevSibling() == null) {
                    parent = parent.getParent();
                }
                if (parent == null) { //we got to top of block tree and none of the block had siblings thus there is no
                        //previous element as current is the first one
                    return null; //no previous element
                } else {
                    return PsiTreeUtil.getDeepestLast(parent.getPrevSibling());
                }
            }
        } else {
            Utils.alert("warning: call to getPreviousPsiElement on null element");
            return null;
        }
    }

    @Override
    public String toString() {
        return psiElement + " -> " + psiElement.getText();
    }
}
