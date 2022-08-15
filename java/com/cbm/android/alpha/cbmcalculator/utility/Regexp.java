package com.cbm.android.alpha.cbmcalculator.utility;

import java.util.regex.Pattern;

public class Regexp {
    public static boolean matchNumber(String s) {
        if(Pattern.compile("[0-9]\\.*").matcher(s).matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean matchNumberAndSigns(String s) {
        if(Pattern.compile("[0-9\\+\\-\\.\\*\\/÷×e√]*").matcher(s).matches()) {
            return true;
        } else {
            return false;
        }
    }
}
