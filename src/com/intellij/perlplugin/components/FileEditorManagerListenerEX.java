package com.intellij.perlplugin.components;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.ModulesContainer;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.extensions.PerlCompletionContributor;

/**
 * Created by ELI-HOME on 26-Feb-15.
 */
public class FileEditorManagerListenerEX implements FileEditorManagerListener {

    private final Project project;

    public FileEditorManagerListenerEX(Project project) {
        this.project = project;
    }

    @Override
    public void fileOpened(FileEditorManager source, final VirtualFile file) {
        ApplicationManager.getApplication().invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        while (!ModulesContainer.isInitialized()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (Utils.verbose) {
                            Utils.print("file opened:" + file.getPath());
                        }
                        PerlCompletionContributor.cacheSingleFile(project, file);
                    }
                }
        );
    }

    @Override
    public void fileClosed(FileEditorManager source, final VirtualFile file) {
        ApplicationManager.getApplication().invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.verbose) {
                            Utils.print("file closed:" + file.getPath());
                        }
                    }
                    //TODO:: clear relevant part of the cache
                }
        );
    }

    @Override
    public void selectionChanged(FileEditorManagerEvent event) {
        if (Utils.verbose) {
            Utils.print("file changed to:" + event.getNewFile());
        }
    }

}
