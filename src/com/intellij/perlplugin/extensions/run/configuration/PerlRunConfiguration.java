package com.intellij.perlplugin.extensions.run.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.perlplugin.PerlInternalParser;
import com.intellij.perlplugin.extensions.run.configuration.ui.PerlSettingsEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

/**
 * Created by ELI-HOME on 22-May-15.
 */
public class PerlRunConfiguration extends ModuleBasedConfiguration {

    public PerlRunConfiguration(String name, RunConfigurationModule configurationModule, ConfigurationFactory factory) {
        super(name, configurationModule, factory);
    }

    public PerlRunConfiguration(RunConfigurationModule configurationModule, ConfigurationFactory factory) {
        super(configurationModule, factory);
    }

    @Override
    public Collection<Module> getValidModules() {
        Collection perlModules = this.getAllModules();
        ListIterator<Module> allModules = new ArrayList<Module>().listIterator();
        while (allModules.hasNext()) {
            Module module = allModules.next();
            if (PerlInternalParser.isValidModuleType(module)) {
                perlModules.add(module);
            }
        }
        return perlModules;
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new PerlSettingsEditor();//TODO:: implement
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return null;//TODO:: implement
    }
}
