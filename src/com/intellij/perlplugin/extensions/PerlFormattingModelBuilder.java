package com.intellij.perlplugin.extensions;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.perlplugin.psi.PerlTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
 
public class PerlFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(),
                new PerlBlock(element.getNode(), Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(), createSpaceBuilder(settings)), settings);
    }
 
    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        SpacingBuilder spacingBuilder = new SpacingBuilder(settings);
        spacingBuilder.between(PerlTypes.LANG_SYNTAX, PerlTypes.ATTRIBUTE).spaces(1);
        spacingBuilder.between(PerlTypes.ATTRIBUTE, PerlTypes.LANG_SYNTAX).spaces(1);
        return spacingBuilder;
    }
 
    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}