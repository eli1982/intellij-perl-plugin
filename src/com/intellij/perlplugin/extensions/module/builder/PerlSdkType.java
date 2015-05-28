package com.intellij.perlplugin.extensions.module.builder;

import com.intellij.openapi.projectRoots.*;
import com.intellij.perlplugin.PerlCli;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.language.PerlIcons;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/**
 * Created by ELI-HOME on 19-May-15.
 */
public class PerlSdkType extends SimpleJavaSdkType {

    @Override
    public String getPresentableName() {
        return "Perl Sdk";
    }

    @Override
    public Sdk createJdk(String jdkName, String home) {
        return super.createJdk(jdkName, home);
    }

    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator) {
        return super.createAdditionalDataConfigurable(sdkModel, sdkModificator);
    }

    @Override
    public void saveAdditionalData(SdkAdditionalData additionalData, Element additional) {
        super.saveAdditionalData(additionalData, additional);
    }

    @Override
    public String getBinPath(Sdk sdk) {
        return super.getBinPath(sdk);
    }

    @Override
    public String getToolsPath(Sdk sdk) {
        return super.getToolsPath(sdk);
    }

    @Override
    public String getVMExecutablePath(Sdk sdk) {
        return super.getVMExecutablePath(sdk);
    }

    @Override
    public String suggestHomePath() {
        if(Utils.getOperatingSystemType().equals(Utils.OSType.Linux)){
            return "/usr/";
        }
        return System.getenv("PERL_HOME");
    }

    @Override
    public boolean isValidSdkHome(String path) {
        if(path == null){
            return false;
        }
        File dir = new File(path);
        if(!dir.exists() || dir.isFile()){
            return false;
        }
        if(Utils.getOperatingSystemType().equals(Utils.OSType.Windows)) {
            return new File(path + "/bin/perl.exe").exists();
        }
        return new File(path + "/bin/perl").exists();
    }

    @Override
    public String suggestSdkName(String currentSdkName, String sdkHome) {
        return getVersionNumber(sdkHome);
//        return super.suggestSdkName(currentSdkName, sdkHome);
    }

    public static String getVersionNumber(String sdkHome) {
        return PerlCli.getVersionString(null);
    }

    @Nullable
    @Override
    public String getVersionString(Sdk sdk) {
        return getFullVersionString(sdk.getHomePath());
    }

    public static String getFullVersionString(String homePath) {
        return "perl version \"" + getVersionNumber(homePath) + "\"";
    }

    @Override
    public Icon getIcon() {
        return PerlIcons.LANGUAGE;
    }
}
