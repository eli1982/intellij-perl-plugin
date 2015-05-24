package com.intellij.perlplugin.extensions.run.configuration;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.perlplugin.language.Constants;
import com.intellij.perlplugin.language.PerlIcons;

/**
 * Created by ELI-HOME on 20-May-15.
 * configuration type
 */
public class PerlConfigurationType extends ConfigurationTypeBase {

    protected PerlConfigurationType() {
        super(Constants.ID, Constants.LANGUAGE_NAME, Constants.DESCRIPTION, PerlIcons.LANGUAGE);
        addFactory(new PerlConfigurationFactory(this));
    }

}
