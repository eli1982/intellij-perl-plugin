package com.intellij.perlplugin.language;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.perlplugin.extensions.syntax.highlighting.PerlSyntaxHighlighter;
import com.intellij.perlplugin.extensions.syntax.highlighting.PerlSyntaxHighlighterFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by eli on 29-12-14.
 */
public class PerlLanguage extends Language {
    public static final PerlLanguage INSTANCE = new PerlLanguage();

    public PerlLanguage() {
        super(Constants.LANGUAGE_NAME);
        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new PerlSyntaxHighlighterFactory() {
            @NotNull
            protected SyntaxHighlighter createHighlighter() {
                return new PerlSyntaxHighlighter();
            }
        });
    }

}
