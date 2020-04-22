/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ar.aimark.server.support.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author root
 */
public class StrUtils {
    
    
    private static final String EMPTY = "";
    private static final String FILE_SEPARATOR = loadFileSeparator();
    
    
    public static boolean findNothingIn(String str) {
        return null == str || EMPTY.equals(str);
    }
    
    
    
    public static boolean matchRegexp(String value, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    
    
    
    
    
    
    
    
    private static String loadFileSeparator() {
        String separator = "/";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) {
            separator = "\\";
        }
        return separator;
    }
    
    
    
}
