package com.intellij.perlplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.impl.ProjectRootManagerImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by ELI-HOME on 24-May-15.
 * Perl CLI Util - please put all CLI controls here
 */
public class PerlCli {

    public static String runFile(final Project project, String filePath) {

        String result = "";
        try {
            String path = new File(filePath).getPath().replaceFirst("file:\\\\", "");
            if(Utils.debug) {
                Utils.print("running: perl " + "\"" + path + "\"");
            }
            String cmd = ((project == null) ? System.getenv("PERL_HOME") : getSdkHome(project)) + "\\bin\\perl";
            String[] params = {cmd, "\"" + path + "\""};

            Process p = Runtime.getRuntime().exec(params);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                Utils.print(result += line);
            }
            input.close();
            int resultCode = p.waitFor();
            if (resultCode != 0) {
                throw new Exception("Failed to run perl - Code (" + resultCode + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String executeCode(Project project, String code) {
        String result = "";
        try {
            String cmd = ((project == null) ? System.getenv("PERL_HOME") : getSdkHome(project)) + "\\bin\\perl";
            String[] params = {cmd, "-e", code};

            Process p = Runtime.getRuntime().exec(params);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                Utils.print(result += line);
            }
            input.close();
            int resultCode = p.waitFor();
            if (resultCode != 0) {
                throw new Exception("Failed to run perl - Code (" + resultCode + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //"perl version \"" + getVersionNumber(homePath) + "\""
    public static String getVersionString(Project project) {
        return executeCode(project, "\"use Config;print $Config{version}\"");
    }

    public static void main(String[] args) {
        PerlCli.getVersionString(null);
    }

    private static String getSdkHome(Project project) {
        Sdk sdk = ProjectRootManagerImpl.getInstance(project).getProjectSdk();
        if(sdk == null){
            return System.getenv("PERL_HOME");
        }
        return sdk.getHomePath();
    }
}
