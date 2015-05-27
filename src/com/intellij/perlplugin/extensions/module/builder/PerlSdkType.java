package com.intellij.perlplugin.extensions.module.builder;

import com.intellij.openapi.projectRoots.*;
import com.intellij.perlplugin.PerlCli;
import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.language.PerlIcons;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by ELI-HOME on 19-May-15.
 */
public class PerlSdkType extends SimpleJavaSdkType {
    private static final String PERL_EXE_NAME = "perl";
    private static final String PERL_FLAG = "-e";
    private static final String PERL_CODE = "\"use Config;print $Config{version}\"";

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
            return "/usr/bin/";
        }
        return System.getenv("PERL_HOME");
    }

    @Override
    public boolean isValidSdkHome(String path) {
        boolean perl_run_file = new File(path + "/bin/perl.exe").exists();
        boolean perl_lib_folder = new File(path + "/lib").exists() && new File(path + "/lib").isDirectory();
        return perl_run_file && perl_lib_folder;
    }

    @Override
    public String suggestSdkName(String currentSdkName, String sdkHome) {
        return getVersionNumber(sdkHome);
//        return super.suggestSdkName(currentSdkName, sdkHome);
    }

    public static String getVersionNumber(String sdkHome) {
        try {
            String[] params = {PerlCli.getPerlPath(sdkHome), PERL_FLAG, PERL_CODE};

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
            return sdkVersion;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
