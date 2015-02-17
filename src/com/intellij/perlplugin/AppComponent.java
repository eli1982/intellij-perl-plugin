package com.intellij.perlplugin;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by eli on 27-11-14.
 */
public class AppComponent implements ApplicationComponent {
    public AppComponent() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "AppComponent";
    }
}
