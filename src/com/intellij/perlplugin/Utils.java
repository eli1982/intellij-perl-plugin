package com.intellij.perlplugin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eli on 27-11-14.
 */
public class Utils {
    public static boolean debug = true;

    public static void alert(String str) {
        throw new RuntimeException(str);
    }

    public static void print(Object obj) {
        System.out.println(obj);
    }

    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
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
}
