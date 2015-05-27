package com.intellij.perlplugin;

import com.intellij.perlplugin.filters.FileFilter;
import com.intellij.perlplugin.language.Constants;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eli on 27-11-14.
 */
public class Utils {
    public static boolean verbose = false;
    public static boolean debug = verbose || false;
    protected static OSType detectedOS;

    public enum OSType {
        Windows, MacOS, Linux, os, Other
    }

    ;

    public static void alert(String str) {
        System.out.println("ERROR: " + str);//TODO:: Log an exception properly
    }

    public static void print(Object obj) {
        System.out.println(obj);
    }

    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            Utils.alert(e.getMessage());
        }
        return "";
    }

    public static Matcher applyRegex(String regex, String content) {
        return applyRegex(regex, content, 0);
    }

    public static Matcher applyRegex(String regex, String content, int flags) {
        Pattern pattern = Pattern.compile(regex, flags);
        return pattern.matcher(content);
    }

    public static int getFilesCount(File file, FileFilter fileFilter) {
        File[] files = file.listFiles(fileFilter);
        int count = 0;
        for (File f : files)
            if (f.isDirectory())
                count += getFilesCount(f, fileFilter);
            else
                count++;

        return count;
    }

    public static boolean isValidateExtension(String path) {
        return path.endsWith(Constants.FILE_TYPE_PM) || path.endsWith(Constants.FILE_TYPE_PL);
    }

    public static OSType getOperatingSystemType() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                detectedOS = OSType.MacOS;
            } else if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else if (OS.indexOf("nux") >= 0) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}
