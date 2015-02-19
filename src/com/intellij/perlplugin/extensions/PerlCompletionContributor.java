package com.intellij.perlplugin.extensions;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.bo.Sub;
import com.intellij.perlplugin.language.PerlLanguage;
import com.intellij.perlplugin.psi.PerlTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

public class PerlCompletionContributor extends CompletionContributor {


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

                if (prevElement != null && prevElement.getPrevSibling() != null && prevElement.getNode().getElementType().equals(PerlTypes.POINTER)) {
                    //get all subs of package if we are on a package's pointer
                    if (prevElement.getPrevSibling().getNode().getElementType().equals(PerlTypes.PACKAGE)) {
                        String packageName = prevElement.getPrevSibling().getText();
                        ArrayList<Package> packageList = ModulesContainer.getPackageList(packageName);
                        if (Utils.debug) {
                            Utils.print("Detected Package:" + packageName);
                        }
                        for (int i = 0; i < packageList.size(); i++) {
                            Package packageObj = packageList.get(i);
                            ArrayList<Sub> subs = packageObj.getAllSubs();
                            for (int j = 0; j < subs.size(); j++) {
                                Sub sub = subs.get(j);
                                resultSet.addElement(getSubLookupElementBuilder(sub));
                            }
                        }
                    } else if (prevElement.getPrevSibling().getNode().getElementType().equals(PerlTypes.ATTRIBUTE)) {
                        //get all subs of current package if we are on an attributes pointer
                        ArrayList<Package> packageList = ModulesContainer.getPackageListFromFile(parameters.getOriginalFile().getVirtualFile().getCanonicalPath());
                        for (int i = 0; i < packageList.size(); i++) {
                            Package packageObj = packageList.get(i);
                            ArrayList<Sub> subs = packageObj.getAllSubs();
                            for (int j = 0; j < subs.size(); j++) {
                                Sub sub = subs.get(j);
                                resultSet.addElement(getSubLookupElementBuilder(sub));
                            }
                        }
                    }
                }

                //get all subs in file
                ArrayList<Package> packageList = ModulesContainer.getPackageListFromFile(virtualFile.getCanonicalPath());
                for (int i = 0; i < packageList.size(); i++) {
                    Package packageObj = packageList.get(i);
                    if (editor.getCaretModel().getOffset() > packageObj.getStartPositionInFile() &&
                            editor.getCaretModel().getOffset() < packageObj.getEndPositionInFile()) {
                        ArrayList<Sub> subs = packageObj.getAllSubs();
                        for (int j = 0; j < subs.size(); j++) {
                            Sub sub = subs.get(j);
                            resultSet.addElement(getSubLookupElementBuilder(sub));
                        }
                        break;
                    }
                }

                boolean attributeFound = false;
                //get all attributes in file if we are in value (String)
                if (currentElement != null && currentElement.getNode().getElementType().equals(PerlTypes.VALUE)) {
                    HashSet<String> rs = findAllAttributes(parameters.getOriginalFile().getNode().getChildren(null), PerlTypes.ATTRIBUTE);
                    for (String str : rs) {
                        resultSet.addElement(getAttributeLookupElementBuilder(str));
                        attributeFound = true;
                    }
                }

                //get all attributes in file if we are in a pointer or have a pointer an element before that
                if (!attributeFound && (prevElement != null && (prevElement.getNode().getElementType().equals(PerlTypes.POINTER))
                        || (prevElement != null && prevElement.getPrevSibling() != null && prevElement.getPrevSibling().getNode().getElementType().equals(PerlTypes.POINTER)))) {
                    HashSet<String> rs = findAllAttributes(parameters.getOriginalFile().getNode().getChildren(null), PerlTypes.ATTRIBUTE);
                    for (String str : rs) {
                        resultSet.addElement(getAttributeLookupElementBuilder(str));
                        attributeFound = true;
                    }
                }

                //Predicate AutoCompletion ( _ $ @ % )
                if (!attributeFound && currentElement != null && currentElement.getNode().getElementType().equals(PerlTypes.PREDICATE)) {
                    HashSet<String> rs = findAllAttributes(parameters.getOriginalFile().getNode().getChildren(null), PerlTypes.ATTRIBUTE);
                    for (String str : rs) {
                        resultSet.addElement(getAttributeLookupElementBuilder(str.replaceFirst("\\_|\\$|\\%|\\@|\\&", "")));
                        attributeFound = true;
                    }
                }
            }
        };
        addCompleteHandler(PerlTypes.PROPERTY, handler);
        addCompleteHandler(PerlTypes.OPERATOR, handler);
        addCompleteHandler(PerlTypes.POINTER, handler);
        addCompleteHandler(PerlTypes.PACKAGE, handler);
        addCompleteHandler(PerlTypes.ATTRIBUTE, handler);
        addCompleteHandler(PerlTypes.WHITESPACE, handler);
        addCompleteHandler(PerlTypes.VALUE, handler);
        addCompleteHandler(PerlTypes.PREDICATE, handler);
    }

    private HashSet<String> findAllAttributes(ASTNode[] children, IElementType type) {
        HashSet<String> resultSet = new HashSet<String>();
        return findAllAttributes(children, resultSet, type);
    }

    private HashSet<String> findAllAttributes(ASTNode[] children, HashSet<String> resultSet, IElementType type) {
        for (int i = 0; i < children.length; i++) {
            ASTNode astNode = children[i].findChildByType(type);
            if (astNode != null) {
                resultSet.add(astNode.getText());
            } else if (children[i].getChildren(null) != null) {
                findAllAttributes(children[i].getChildren(null), resultSet, type);
            }
        }
        return resultSet;
    }

    private LookupElementBuilder getSubLookupElementBuilder(Sub sub) {
        String text = sub.toString2();
        String containingPackage = sub.getPackageObj().getPackageName();
        if (Utils.debug) {
            Utils.print("sub: " + text + " , containingPackage:" + containingPackage);
        }
        return LookupElementBuilder.create(text).withIcon(AllIcons.Nodes.Method).withTypeText(containingPackage, true);
    }

    private LookupElement getAttributeLookupElementBuilder(String text) {
        if (Utils.debug) {
            Utils.print("Attribute: " + text);
        }
        return LookupElementBuilder.create(text).withIcon(AllIcons.Nodes.Parameter).withTypeText("Attribute", true);
    }

    private void addCompleteHandler(IElementType elementType, CompletionProvider<CompletionParameters> handler) {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(elementType).withLanguage(PerlLanguage.INSTANCE), handler);
    }
}
