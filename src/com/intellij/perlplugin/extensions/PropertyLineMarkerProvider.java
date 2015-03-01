package com.intellij.perlplugin.extensions;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.language.PerlIcons;
import com.intellij.perlplugin.psi.PerlTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertyLineMarkerProvider extends RelatedItemLineMarkerProvider {
    public PropertyLineMarkerProvider() {
        super();
    }

    @Override
    public RelatedItemLineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        return super.getLineMarkerInfo(element);
    }

    @Override
    public void collectNavigationMarkers(List<PsiElement> elements, Collection<? super RelatedItemLineMarkerInfo> result, boolean forNavigation) {
        super.collectNavigationMarkers(elements, result, forNavigation);
    }

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        if (element !=null && element.getNode().getElementType().equals(PerlTypes.PACKAGE)) {
            Project project = element.getProject();
            ArrayList<Package> packageList = ModulesContainer.getPackageList(element.getText().replace(";", ""));
            PsiElement[] targets = new PsiElement[packageList.size()];

            for (int i = 0; i < packageList.size(); i++) {
                String file = packageList.get(i).getOriginFile();
                VirtualFile res = ModulesContainer.getVirtualFileFromPath(project,file);//element.getProject().getBaseDir().findChild("PerlDummyProject").findChild("src").findChild("test").findChild(new File(file).getName());
                if(res != null) {
                    targets[i] = PsiManager.getInstance(project).findFile(res);
                }else{
                    targets = new PsiElement[0];
                }
            }

            if (packageList.size() > 0 && targets.length > 0) {
                NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(PerlIcons.PACKAGE).
                                setTargets(targets).
                                setTooltipText("Navigate to package");
                result.add(builder.createLineMarkerInfo(element));
            }
        } else if (element !=null && element.getNode().getElementType().equals(PerlTypes.SUBROUTINE)) {
            boolean isConstructor = Utils.applyRegex("sub\\s+new", element.getNode().getText()).find();
            NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create((isConstructor) ? PerlIcons.CONSTRUCTOR : PerlIcons.SUBROUTINE).
                    setTargets(element).
                    setTooltipText((isConstructor) ? "Constructor" : "");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

}