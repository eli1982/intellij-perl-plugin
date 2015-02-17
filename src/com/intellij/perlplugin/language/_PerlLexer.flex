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

LineComment = "#" {InputCharacter}* {LineTerminator}?

/*-*
 * PATTERN DEFINITIONS:
 */
letter          = [A-Za-z]
digit           = [0-9]
alphanumeric    = {letter}|{digit}|\_
Perdicate   = (\_|\$|\@|\%)
identifier      = {letter}{alphanumeric}+
integer         = {digit}*
real            = {integer}\.{integer}
char            = '.'
LeftBrace       = \{
RightBrace      = \}
RightParentheses= \(
LeftParentheses = \)
nonRightBrace   = [^}]
/*whitespace      = [ \r\n\t]*/
Subroutine="sub"[\ ]+{PropertyCharacter}+
Pointer="->"
HashKey="=>"
String=(\".*\")
PropertyCharacter=[^->\ \n\r\t\f\\\{\}=] | "\\"{LineTerminator}
PackageName=({identifier}::{identifier})+{identifier}+
head="=head1"
begin="=begin"
cut="=cut"
blockcomment=(=head1)(.|[\r\n])+?(=cut)?
Attribute=({Perdicate}({identifier})+);
Arguments=(\s+{Attribute}\s+)(\,\s+{Attribute}\s+)+
Variables=(\$\!|\$\"|\$\#|\$\$|\$\%|\$\&|\$\'|\$\*|\$\+|\$\,|\$\-|\$\.|\$\/|\$\0|\$\:|\$\;|\$\<|\$\=|\$\>|\$\?|\$\@|\$\^|\$\^\A|\$\^\C|\$\^CHILD_ERROR_NATIVE|\$\^D|\$\^E|\$\^ENCODING|\$\^F|\$\^H|\$\^I|\$\^L|\$\^M|\$\^N|\$\^O|\$\^OPEN|\$\^P|\$\^R|\$\^RE_DEBUG_FLAGS|\$\^RE_TRIE_MAXBUF|\$\^S|\$\^T|\$\^TAINT|\$\^UNICODE|\$\^UTF8LOCALE|\$\^V|\$\^W|\$\^WARNING_BITS|\$\^WIDE_SYSTEM_CALLS|\$\^X|\$\_|\$\`|\$ACCUMULATOR|\$ARG|\$ARGV|\$BASETIME|\$CHILD_ERROR|\$COMPILING|\$DEBUGGING|\$EFFECTIVE_GROUP_ID|\$EFFECTIVE_USER_ID|\$EGID|\$ERRNO|\$EUID|\$EVAL_ERROR|\$EXCEPTIONS_BEING_CAUGHT|\$EXECUTABLE_NAME|\$EXTENDED_OS_ERROR|\$FORMAT_FORMFEED|\$FORMAT_LINE_BREAK_CHARACTERS|\$FORMAT_LINES_LEFT|\$FORMAT_LINES_PER_PAGE|\$FORMAT_NAME|\$FORMAT_PAGE_NUMBER|\$FORMAT_TOP_NAME|\$GID|\$INPLACE_EDIT|\$INPUT_LINE_NUMBER|\$INPUT_RECORD_SEPARATOR|\$LAST_REGEXP_CODE_RESULT|\$LIST_SEPARATOR|\$MATCH|\$MULTILINE_MATCHING|\$NR|\$OFMT|\$OFS|\$ORS|\$OS_ERROR|\$OSNAME|\$OUTPUT_AUTO_FLUSH|\$OUTPUT_FIELD_SEPARATOR|\$OUTPUT_RECORD_SEPARATOR|\$PERL_VERSION|\$PERLDB|\$PID|\$POSTMATCH|\$PREMATCH|\$PROCESS_ID|\$PROGRAM_NAME|\$REAL_GROUP_ID|\$REAL_USER_ID|\$RS|\$SUBSCRIPT_SEPARATOR|\$SUBSEP|\$SYSTEM_FD_MAX|\$UID|\$WARNING|\$\~|\%\!|\%\^H|\%ENV|\%INC|\%OVERLOAD|\%SIG|\@\+|\@\-|\@\_|\@ARGV|\@INC|\@LAST_MATCH_START|\$LAST_MATCH_END|\$LAST_PAREN_MATCH)
Functions=(-A|-B|-b|-C|-c|-d|-e|-f|-g|-k|-l|-M|-O|-o|-p|-r|-R|-S|-s|-T|-t|-u|-w|-W|-X|-x|-z|abs|accept|alarm|atan2|AUTOLOAD|BEGIN|bind|binmode|bless|break|caller|chdir|CHECK|chmod|chomp|chop|chown|chr|chroot|close|closedir|connect|cos|crypt|dbmclose|dbmopen|defined|delete|DESTROY|die|dump|each|END|endgrent|endhostent|endnetent|endprotoent|endpwent|endservent|eof|eval|exec|exists|exit|fcntl|fileno|flock|fork|format|formline|getc|getgrent|getgrgid|getgrnam|gethostbyaddr|gethostbyname|gethostent|getlogin|getnetbyaddr|getnetbyname|getnetent|getpeername|getpgrp|getppid|getpriority|getprotobyname|getprotobynumber|getprotoent|getpwent|getpwnam|getpwuid|getservbyname|getservbyport|getservent|getsockname|getsockopt|glob|gmtime|goto|grep|hex|index|INIT|int|ioctl|join|keys|kill|last|lc|each|lcfirst|setnetent|length|link|listen|local|localtime|log|lstat|map|mkdir|msgctl|msgget|msgrcv|msgsnd|my|next|not|oct|open|opendir|ord|our|pack|pipe|pop|pos|print|printf|prototype|push|quotemeta|rand|read|readdir|readline|readlink|readpipe|recv|redo|ref|rename|require|reset|return|reverse|rewinddir|rindex|rmdir|say|scalar|seek|seekdir|select|semctl|semget|semop|send|setgrent|sethostent|each|lcfirst|setnetent|setpgrp|setpriority|setprotoent|setpwent|setservent|setsockopt|shift|shmctl|shmget|shmread|shmwrite|shutdown|sin|sleep|socket|socketpair|sort|splice|split|sprintf|sqrt|srand|stat|state|study|substr|symlink|syscall|sysopen|sysread|sysseek|system|syswrite|tell|telldir|tie|tied|time|times|truncate|uc|ucfirst|umask|undef|UNITCHECK|unlink|unpack|unshift|untie|use|utime|values|vec|wait|waitpid|wantarray|warn|write|each|lcfirst|setnetent)\b
Syntax=(and|cmp|continue|CORE|do|else|elsif|eq|exp|for|foreach|ge|gt|if|le|lock|lt|m|ne|no|or|package|q|qq|qr|qw|qx|s|sub|tr|unless|until|while|xor|y|\;)
Filehandles=(ARGV|ARGVOUT|STDERR|STDIN|STDOUT)
Symbols=(\,|\.|\/|\;|\-|\=|\+|\*)
%%
{PackageName}        { return PACKAGE; }
{LineTerminator} {return WHITESPACE;}
{LeftBrace} {return BRACES;}
{RightBrace} {return BRACES;}
{RightParentheses} {return BRACES;}
{LeftParentheses} {return BRACES;}
{Pointer} {return POINTER;}
{HashKey} {return HASH_KEY;}
{WhiteSpace} {return WHITESPACE;}
{String}        { return VALUE; }
{Syntax} {return LANG_SYNTAX;}
{Symbols} {return LANG_SYNTAX;}
{Arguments} {return ARGUMENTS;}
{Variables} {return LANG_VARIABLE;}
{Functions} {return LANG_FUNCTION;}
{Filehandles} {return LANG_FILE_HANDLES;}
({Perdicate})?{identifier} {return ATTRIBUTE;}
/*{PropertyCharacter}+ { return PROPERTY; }*/
{Subroutine} { return SUBROUTINE; }
{LineComment} {return LINE_COMMENT;}
/*{blockcomment} {return LINE_COMMENT;}*/
[^] {return TokenType.BAD_CHARACTER;}