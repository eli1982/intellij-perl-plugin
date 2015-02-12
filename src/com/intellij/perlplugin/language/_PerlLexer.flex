package com.intellij.perlplugin.language;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import static com.intellij.perlplugin.psi.PerlTypes.*;

%%

%{
  public _PerlLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _PerlLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode


%state OPERATION

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

EndOfLineComment = "#" {InputCharacter}* {LineTerminator}?

/*-*
 * PATTERN DEFINITIONS:
 */
letter          = [A-Za-z]
digit           = [0-9]
alphanumeric    = {letter}|{digit}
other_id_char   = [_]
identifier      = {letter}({alphanumeric}|{other_id_char})*
integer         = {digit}*
real            = {integer}\.{integer}
char            = '.'
leftbrace       = \{
rightbrace      = \}
nonrightbrace   = [^}]
whitespace      = [ \r\n\t]
subroutine="sub"[\ ]+{propertycharacter}+
pointer="->"
propertycharacter=[^->\ \n\r\t\f\\\{\}=] | "\\"{LineTerminator}
packagename=({identifier}+::{identifier})+({identifier})+(;?)
head="=head1"
begin="=begin"
cut="=cut"
blockcomment=({head}|{begin})({char}|{LineTerminator})*?({cut})
%%
{packagename}        { return PACKAGE; }
{LineTerminator} {return WHITESPACE;}
{leftbrace} {return BRACES;}
{rightbrace} {return BRACES;}
{pointer} {return POINTER;}
{whitespace}+ {return WHITESPACE;}
"="        { return VALUE; }
{propertycharacter}+ { return PROPERTY; }
{subroutine} { return SUBROUTINE; }
{EndOfLineComment} {return ENDOFLINECOMMENT;}
{blockcomment} {return ENDOFLINECOMMENT;}
[^] {return TokenType.BAD_CHARACTER;}