package com.intellij.perlplugin.extensions;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.Utils;
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

public class PerlCompletionContributor extends CompletionContributor {
    private static HashMap<String, LookupElement> variablesCache = new HashMap<String, LookupElement>();
    private static HashMap<Sub, LookupElement> subsCache = new HashMap<Sub, LookupElement>();
    private static HashMap<Package, LookupElement> packagesCache = new HashMap<Package, LookupElement>();

    public PerlCompletionContributor() {
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PerlTypes.KEY).withLanguage(PerlLanguage.INSTANCE), new CompletionProvider<CompletionParameters>() {
//            public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
//                resultSet.addElement(LookupElementBuilder.create("KEY"));
//            }
//        });
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PerlTypes.PROPERTY).withLanguage(PerlLanguage.INSTANCE), new CompletionProvider<CompletionParameters>() {
//            public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
//                resultSet.addElement(LookupElementBuilder.create("PROPERTY"));
//            }
//        });
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PerlTypes.OPERATOR).withLanguage(PerlLanguage.INSTANCE), new CompletionProvider<CompletionParameters>() {
//            public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
//                resultSet.addElement(LookupElementBuilder.create("OPERATOR"));
//            }
//        });
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PerlTypes.VALUE).withLanguage(PerlLanguage.INSTANCE), new CompletionProvider<CompletionParameters>() {
//            public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
//                resultSet.addElement(LookupElementBuilder.create("VALUE"));
//            }
//        });
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PerlTypes.SEPARATOR).withLanguage(PerlLanguage.INSTANCE), new CompletionProvider<CompletionParameters>() {
//            public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
//                resultSet.addElement(LookupElementBuilder.create("SEPARATOR"));
//            }
//        });
//
//    }
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
                PsiElement prevElement = parameters.getOriginalPosition().getPrevSibling();

                if (is(currentElement, PerlTypes.PROPERTY)) {
                    getAllPackages(resultSet, currentElement);
                }else if (is(currentElement, PerlTypes.WHITESPACE)) {
                    getAllSubsInFile(resultSet, editor, virtualFile);
                } else if (is(currentElement, PerlTypes.VALUE) || is(prevElement, PerlTypes.PREDICATE) || is(prevElement, PerlTypes.BRACES)) {
                    getAllVariablesInFile(parameters, resultSet);
                } else if (is(currentElement, PerlTypes.PACKAGE)) {
                    getAllPackages(resultSet, currentElement);
                }

                if (is(prevElement, PerlTypes.POINTER)) {
                    if (is(prevElement.getPrevSibling(), PerlTypes.PACKAGE)) {
                        //get all subs of package if we are on a package's pointer
                        String packageName = prevElement.getPrevSibling().getText();
                        ArrayList<Package> packageList = ModulesContainer.getPackageList(packageName);
                        if (Utils.debug) {
                            Utils.print("Detected Package:" + packageName);
                        }
                        for (int i = 0; i < packageList.size(); i++) {
                            Package packageObj = packageList.get(i);
                            ArrayList<Sub> subs = packageObj.getAllSubs();
                            for (int j = 0; j < subs.size(); j++) {
                                addCachedSub(resultSet, subs.get(j));
                            }
                        }
                    } else if (is(prevElement.getPrevSibling(), PerlTypes.VARIABLE)) {
                        //get all subs of current package if we are on an variable pointer
                        ArrayList<Package> packageList = ModulesContainer.getPackageListFromFile(parameters.getOriginalFile().getVirtualFile().getCanonicalPath());
                        for (int i = 0; i < packageList.size(); i++) {
                            Package packageObj = packageList.get(i);
                            ArrayList<Sub> subs = packageObj.getAllSubs();
                            for (int j = 0; j < subs.size(); j++) {
                                addCachedSub(resultSet, subs.get(j));
                            }
                        }
                    }
                } else if (is(prevElement, PerlTypes.WHITESPACE)) {
                    getAllSubsInFile(resultSet, editor, virtualFile);
                }
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
    }

    private void getAllPackages(CompletionResultSet resultSet, PsiElement element) {
        ArrayList<Package> packageList = ModulesContainer.searchPackageList(element.getText());
        for (int i = 0; i < packageList.size(); i++) {
            addCachedPackage(resultSet, packageList.get(i));
        }
    }

    private void getAllVariablesInFile(CompletionParameters parameters, CompletionResultSet resultSet) {
        HashSet<String> rs = findAllVariables(parameters.getOriginalFile().getNode().getChildren(null), PerlTypes.VARIABLE);
        for (String str : rs) {
            addCachedVariables(resultSet, str);
        }
    }

    private void getAllSubsInFile(CompletionResultSet resultSet, Editor editor, VirtualFile virtualFile) {
        //get all subs in file
        ArrayList<Package> packageList = ModulesContainer.getPackageListFromFile(virtualFile.getCanonicalPath());
        for (int i = 0; i < packageList.size(); i++) {
            Package packageObj = packageList.get(i);
            if (editor.getCaretModel().getOffset() > packageObj.getStartPositionInFile() &&
                    editor.getCaretModel().getOffset() < packageObj.getEndPositionInFile()) {
                ArrayList<Sub> subs = packageObj.getAllSubs();
                for (int j = 0; j < subs.size(); j++) {
                    addCachedSub(resultSet, subs.get(j));
                }
                break;
            }
        }
    }


    private boolean is(PsiElement element, IElementType perlType) {
        return element != null && element.getNode().getElementType().equals(perlType);
    }

    //add cached methods
    private void addCachedPackage(CompletionResultSet resultSet, Package packageObj) {
        if (!packagesCache.containsKey(packageObj)) {
            packagesCache.put(packageObj, getPackageLookupElementBuilder(packageObj));
        }
        resultSet.addElement(packagesCache.get(packageObj));
    }

    private void addCachedSub(CompletionResultSet resultSet, Sub sub) {
        if (!subsCache.containsKey(sub)) {
            subsCache.put(sub, getSubLookupElementBuilder(sub));
        }
        resultSet.addElement(subsCache.get(sub));
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
        if (Utils.debug) {
            Utils.print("package: " + text);
        }
        return LookupElementBuilder.create(text).withIcon(PerlIcons.PACKAGE).withTypeText("Package", true);
    }

    private LookupElement getSubLookupElementBuilder(Sub sub) {
        String text = sub.toString2();
        String containingPackage = sub.getPackageObj().getPackageName();
        if (Utils.debug) {
            Utils.print("sub: " + text + " , containingPackage:" + containingPackage);
        }
        return LookupElementBuilder.create(text).withIcon(PerlIcons.SUBROUTINE).withTypeText(containingPackage, true);
    }

    private LookupElement getVariableLookupElementBuilder(String text) {
        if (Utils.debug) {
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
}
