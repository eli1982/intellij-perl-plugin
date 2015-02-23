package com.intellij.perlplugin.extensions;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.bo.ImportedSub;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.bo.Sub;
import com.intellij.perlplugin.language.PerlIcons;
import com.intellij.perlplugin.language.PerlLanguage;
import com.intellij.perlplugin.psi.PerlTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;

public class PerlCompletionContributor extends CompletionContributor {
    private static HashMap<String, LookupElement> variablesCache = new HashMap<String, LookupElement>();
    private static HashMap<Sub, LookupElement> subsCache = new HashMap<Sub, LookupElement>();
    private static HashMap<Sub, LookupElement> subsCacheNoArgs = new HashMap<Sub, LookupElement>();
    private static HashMap<Package, LookupElement> packagesCache = new HashMap<Package, LookupElement>();
    private static boolean updateFlipper = false;


    public PerlCompletionContributor() {

        CompletionProvider<CompletionParameters> handler = new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {

                if (!ModulesContainer.isInitialized()) {
                    Utils.alert("warning: perl parser was not initialized");
                }
                Editor editor = parameters.getEditor();
                VirtualFile virtualFile = parameters.getOriginalFile().getVirtualFile();
                if (editor == null || virtualFile == null) {
                    return;
                }


                PsiElement currentElement = parameters.getOriginalPosition();
                if (currentElement == null) {
                    currentElement = parameters.getPosition();//handle case where we are auto completing in the end of file
                }
                PsiElement prevElement = prevSibling(currentElement, 1);

                //current element based
                if (is(currentElement, PerlTypes.PROPERTY)) {
                    addAllPackages(resultSet, currentElement);
                    if (currentElement.getTextLength() >= 2) {
                        addLanguageKeyword(resultSet, currentElement.getText());
                    }
                } else if (is(currentElement, PerlTypes.WHITESPACE) && !is(prevElement, PerlTypes.POINTER) || is(currentElement, PerlTypes.BRACES)) {
                    //qw subs auto complete
                    if (prevElement != null) {
                        PsiElement brace = prevSibling(currentElement, 1);
                        while (is(brace, PerlTypes.WHITESPACE) || is(brace, PerlTypes.VARIABLE) || is(brace, PerlTypes.PROPERTY)) {
                            brace = prevSibling(brace, 1);
                        }
                        if (is(brace, PerlTypes.BRACES) && (brace.getText().equals("(") || brace.getText().equals(")"))) {
                            boolean condition = is(prevSibling(brace, 1), PerlTypes.LANG_SYNTAX) && prevSibling(brace, 1).getText().equals("qw")
                                    && is(prevSibling(brace, 2), PerlTypes.WHITESPACE)
                                    && is(prevSibling(brace, 3), PerlTypes.PACKAGE)
                                    && is(prevSibling(brace, 4), PerlTypes.WHITESPACE)
                                    && is(prevSibling(brace, 5), PerlTypes.LANG_FUNCTION) && prevSibling(brace, 5).getText().equals("use");
                            if (condition) {
                                addAllSubsInPackage(resultSet, prevSibling(brace, 3), false);
                                return;
                            }
                        }
                    }
                    addAllSubsInFile(parameters, resultSet);
                    addAllVariablesInFile(parameters, resultSet);
                } else if (is(currentElement, PerlTypes.VARIABLE) || is(currentElement, PerlTypes.VALUE) || is(currentElement, PerlTypes.PREDICATE) || is(currentElement, PerlTypes.BRACES) || is(currentElement, PerlTypes.LANG_SYNTAX)) {
                    addAllVariablesInFile(parameters, resultSet);
                } else if (is(currentElement, PerlTypes.PACKAGE)) {
                    addAllPackages(resultSet, currentElement);
                }/* else if (is(currentElement, PerlTypes.SUBROUTINE)) {
                    ModulesContainer.updateFile(virtualFile.getPath(),editor.getDocument().getText());
                }*/


                //prev element based
                if (is(prevElement, PerlTypes.POINTER)) {
                    PsiElement prevPrevElement = prevSibling(prevElement, 1);
                    if (is(prevPrevElement, PerlTypes.PACKAGE)) {
                        //get all subs of package if we are on a package's pointer
                        addAllSubsInPackage(resultSet, prevPrevElement, true);
                    } else if (is(prevPrevElement, PerlTypes.VARIABLE)) {
                        //get all subs of current package if we are on an variable pointer
                        addAllSubsInFile(parameters, resultSet);
                    }
                } else if (is(prevElement, PerlTypes.WHITESPACE)) {
                    addAllSubsInFile(parameters, resultSet);
                }

                //ya, i know this is crappy - temporary fix
                if(updateFlipper) {
                    ModulesContainer.updateFile(virtualFile.getPath(), editor.getDocument().getText());
                }
                updateFlipper = !updateFlipper;
            }
        };
        addCompleteHandler(PerlTypes.PROPERTY, handler);
        addCompleteHandler(PerlTypes.OPERATOR, handler);
        addCompleteHandler(PerlTypes.POINTER, handler);
        addCompleteHandler(PerlTypes.PACKAGE, handler);
        addCompleteHandler(PerlTypes.VARIABLE, handler);
        addCompleteHandler(PerlTypes.WHITESPACE, handler);
        addCompleteHandler(PerlTypes.VALUE, handler);
        addCompleteHandler(PerlTypes.PREDICATE, handler);
        addCompleteHandler(PerlTypes.LANG_SYNTAX, handler);
        addCompleteHandler(PerlTypes.BRACES, handler);
        addCompleteHandler(PerlTypes.SUBROUTINE, handler);
    }

    private void addLanguageKeyword(CompletionResultSet resultSet, String text) {
        //TODO:: order most common used first
        String keywords = "|abs|accept|alarm|atan2|AUTOLOAD|BEGIN|bind|binmode|bless|break|caller|chdir|CHECK|chmod|chomp|chop|chown|chr|chroot|close|closedir|connect|cos|crypt|dbmclose|dbmopen|defined|delete|DESTROY|die|dump|each|END|endgrent|endhostent|endnetent|endprotoent|endpwent|endservent|eof|eval|exec|exists|exit|fcntl|fileno|flock|fork|format|formline|getc|getgrent|getgrgid|getgrnam|gethostbyaddr|gethostbyname|gethostent|getlogin|getnetbyaddr|getnetbyname|getnetent|getpeername|getpgrp|getppid|getpriority|getprotobyname|getprotobynumber|getprotoent|getpwent|getpwnam|getpwuid|getservbyname|getservbyport|getservent|getsockname|getsockopt|glob|gmtime|goto|grep|hex|index|INIT|int|ioctl|join|keys|kill|last|lc|each|lcfirst|setnetent|length|link|listen|local|localtime|log|lstat|map|mkdir|msgctl|msgget|msgrcv|msgsnd|next|not|oct|open|opendir|ord|our|pack|pipe|pop|pos|print|printf|prototype|push|quotemeta|rand|read|readdir|readline|readlink|readpipe|recv|redo|ref|rename|require|reset|return|reverse|rewinddir|rindex|rmdir|say|scalar|seek|seekdir|select|semctl|semget|semop|send|setgrent|sethostent|each|lcfirst|setnetent|setpgrp|setpriority|setprotoent|setpwent|setservent|setsockopt|shift|shmctl|shmget|shmread|shmwrite|shutdown|sin|sleep|socket|socketpair|sort|splice|split|sprintf|sqrt|srand|stat|state|study|substr|symlink|syscall|sysopen|sysread|sysseek|system|syswrite|tell|telldir|tie|tied|time|times|truncate|ucfirst|umask|undef|UNITCHECK|unlink|unpack|unshift|untie|use|utime|values|vec|wait|waitpid|wantarray|warn|write|each|lcfirst|setnetent|cmp|continue|CORE|else|elsif|exp|for|foreach|lock|package|unless|until|while|ARGV|ARGVOUT|STDERR|STDIN|STDOUT";
        Matcher matcher = Utils.applyRegex("\\|(" + text + "[^\\|]+)", keywords);
        while (matcher.find()) {
            resultSet.addElement(LookupElementBuilder.create(matcher.group(1)));
        }
    }

    //this is a temporary solution until we will have structured code blocks
    private PsiElement prevSibling(PsiElement psiElement, int times) {
        PsiElement result = psiElement;
        if (result != null) {
            for (int i = 0; i < times; i++) {
                if (result.getPrevSibling() != null) {
                    //get sibling
                    result = result.getPrevSibling();
                    while (is(result, GeneratedParserUtilBase.DUMMY_BLOCK)) {
                        result = result.getLastChild();
                    }
                } else if (result.getParent() != null && result.getParent().getPrevSibling() != null) {
                    //get sibling from previous parent
                    result = result.getParent().getPrevSibling();
                    while (is(result, GeneratedParserUtilBase.DUMMY_BLOCK)) {
                        result = result.getLastChild();
                    }
                } else {
                    //we have no sibling - no point to continue
                    return null;
                }
            }
        }
        return result;
    }

    private boolean is(PsiElement element, IElementType perlType) {
        return element != null && element.getNode().getElementType().equals(perlType);
    }

    //add cached methods
    private void addAllPackages(CompletionResultSet resultSet, PsiElement element) {
        ArrayList<Package> packageList = ModulesContainer.searchPackageList(element.getText());
        for (int i = 0; i < packageList.size(); i++) {
            addCachedPackage(resultSet, packageList.get(i));
        }
    }

    private void addAllSubsInPackage(CompletionResultSet resultSet, PsiElement packageName, boolean withArguments) {
        ArrayList<Package> packageList = ModulesContainer.getPackageList(packageName.getText());
        if (Utils.verbose) {
            Utils.print("Detected Package:" + packageName);
        }
        for (int i = 0; i < packageList.size(); i++) {
            Package packageObj = packageList.get(i);
            ArrayList<Sub> subs = packageObj.getAllSubs();
            for (int j = 0; j < subs.size(); j++) {
                if (withArguments) {
                    addCachedSub(resultSet, subs.get(j));
                } else {
                    addCachedSubNoArgs(resultSet, subs.get(j));
                }
            }
        }
    }

    private void addAllSubsInFile(CompletionParameters parameters, CompletionResultSet resultSet) {
        ArrayList<Package> packageList = ModulesContainer.getPackageListFromFile(parameters.getOriginalFile().getVirtualFile().getCanonicalPath());
        for (int i = 0; i < packageList.size(); i++) {
            ArrayList<Sub> subs = packageList.get(i).getAllSubs();
            for (int j = 0; j < subs.size(); j++) {
                addCachedSub(resultSet, subs.get(j));
            }
            ArrayList<ImportedSub> importedSubs = packageList.get(i).getImportedSubs();
            for (int j = 0; j < importedSubs.size(); j++) {
                ArrayList<Package> packages = ModulesContainer.getPackageList(importedSubs.get(j).getContainingPackage());//TODO: handle more than 1 package
                if (packages.size() > 0) {
                    Sub sub = packages.get(0).getSubByName(importedSubs.get(j).getImportSub());
                    addCachedSub(resultSet, sub);
                }
            }
        }
    }

    private void addAllVariablesInFile(CompletionParameters parameters, CompletionResultSet resultSet) {
        HashSet<String> rs = findAllVariables(parameters.getOriginalFile().getNode().getChildren(null), PerlTypes.VARIABLE);
        for (String str : rs) {
            addCachedVariables(resultSet, str);
        }
    }

    private void addCachedPackage(CompletionResultSet resultSet, Package packageObj) {
        if (!packagesCache.containsKey(packageObj)) {
            packagesCache.put(packageObj, getPackageLookupElementBuilder(packageObj));
        }
        resultSet.addElement(packagesCache.get(packageObj));
    }

    private void addCachedSub(CompletionResultSet resultSet, Sub sub) {
        if (!subsCache.containsKey(sub)) {
            subsCache.put(sub, getSubLookupElementBuilder(sub, true));
        }
        resultSet.addElement(subsCache.get(sub));
    }

    private void addCachedSubNoArgs(CompletionResultSet resultSet, Sub sub) {
        if (!subsCacheNoArgs.containsKey(sub)) {
            subsCacheNoArgs.put(sub, getSubLookupElementBuilder(sub, false));
        }
        resultSet.addElement(subsCacheNoArgs.get(sub));
    }

    private void addCachedVariables(CompletionResultSet resultSet, String str) {
        if (!variablesCache.containsKey(str)) {
            variablesCache.put(str, getVariableLookupElementBuilder(str));
        }
        resultSet.addElement(variablesCache.get(str));
    }


    //get lookup elements methods
    private LookupElement getPackageLookupElementBuilder(Package packageObj) {
        String text = packageObj.getPackageName();
        if (Utils.verbose) {
            Utils.print("package: " + text);
        }
        return LookupElementBuilder.create(text).withIcon(PerlIcons.PACKAGE).withTypeText("Package", true);
    }

    private LookupElement getSubLookupElementBuilder(Sub sub, boolean withArguments) {
        String text = (withArguments) ? sub.toString2() : sub.getName();
        String containingPackage = sub.getPackageObj().getPackageName();
        if (Utils.verbose) {
            Utils.print("sub: " + text + " , containingPackage:" + containingPackage);
        }
        return LookupElementBuilder.create(text).withIcon(PerlIcons.SUBROUTINE).withTypeText(containingPackage, true);
    }

    private LookupElement getVariableLookupElementBuilder(String text) {
        if (Utils.verbose) {
            Utils.print("variable: " + text);
        }
        return LookupElementBuilder.create(text).withIcon(PerlIcons.VARIABLE).withTypeText("Variable", true);
    }

    private HashSet<String> findAllVariables(ASTNode[] children, IElementType type) {
        HashSet<String> resultSet = new HashSet<String>();
        return findAllVariables(children, resultSet, type);
    }

    private HashSet<String> findAllVariables(ASTNode[] children, HashSet<String> resultSet, IElementType type) {
        for (int i = 0; i < children.length; i++) {
            ASTNode astNode = children[i].findChildByType(type);
            if (astNode != null) {
                resultSet.add(astNode.getText());
            } else if (children[i].getChildren(null) != null) {
                findAllVariables(children[i].getChildren(null), resultSet, type);
            }
        }
        return resultSet;
    }

    private void addCompleteHandler(IElementType elementType, CompletionProvider<CompletionParameters> handler) {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(elementType).withLanguage(PerlLanguage.INSTANCE), handler);
    }

    public static void clear() {
        //variablesCache.clear();//temporarily commented to improve performance
        subsCache.clear();
        subsCacheNoArgs.clear();
        packagesCache.clear();
    }
}
