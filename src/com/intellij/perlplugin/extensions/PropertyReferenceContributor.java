package com.intellij.perlplugin.extensions;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.psi.impl.PerlPropertyImpl;
import com.intellij.perlplugin.psi.impl.PerlPropertyReference;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by eli on 9-2-15.
 */
public class PropertyReferenceContributor extends PsiReferenceContributor {
    public PropertyReferenceContributor() {
        super();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(PerlPropertyImpl.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        Utils.print("registerReferenceProviders");
                        PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
                        String text = (String) literalExpression.getValue();
                        if (text != null) {
                            return new PsiReference[]{new PerlPropertyReference(element, new TextRange(8, text.length() + 1))};
                        }
                        return new PsiReference[0];
                    }
                });

    }
}
