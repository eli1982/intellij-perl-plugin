package com.intellij.perlplugin.extensions;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.perlplugin.language.PerlLexerAdapter;
import com.intellij.perlplugin.psi.PerlTypes;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

;

/**
 * Created by eli on 9-2-15.
 */
public class PerlSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey OPERATOR = createTextAttributesKey("OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey BRACES = createTextAttributesKey("BRACES", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey KEY = createTextAttributesKey("KEY", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey VALUE = createTextAttributesKey("VALUE", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey COMMENTS = createTextAttributesKey("COMMENTS", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey LANG_VARIABLE = createTextAttributesKey("LANG_VARIABLE", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
    public static final TextAttributesKey LANG_FUNCTION = createTextAttributesKey("LANG_FUNCTION", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
    public static final TextAttributesKey LANG_SYNTAX = createTextAttributesKey("LANG_SYNTAX", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
    public static final TextAttributesKey LANG_FILE_HANDLES = createTextAttributesKey("LANG_FILE_HANDLES", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
    public static final TextAttributesKey MARKUP = createTextAttributesKey("MARKUP", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
    public static final TextAttributesKey PACKAGE = createTextAttributesKey("PACKAGE", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey ARGUMENTS = createTextAttributesKey("ARGUMENTS", DefaultLanguageHighlighterColors.PARAMETER);

    public static final TextAttributesKey SUBROUTINE = createTextAttributesKey("SUBROUTINE", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("BAD_CHARACTER", getTextAttribute(Color.RED, true));

    private static final TextAttributesKey[] MARKUP_KEYS = new TextAttributesKey[]{MARKUP};
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] BRACES_KEYS = new TextAttributesKey[]{BRACES};
    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{OPERATOR};
    private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[]{KEY};
    private static final TextAttributesKey[] PACKAGE_KEYS = new TextAttributesKey[]{PACKAGE};
    private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[]{VALUE};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENTS};
    private static final TextAttributesKey[] SUBROUTINE_KEYS = new TextAttributesKey[]{SUBROUTINE};
    private static final TextAttributesKey[] ARGUMENTS_KEYS = new TextAttributesKey[]{ARGUMENTS};
    private static final TextAttributesKey[] LANG_VARIABLE_KEYS = new TextAttributesKey[]{LANG_VARIABLE};
    private static final TextAttributesKey[] LANG_FUNCTION_KEYS = new TextAttributesKey[]{LANG_FUNCTION};
    private static final TextAttributesKey[] LANG_SYNTAX_KEYS = new TextAttributesKey[]{LANG_SYNTAX};
    private static final TextAttributesKey[] LANG_FILE_HANDLES_KEYS = new TextAttributesKey[]{LANG_FILE_HANDLES};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    private static TextAttributes getTextAttribute(Color color, boolean bold) {
        return new TextAttributes(color, null, null, null, (bold) ? Font.BOLD : null);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new PerlLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {

        if (tokenType.equals(PerlTypes.LANG_VARIABLE)) {
            return LANG_VARIABLE_KEYS;
        } else if (tokenType.equals(PerlTypes.LANG_FUNCTION)) {
            return LANG_FUNCTION_KEYS;
        } else if (tokenType.equals(PerlTypes.LANG_SYNTAX)) {
            return LANG_SYNTAX_KEYS;
        } else if (tokenType.equals(PerlTypes.LANG_FILE_HANDLES)) {
            return LANG_FILE_HANDLES_KEYS;
        } else if (tokenType.equals(PerlTypes.SUBROUTINE)) {
            return SUBROUTINE_KEYS;
        } else if (tokenType.equals(PerlTypes.ARGUMENTS)) {
            return ARGUMENTS_KEYS;
        } else if (tokenType.equals(PerlTypes.BRACES)) {
            return BRACES_KEYS;
        } else if (tokenType.equals(PerlTypes.OPERATOR)) {
            return OPERATOR_KEYS;
        } else if (tokenType.equals(PerlTypes.KEY)) {
            return KEY_KEYS;
        } else if (tokenType.equals(PerlTypes.PACKAGE)) {
            return PACKAGE_KEYS;
        } else if (tokenType.equals(PerlTypes.VALUE)) {
            return VALUE_KEYS;
        } else if (tokenType.equals(PerlTypes.LINE_COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(PerlTypes.MARKUP)) {
            return MARKUP_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
