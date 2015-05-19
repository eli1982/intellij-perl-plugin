package com.intellij.perlplugin.components;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.PerlInternalParser;
import com.intellij.perlplugin.extensions.PerlCompletionContributor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by eli on 27-11-14.
 */
public class MainModuleComponent implements ModuleComponent {
    private final Module module;

    public MainModuleComponent(Module module) {
        this.module = module;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
        ModulesContainer.clear();
        PerlCompletionContributor.clear();
    }

    @NotNull
    public String getComponentName() {
        return "ModuleComponent";
    }

    public void projectOpened() {
        // called when project is opened
    }

    public void projectClosed() {
        // called when project is being closed
        ModulesContainer.clear();
    }

    public void moduleAdded() {
        // Invoked when the module corresponding to this component instance has been completely
        // loaded and added to the project.
        PerlInternalParser.start(module);
    }
}
