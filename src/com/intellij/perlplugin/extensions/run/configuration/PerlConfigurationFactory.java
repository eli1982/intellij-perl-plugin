package com.intellij.perlplugin.extensions.run.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.perlplugin.extensions.run.configuration.ui.PerlSettingsEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ELI-HOME on 22-May-15.
 */
public class PerlConfigurationFactory extends ConfigurationFactory {

    protected PerlConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
        return new RunConfigurationBase(project,this,"Perl RunConfiguration Template") {
            @NotNull
            @Override
            public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
                return new PerlSettingsEditor();
            }

            @Override
            public void checkConfiguration() throws RuntimeConfigurationException {
            }

            @Nullable
            @Override
            public RunProfileState getState(Executor executor, ExecutionEnvironment executionEnvironment) throws ExecutionException {
                return new PerlRunProfileState(executionEnvironment);
            }

        };
    }
}
