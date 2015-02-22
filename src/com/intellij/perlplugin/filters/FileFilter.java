package com.intellij.perlplugin.filters;

import com.intellij.perlplugin.Utils;
import com.intellij.perlplugin.language.Constants;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by eli on 28-11-14.
 */
public class FileFilter implements FilenameFilter {
    @Override
    public boolean accept(File file, String path) {
        try {
            return  (Utils.isValidateExtension(path) || (new File(file.getCanonicalPath() + "/" + path).isDirectory()) && file.exists());
        } catch (IOException e) {
            return false;
        }
    }
}
