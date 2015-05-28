package com.intellij.perlplugin;

import com.intellij.execution.console.ConsoleExecuteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.impl.ProjectRootManagerImpl;
import org.jetbrains.builtInWebServer.ConsoleManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ELI-HOME on 24-May-15.
 * Perl CLI Util - please put all CLI controls here
 */
public class PerlCli {
    public static final String CODE_FLAG = "-e";

    private static Utils.OSType os = null;
    static {
        os = Utils.getOperatingSystemType();
    }

    public static String runFile(final Project project, String filePath) {
        String result = "";
        try {
            String path = new File(filePath).getPath().replaceFirst("file:\\\\", "");
            if (Utils.debug) {
                Utils.print("running: perl " + "\"" + path + "\"");
            }
            String cmd = ((project == null) ? getPerlPath("") : getPerlPath(project));;
            String[] params = {cmd, ((os.equals(os.Windows))? "\"" + path + "\"" : path)};

            Process p = Runtime.getRuntime().exec(params);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            result = printStream( input);
            printStream( err);
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
            String cmd = ((project == null) ? getPerlPath("") : getPerlPath(project));
            String[] params = {cmd, CODE_FLAG, code};

            if(cmd == null){
                throw new Exception("can't find perl");
            }
            Process p = Runtime.getRuntime().exec(params);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            result = printStream(input);
            printStream(err);
            int resultCode = p.waitFor();
            if (resultCode != 0) {
                throw new Exception("Failed to run perl - Code (" + resultCode + ")");
            }
        } catch (Exception e) {
            Utils.alert(e.getMessage());
        }
        return result;
    }

    private static String printStream(BufferedReader input) throws IOException {
        String result = "";
        String line;
        while ((line = input.readLine()) != null) {
            Utils.print(result += line);
        }
        input.close();
        return result;
    }

    public static String getVersionString(Project project) {
        if(os.equals(os.Windows)) {
            return executeCode(project, "\"use Config;print Config{version};\"");
        }
        return executeCode(project, "use Config;print $Config{version};");
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
            }else{
                getPerlPath("");
            }
        }

        return path;
    }

    public static String getPerlPath(String sdkHome) {
        String path = null;
        if (sdkHome == null || sdkHome.isEmpty()) {
            switch (os) {
                case Linux:
                    path = "/usr/";
                    break;
                case Windows:
                case MacOS:
                case Other:
                    path = System.getenv("PERL_HOME");
            }
        } else {
            path = sdkHome;
        }
        if(path == null){
            return "";//TODO::Handle Exception
        }
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }
        return path + "bin/perl";
    }
}
