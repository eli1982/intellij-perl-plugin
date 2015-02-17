package com.intellij.perlplugin.extensions;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.bo.Package;
import com.intellij.perlplugin.psi.PerlTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.PsiShortNamesCache;
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
                VirtualFile res = getVirtualFileFromPath(project,file);//element.getProject().getBaseDir().findChild("PerlDummyProject").findChild("src").findChild("test").findChild(new File(file).getName());
                if(res != null) {
                    targets[i] = PsiManager.getInstance(project).findFile(res);
                }else{
                    targets = new PsiElement[0];
                }
            }

            if (packageList.size() > 0 && targets.length > 0) {
                NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(AllIcons.Nodes.Class).
                                setTargets(targets).
                                setTooltipText("Navigate to package");
                result.add(builder.createLineMarkerInfo(element));
            }
        } else if (element.getNode().getElementType().equals(PerlTypes.SUBROUTINE)) {
            boolean isConstructor = Utils.applyRegex("sub\\s+new", element.getNode().getText()).find();
            NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create((isConstructor) ? AllIcons.Nodes.ClassInitializer : AllIcons.Nodes.Method).
                    setTargets(element).
                    setTooltipText((isConstructor) ? "Constructor" : "");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

    private VirtualFile getVirtualFileFromPath(Project project, String filePath) {
        File file = new File(filePath);
        VirtualFile vFile = project.getBaseDir();
        VirtualFile result = vFile;

        File temp = file;
        boolean first = true;
        while(true) {
            if (result.findChild(temp.getName()) == null){
                first = false;
                temp = temp.getParentFile();
                if(temp == null){
                    //couldn't find file
                    return  null;
                }
            }else {
                result = result.findChild(temp.getName());
                if(first) {
                    //file found
                    return result;
                }else{
                    temp = file;
                }
                first = true;
            }
        }
    }
}