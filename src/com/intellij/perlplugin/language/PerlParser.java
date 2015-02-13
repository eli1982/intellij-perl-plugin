// This is a generated file. Not intended for manual editing.
package com.intellij.perlplugin.language;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static com.intellij.perlplugin.language.PerlParserUtil.*;
import static com.intellij.perlplugin.psi.PerlTypes.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PerlParser implements PsiParser {

    /* ********************************************************** */
    // property|COMMENTS|CRLF
    static boolean item_(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "item_")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = property(b, l + 1);
        if (!r) r = consumeToken(b, ENDOFLINECOMMENT);
        if (!r) r = consumeToken(b, CRLF);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // item_*
    static boolean perlFile(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "perlFile")) return false;
        int c = current_position_(b);
        while (true) {
            if (!item_(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "perlFile", c)) break;
            c = current_position_(b);
        }
        return true;
    }

    /* ********************************************************** */
    // (KEY? SEPARATOR VALUE?) | (KEY? OPERATOR VALUE?)|  (KEY? '->' VALUE?)  | KEY
    public static boolean property(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, "<property>");
        r = property_0(b, l + 1);
        if (!r) r = property_1(b, l + 1);
        if (!r) r = property_2(b, l + 1);
        if (!r) r = consumeToken(b, KEY);
        exit_section_(b, l, m, PROPERTY, r, false, null);
        return r;
    }

    // KEY? SEPARATOR VALUE?
    private static boolean property_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = property_0_0(b, l + 1);
        r = r && consumeToken(b, BRACES);
        r = r && property_0_2(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // KEY?
    private static boolean property_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_0_0")) return false;
        consumeToken(b, KEY);
        return true;
    }

    // VALUE?
    private static boolean property_0_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_0_2")) return false;
        consumeToken(b, VALUE);
        return true;
    }

    // KEY? OPERATOR VALUE?
    private static boolean property_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = property_1_0(b, l + 1);
        r = r && consumeToken(b, OPERATOR);
        r = r && property_1_2(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // KEY?
    private static boolean property_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_1_0")) return false;
        consumeToken(b, KEY);
        return true;
    }

    // VALUE?
    private static boolean property_1_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_1_2")) return false;
        consumeToken(b, VALUE);
        return true;
    }

    // KEY? '->' VALUE?
    private static boolean property_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_2")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = property_2_0(b, l + 1);
        r = r && consumeToken(b, "->");
        r = r && property_2_2(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // KEY?
    private static boolean property_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_2_0")) return false;
        consumeToken(b, KEY);
        return true;
    }

    // VALUE?
    private static boolean property_2_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_2_2")) return false;
        consumeToken(b, VALUE);
        return true;
    }

    public ASTNode parse(IElementType t, PsiBuilder b) {
        parseLight(t, b);
        return b.getTreeBuilt();
    }

    public void parseLight(IElementType t, PsiBuilder b) {
        boolean r;
        b = adapt_builder_(t, b, this, null);
        Marker m = enter_section_(b, 0, _COLLAPSE_, null);
        if (t == PROPERTY) {
            r = property(b, 0);
        } else {
            r = parse_root_(t, b, 0);
        }
        exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
    }

    protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
        return perlFile(b, l + 1);
    }

}
