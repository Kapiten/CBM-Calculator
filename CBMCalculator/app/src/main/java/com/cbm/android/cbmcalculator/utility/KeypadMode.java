package com.cbm.android.cbmcalculator.utility;

import com.cbm.android.cbmcalculator.ASelf;
import java.util.ArrayList;

public class KeypadMode {
    ASelf.KeypadModeOpt mKeypadMode = ASelf.KeypadModeOpt.Numbers; 
    
    private ArrayList<String> arrPadModeBtns(){
        ArrayList<String> arrA = new ArrayList<>();
        int a = 0;
        while(a<10) {arrA.add(a+"");}
        
        return arrA;
    }
    
    
}
