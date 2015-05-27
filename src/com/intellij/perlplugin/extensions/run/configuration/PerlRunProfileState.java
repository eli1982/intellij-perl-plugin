package com.intellij.perlplugin.extensions.run.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.newvfs.impl.VirtualFileImpl;
import com.intellij.perlplugin.PerlCli;
import com.intellij.perlplugin.Utils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by ELI-HOME on 22-May-15.
 */
public class PerlRunProfileState implements RunProfileState {
    private final Project project;
    private ExecutionEnvironment executionEnvironment;

    public PerlRunProfileState(ExecutionEnvironment executionEnvironment) {
        this.executionEnvironment = executionEnvironment;
        this.project = executionEnvironment.getProject();
        String file = "";
        Object vFile = executionEnvironment.getDataContext().getData("virtualFile");
        if(vFile != null && vFile instanceof VirtualFileImpl && ((VirtualFileImpl) vFile).getExtension().equals("pl")) {
            //get file path from context
            file = ((VirtualFileImpl) vFile).getPath();
        }else{
            //get file from selected text editor
            file = FileDocumentManager.getInstance().getFile(FileEditorManager.getInstance(executionEnvironment.getProject()).getSelectedTextEditor().getDocument()).toString();
        }
        if(file.endsWith("pl")) {
            PerlCli.runFile(executionEnvironment.getProject(), file);
        }
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, ProgramRunner programRunner) throws ExecutionException {
        try {
            String cmd =  PerlCli.getPerlPath(project);
            String[] params = {cmd ,"-e","'print 1;'"};

            Process p = Runtime.getRuntime().exec(params);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            String sdkVersion = "UNKNOWN";
            while ((line = input.readLine()) != null) {
                Utils.print(sdkVersion = line);
            }
            input.close();
            int result = p.waitFor();
            if (result != 0) {
                throw new Exception("Failed to get perl version - make sure PERL_HOME directs to the right folder");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;//TODO:: Implement ExecutionResult
    }
}
