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
            if (Utils.debug) {
                Utils.print("running: perl " + "\"" + path + "\"");
            }
            String cmd = getPerlPath(project);
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
        if (project == null) {
            Utils.alert("Missing project, cannot execute code");
            return null;
        }
        try {
            String cmd = getPerlPath(project);
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
        if (sdk == null) {
            return null;
        }
        return sdk.getHomePath();
    }

    /**
     * @param project
     * @return perl's executable file full path
     */
    public static String getPerlPath(Project project) {
        String path = null;
        if (project != null) {
            Sdk sdk = ProjectRootManagerImpl.getInstance(project).getProjectSdk();
            if (sdk != null) {
                return getPerlPath(sdk.getHomePath());
            }
        }
        return path;
    }

    public static String getPerlPath(String sdkHome) {
        String path = null;
        Utils.OSType os = Utils.getOperatingSystemType();
        if (sdkHome == null) {
            switch (os) {
                case Linux:
                    path = "/usr/";
                case Windows:
                case MacOS:
                case Other:
                    path = System.getenv("PERL_HOME");
            }
        } else {
            path = sdkHome;
        }
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }
        return path + "/bin/perl";
    }
}
