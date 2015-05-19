package com.intellij.perlplugin.extensions.module.builder;

import com.intellij.icons.AllIcons.Nodes;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Condition;
import com.intellij.perlplugin.language.PerlIcons;
import com.intellij.psi.JavaPsiFacade;
import javax.swing.Icon;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

public class PerlModuleType extends ModuleType<PerlModuleBuilder> {
    public static final String MODULE_NAME = "Perl";
    public static final String PERL_GROUP = "Perl";
    public static final String BUILD_TOOLS_GROUP = "Build Tools";
    public static final String MOBILE_GROUP = "Mobile Perl";
    public static final String MODULE_TYPE = "PERL_MODULE";

    public static ModuleType getModuleType() {
        return ModuleTypeManager.getInstance().findByID(MODULE_TYPE);
    }

    public PerlModuleType() {
        this(MODULE_TYPE);
    }

    protected PerlModuleType(@NonNls String id) {
        super(id);
    }

    @NotNull
    public PerlModuleBuilder createModuleBuilder() {
        return new PerlModuleBuilder();
    }

    @NotNull
    public String getName() {
        String var10000 = MODULE_NAME;
        if(MODULE_NAME == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/intellij/perlplugin/extensions/module/builder/PerlModuleType", "getName"}));
        } else {
            return var10000;
        }
    }

    @NotNull
    public String getDescription() {
        return "Perl Module";
    }

    public Icon getBigIcon() {
        return PerlIcons.LANGUAGE_32;
    }

    public Icon getNodeIcon(boolean isOpened) {
        return PerlIcons.LANGUAGE;
    }

    @Nullable
    public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep, @NotNull final ModuleBuilder moduleBuilder) {
        return ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, moduleBuilder, new Condition() {
            @Override
            public boolean value(Object o) {
                return true;
            }

            public boolean value(SdkTypeId sdkType) {
                return moduleBuilder.isSuitableSdkType(sdkType);
            }
        });
    }

    public boolean isValidSdk(@NotNull Module module, Sdk projectSdk) {
        return true;//isValidJavaSdk(module);//TODO::handle perl sdk
    }

    public static boolean isValidJavaSdk(@NotNull Module module) {
        return true;// ModuleRootManager.getInstance(module).getSourceRoots(JavaModuleSourceRootTypes.SOURCES).isEmpty()?true:JavaPsiFacade.getInstance(module.getProject()).findClass("java.lang.Object", module.getModuleWithLibrariesScope()) != null;//TODO::handle as perl
    }
}