package com.intellij.perlplugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.language.PerlFile;
import com.intellij.perlplugin.language.PerlIcons;
import com.intellij.perlplugin.psi.impl.PerlElementFactory;
import com.intellij.perlplugin.psi.impl.PerlProperty;
import com.intellij.perlplugin.psi.impl.PerlPropertyImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static List<PerlProperty> findProperties(Project project, PsiElement key) {
        List<PerlProperty> result = null;
        ArrayList<Package> packageList = ModulesContainer.getPackageList(key.getText().replace(";", ""));
        for (int i = 0; i < packageList.size(); i++) {
            Package packageObj = packageList.get(i);
            PerlFile perlFile = (PerlFile) PsiManager.getInstance(project).findFile(key.getContainingFile().getVirtualFile());
            PerlProperty[] properties = PsiTreeUtil.getChildrenOfType(perlFile, PerlPropertyImpl.class);
            if (properties != null) {
                for (PerlProperty property : properties) {
                    if (key.equals(property.getKey())) {
                        if (result == null) {
                            result = new ArrayList<PerlProperty>();
                        }
                        result.add(property);
                    }
                }
            }
        }
        return result != null ? result : Collections.<PerlProperty>emptyList();
    }

}