package com.intellij.perlplugin;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
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
//        Utils.print("parsing started");
//        long start = System.currentTimeMillis();
//          PerlInternalParser.parseAllSources(module);
//        long end = System.currentTimeMillis();
//        Utils.print("update completed in " + ((end - start) / 1000) + "sec");

    }

    public void disposeComponent() {
        ModulesContainer.clear();
    }

    @NotNull
    public String getComponentName() {
        return "MainModuleComponent";
    }

    public void projectOpened() {
        // called when project is opened
    }

    public void projectClosed() {
        // called when project is being closed
    }

    public void moduleAdded() {
        // Invoked when the module corresponding to this component instance has been completely
        // loaded and added to the project.
    }
}
