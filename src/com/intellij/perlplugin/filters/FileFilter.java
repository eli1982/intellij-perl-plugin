package com.intellij.perlplugin.filters;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by eli on 28-11-14.
 */
public class FileFilter implements FilenameFilter {
    @Override
    public boolean accept(File file, String s) {
        try {
            //    Utils.print("warning! excluding app");
            return /*!s.equals("app") && */!file.getCanonicalPath().contains(".git") &&
                    (s.endsWith(".pm") || s.endsWith(".pl") ||
                            new File(file.getCanonicalPath() + "/" + s).isDirectory()) && file.exists();
        } catch (IOException e) {
            return false;
        }
    }
}
