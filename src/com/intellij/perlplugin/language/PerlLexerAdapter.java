package com.intellij.perlplugin.language;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class PerlLexerAdapter extends FlexAdapter {
    public PerlLexerAdapter() {
        super(new _PerlLexer((Reader) null));
    }
}
