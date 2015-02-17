package com.intellij.perlplugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.perlplugin.language.PerlIcons;
import com.intellij.perlplugin.psi.impl.PerlElementFactory;
import com.intellij.perlplugin.psi.impl.PerlProperty;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PerlPsiImplUtil {
    public static String getKey(PerlProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(PerlTypes.KEY);
        if (keyNode != null) {
            return keyNode.getText();
        } else {
            return null;
        }
    }

    public static String getValue(PerlProperty element) {
        ASTNode valueNode = element.getNode().findChildByType(PerlTypes.VALUE);
        if (valueNode != null) {
            return valueNode.getText();
        } else {
            return null;
        }
    }

    public static String getName(PerlProperty element) {
        return getKey(element);
    }

    public static PsiElement setName(PerlProperty element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(PerlTypes.KEY);
        if (keyNode != null) {

            PerlProperty property = PerlElementFactory.createProperty(element.getProject(), newName);
            ASTNode newKeyNode = property.getFirstChild().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement getNameIdentifier(PerlProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(PerlTypes.KEY);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static ItemPresentation getPresentation(final PerlProperty element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getKey();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return element.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return PerlIcons.FILE;
            }
        };
    }

}