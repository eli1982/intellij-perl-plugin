package com.intellij.perlplugin.components;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.PerlInternalParser;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.extensions.PerlCompletionContributor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by eli on 27-11-14.
 */
public class MainModuleComponent implements com.intellij.openapi.module.ModuleComponent {
    private final Module module;

    public MainModuleComponent(Module module) {
        this.module = module;
    }

    public void initComponent() {
        PerlInternalParser.start(module.getProject());
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
    }

    public void moduleAdded() {
        // Invoked when the module corresponding to this component instance has been completely
        // loaded and added to the project.
    }
}
