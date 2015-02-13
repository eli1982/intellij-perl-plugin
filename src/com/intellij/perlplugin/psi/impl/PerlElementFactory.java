package com.intellij.perlplugin.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.perlplugin.language.PerlFile;
import com.intellij.perlplugin.language.PerlFileType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

public class PerlElementFactory {
    public static PerlProperty createProperty(Project project, String name, String value) {
        final PerlFile file = createFile(project, name + " = " + value);
        return (PerlProperty) file.getFirstChild();
    }

    public static PerlProperty createProperty(Project project, String name) {
        final PerlFile file = createFile(project, name);
        return (PerlProperty) file.getFirstChild();
    }

    public static PsiElement createCRLF(Project project) {
        final PerlFile file = createFile(project, "\n");
        return file.getFirstChild();
    }

    public static PerlFile createFile(Project project, String text) {
        String name = "dummy.Perl";
        return (PerlFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, PerlFileType.INSTANCE, text);
    }
}