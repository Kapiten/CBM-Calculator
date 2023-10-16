package com.cbm.android.cbmcalculator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.cbmcalculator.R;
import com.cbm.android.cbmcalculator.extended.*;
import com.cbm.android.cbmcalculator.utility.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ASelf {
    private static ASelf aSelf;
    //private static Context mContext;
    public String cTxt="", calcNm="#1 Calculation"; //Current Light And Sound Description
    public static String setting="#FF000000", stitle="ColorBackground";
    public static class Constants {
        public static String DEFAULT_JSONCOLOR_BLKTXT = "{'A':'1.0', 'R':'0.0', 'B':'0.0', 'G':'0.0'}";
        public static String DEFAULT_JSONCOLOR_WHTTXT = "{'A':'1.0', 'R':'1.0', 'B':'1.0', 'G':'1.0'}";
        public static String DEFAULT_JSONCOLOR_ID="ID";
        public static String DEFAULT_JSONCOLOR_BGCOLOR="BGCOLOR";
        public static String DEFAULT_JSONCOLOR_BGTYPE="BGTYPE";
        public static String DEFAULT_JSONCOLOR_TXTCOLOR="TXTCOLOR";
        public static String SOLID="SOLID";
        public static String BORDER="BORDER";
    }
    
    public boolean isDotd = false, standBy = false, hcb=false;
    private SharedPreferences sp;
    private SharedPreferences.Editor sped;
    
    public ArrayList<String> arr = new ArrayList<>();
    public ArrayList<JSONObject> arrS = new ArrayList<>();
    public String mDoneCalculation, carriedAnswer="";
    public int tvCInd=0, historyCount=0, sbEmpty = 0, expCount=0, expAltInd=0;
    public static boolean decimalAlways, bOpts, phOn, carryAnswer;
    public static DecimalFormat mDF;
    public KeypadModeOpt mKeypadMode = KeypadModeOpt.Numbers;
    
    public static String[] clrStates = new String[] {
        "ColorBackground",
        "ColorAnswer",
        "ColorCalculation_Details",
        "ColorNumbers",
        "ColorSymbols",
        "ColorDefault_Theme"
    };
    
    public static String[] settings;
    
    public ArrayList<View> getSettingViews(String setting) {
        ArrayList<View> arrV = new ArrayList<>();
        if(setting.equals(settings[0])) {
            
        }
        
        return arrV;
    }
    
    public enum KeypadModeOpt {
        Numbers,
        Selection
    };
    
    public static ArrayList<Integer> arrC() {
        ArrayList<Integer> arrC = new ArrayList<>();
        arrC.add(R.drawable.bg_white);
        arrC.add(R.drawable.bg_black);
        arrC.add(R.drawable.bg_grey);
        arrC.add(R.drawable.bg_red);
        arrC.add(R.drawable.bg_yellow);
        arrC.add(R.drawable.bg_green);
        arrC.add(R.drawable.bg_accent);
        arrC.add(R.drawable.bg_violet);
        arrC.add(R.drawable.bg_white_ss);
        arrC.add(R.drawable.bg_black_ss);
        arrC.add(R.drawable.bg_grey_ss);
        arrC.add(R.drawable.bg_red_ss);
        arrC.add(R.drawable.bg_yellow_ss);
        arrC.add(R.drawable.bg_green_ss);
        arrC.add(R.drawable.bg_accent_ss);
        arrC.add(R.drawable.bg_violet_ss);
        arrC.add(R.drawable.bg_white_s);
        arrC.add(R.drawable.bg_black_s);
        arrC.add(R.drawable.bg_grey_s);
        arrC.add(R.drawable.bg_red_s);
        arrC.add(R.drawable.bg_yellow_s);
        arrC.add(R.drawable.bg_green_s);
        arrC.add(R.drawable.bg_accent_s);
        arrC.add(R.drawable.bg_violet_s);
        arrC.add(R.drawable.bg_white_r);
        arrC.add(R.drawable.bg_black_r);
        arrC.add(R.drawable.bg_grey_r);
        arrC.add(R.drawable.bg_red_r);
        arrC.add(R.drawable.bg_yellow_r);
        arrC.add(R.drawable.bg_green_r);
        arrC.add(R.drawable.bg_accent_r);
        arrC.add(R.drawable.bg_violet_r);
        arrC.add(R.drawable.bg_white_sr);
        arrC.add(R.drawable.bg_black_sr);
        arrC.add(R.drawable.bg_grey_sr);
        arrC.add(R.drawable.bg_red_sr);
        arrC.add(R.drawable.bg_yellow_sr);
        arrC.add(R.drawable.bg_green_sr);
        arrC.add(R.drawable.bg_accent_sr);
        arrC.add(R.drawable.bg_violet_sr);
        return arrC;
    }
    
    public static ArrayList<Integer> arrClr() {
        ArrayList<Integer> arrC = new ArrayList<>();
        arrC.add(Color.argb(1f,1f,1f,1f));
        arrC.add(Color.argb(1f,0,0,0));
        arrC.add(Color.argb(1f,0.5f,0.5f,0.5f));
        arrC.add(Color.argb(1f,1f,0,0));
        arrC.add(Color.argb(1f,1f,1f,0));
        arrC.add(Color.argb(1f,0,1f,0));
        arrC.add(Color.argb(1f,0,0,1f));
        arrC.add(Color.argb(1f,1f,0,1f));
        return arrC;
    }
    
    public static ArrayList<String> strSigns() {
        ArrayList<String> arrSigns = new ArrayList<>();
        //arrSigns.add(Arrays.asList(new String[]{"+", "-", "*", "/"});
        arrSigns.add("+");
        arrSigns.add("-");
        arrSigns.add("×");
        arrSigns.add("÷");
        arrSigns.add("e");
        arrSigns.add("√");
        //arrSigns.add("cos");
        //arrSigns.add("sin");
        //arrSigns.add("tan");
        
        return arrSigns;
    }
    
    public enum Calculation {
        Ix,
        LmtIx,
        Exp,
        ExpAns,
        DoneCalc,
        dType
    };
    
    private ASelf() {
        new ASelf(null);
    }
    
    private ASelf(Context c) {
        //mContext=c;
        isDotd = false;
        cTxt="";
        mDF = new DecimalFormat("#.#");
        //sp = getCalcSet(c);
        //sped = sp.edit();
    }
    
    /*public static ASelf get() {
        if(aSelf==null) {
            aSelf=new ASelf(null);
        }
        
        return aSelf;
    }*/
    
    public static ASelf get(Context c) {
        if(aSelf==null) {
            aSelf=new ASelf(c);
        }
        
        if(c!=null) {
            //setDecimalAlways(getCalcSet(c).getString(mContext.getResources().getString(R.string.decimal_always_switch,"Switch_Decimal_Always"),"Switch_Decimal_Always").equals("On"));
            updateSettings(c);
            //refresh(c);
        }
        return aSelf;
    }
    
    public static SharedPreferences getCalcHistory(Context c) {
        return c.getSharedPreferences("CalcPrefsFile", Context.MODE_PRIVATE);
    }
    
    public static SharedPreferences getCalcSet(Context c) {
        return c.getSharedPreferences("CBMCalcSettings", Context.MODE_PRIVATE);
    }
    
    public void onSave(Context c, int a) {
        try {
            if(arr.size()>0) {
                SharedPreferences.Editor edh = getCalcHistory(c).edit();
                SharedPreferences.Editor ed = getCalcSet(c).edit();
                
                edh.putString("jsonCalc"+a, Ut.toJSON(arr));
                ed.putInt("hc", a);
                ed.putBoolean("standBy", isStandBy());
                //ed.putBoolean("modeScr", bOpts);
                
                edh.apply();
                ed.apply();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    public void onSaved(Context c, int a, View... vs) {
        try {
            clearEmpties(c);
            SharedPreferences sp = getCalcHistory(c);
            if(getHC(c)>0) {
                if(a>=0) {
                //clearC();
                setStandBy(false);
                //tvDoneCalculation.setText(sp.getString("tvDoneCalculation"+a, ""));
                List<String> l = (List<String>)Ut.toArray(sp.getString("jsonCalc"+a, ""));
                //Toast.makeText(c, "JsonC"+a+", "+l.toString(), Toast.LENGTH_SHORT).show();
                for(int x = 0; x<l.size(); x++) {
                    String str = l.get(x).toString();
                    if(!str.isEmpty()){entry(c, str);}
                        
                }
            } else {
                    getTheSaved(c, vs[0]/*llCalculation*/, vs[1]/*tvDoneCalculation*/);
                }
            }
            //setHCB(false);
               
        } catch(Exception ex) {
            ex.printStackTrace();
            Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
  
    public static void refresh(Context c) {
        try {
        SharedPreferences sp = getCalcSet(c);
        SharedPreferences.Editor ed = sp.edit();
        for(String s: settings) {
            int a = s.substring(0, s.indexOf("_")).equals("Color")?1:0;
            if(!sp.contains(s)){
                if(a==1){ed.putString(s,DefaultThemeSet.defaultBGFormats().get(0).getString(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR));
                }else{ed.putString(s,"Off");}
            }else	if(sp.contains(s)&&(sp.getInt(s,-1)<=0)){
                ed.putString(s,DefaultThemeSet.defaultBGFormats().get(0).toString());
            }
        }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setProperty(Context c, String s, String p) {
        SharedPreferences.Editor ed = getCalcSet(c).edit();
        ed.putString(s,p);
        ed.apply();
    }
    
    public String getProperty(Context c, String s) {
        return getCalcSet(c).getString(s,s);
    }
    
    public void addListItem(Object ol, Object o) {
        try {
            if(ol instanceof List) {
                ((List)ol).add(o);
            } else if(o instanceof JSONObject) {
                JSONObject jo = ((JSONObject)ol);
                ((List)ol).add(o);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void allTypeCalculations() {
        try {
        aSelf.arrS.add(new JSONObject().put("CalculationName","BODMAS")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"÷","×","+","-"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOMDAS")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"×","÷","+","-"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BODMSA")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"÷","×","-","+"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOMDSA")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"×","÷","-","+"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOASMD")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"+","-","×","÷"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOASDM")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"+","-","÷","×"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOSAMD")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"-","+","×","÷"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOSADM")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"-","+","÷","×"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOMADS")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"×","+","÷","-"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOAMDS")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"+","×","÷","-"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOMASD")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"×","+","-","÷"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOAMSD")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"+","×","-","+"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BODSMA")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"÷","-","×","+"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BODSAM")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"÷","-","+","×"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOSDMA")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"-","÷","×","+"})));
        aSelf.arrS.add(new JSONObject().put("CalculationName","BOSDAM")
            .put("Calculation",Ut.toCalculationType(arr, new String[]{"-","÷","+","×"})));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setEntryNumberColors(Context c, int bgColor, int tColor) {
//        switch(bgColor) {
//            case R.drawable.bg_white: bgColor=R.drawable.bg_white_green; break;
//            case R.drawable.bg_white_s: bgColor=R.drawable.bg_white_green_s; break;
//            case R.drawable.bg_white_r: bgColor=R.drawable.bg_white_green_r; break;
//            case R.drawable.bg_white_sr: bgColor=R.drawable.bg_white_green_sr; break;
//            case R.drawable.bg_white_ss: bgColor=R.drawable.bg_white_green_ss; break;
//
//        }
        
        LinearLayout llEntry = ((MainActivity)c).llEntry;
        for(int a = 0; a < llEntry.getChildCount(); a++) {
            LinearLayout ll = ((LinearLayout)llEntry.getChildAt(a));
            
            for(int b = 0; b < ll.getChildCount(); b++) {
                if((a==3&&b==0)||(a==3&&b==2)) continue;
                 ((Button)ll.getChildAt(b)).setBackgroundResource(bgColor);
                 ((Button)ll.getChildAt(b)).setTextColor(tColor);
            }
        }
    }
    
    public void setEntryNumberColors(Context c, ShapeDrawable sdColor, int tColor) {
        LinearLayout llEntry = ((MainActivity)c).llEntry;
        for(int a = 0; a < llEntry.getChildCount(); a++) {
            LinearLayout ll = ((LinearLayout)llEntry.getChildAt(a));
            
            for(int b = 0; b < ll.getChildCount(); b++) {
                if((a==3&&b==0)||(a==3&&b==2)) continue;
                 ((Button)ll.getChildAt(b)).setBackground(sdColor);
                 ((Button)ll.getChildAt(b)).setTextColor(tColor);
            }
        }
        
    }
    
    public void setSymbolColors(Context c, ShapeDrawable sdColor, int tColor) {
        LinearLayout llEntry = ((MainActivity)c).llEntry;
        LinearLayout llToolbar = ((MainActivity)c).llToolbar;
        
        ((LinearLayout)llEntry.getChildAt(0)).getChildAt(3).setBackground(sdColor);
        ((Button)((LinearLayout)llEntry.getChildAt(0)).getChildAt(3)).setTextColor(tColor);
        ((LinearLayout)llEntry.getChildAt(1)).getChildAt(3).setBackground(sdColor);
        ((Button)((LinearLayout)llEntry.getChildAt(1)).getChildAt(3)).setTextColor(tColor);
        ((LinearLayout)llEntry.getChildAt(2)).getChildAt(3).setBackground(sdColor);
        ((Button)((LinearLayout)llEntry.getChildAt(2)).getChildAt(3)).setTextColor(tColor);
        ((LinearLayout)llEntry.getChildAt(3)).getChildAt(3).setBackground(sdColor);
        ((Button)((LinearLayout)llEntry.getChildAt(3)).getChildAt(3)).setTextColor(tColor);
        ((LinearLayout)llEntry.getChildAt(3)).getChildAt(0).setBackground(sdColor);
        ((Button)((LinearLayout)llEntry.getChildAt(3)).getChildAt(0)).setTextColor(tColor);
        for(int a = 0; a < llToolbar.getChildCount(); a++) {
            llToolbar.getChildAt(a).setBackground(sdColor);
            if(llToolbar.getChildAt(a) instanceof Button)
            {((Button)llToolbar.getChildAt(a)).setTextColor(tColor);}
        
        }
        
    }
    
    public void setSymbolColors(Context c, int bgColor, int tColor) {
//        switch(bgColor) {
//            case R.drawable.bg_black: bgColor=R.drawable.bg_black_blue; break;
//            case R.drawable.bg_black_s: bgColor=R.drawable.bg_black_blue_s; break;
//            case R.drawable.bg_black_r: bgColor=R.drawable.bg_black_blue_r; break;
//            case R.drawable.bg_black_sr: bgColor=R.drawable.bg_black_blue_sr; break;
//            case R.drawable.bg_black_ss: bgColor=R.drawable.bg_black_blue_ss; break;
//        }
        
        LinearLayout llEntry = ((MainActivity)c).llEntry;
        LinearLayout llToolbar = ((MainActivity)c).llToolbar;
        
        ((LinearLayout)llEntry.getChildAt(0)).getChildAt(3).setBackgroundResource(bgColor);
        ((Button)((LinearLayout)llEntry.getChildAt(0)).getChildAt(3)).setTextColor(tColor);
        ((LinearLayout)llEntry.getChildAt(1)).getChildAt(3).setBackgroundResource(bgColor);
        ((Button)((LinearLayout)llEntry.getChildAt(1)).getChildAt(3)).setTextColor(tColor);
        ((LinearLayout)llEntry.getChildAt(2)).getChildAt(3).setBackgroundResource(bgColor);
        ((Button)((LinearLayout)llEntry.getChildAt(2)).getChildAt(3)).setTextColor(tColor);
        ((LinearLayout)llEntry.getChildAt(3)).getChildAt(3).setBackgroundResource(bgColor);
        ((Button)((LinearLayout)llEntry.getChildAt(3)).getChildAt(3)).setTextColor(tColor);
        ((LinearLayout)llEntry.getChildAt(3)).getChildAt(0).setBackgroundResource(bgColor);
        ((Button)((LinearLayout)llEntry.getChildAt(3)).getChildAt(0)).setTextColor(tColor);
        for(int a = 0; a < llToolbar.getChildCount(); a++) {
            llToolbar.getChildAt(a).setBackgroundResource(bgColor);
            if(llToolbar.getChildAt(a) instanceof Button)
            ((Button)llToolbar.getChildAt(a)).setTextColor(tColor);
        
        }
        
    }
    
    public static void clearEmpties(Context c) {
        SharedPreferences.Editor ed = ASelf.getCalcSet(c).edit();
            for(int b = 0; b<ASelf.getCalcSet(c).getAll().size(); b++) {
            JSONObject jo=null;
            try{
                String hl = ASelf.getCalcSet(c).getString("jsonCalc"+b, "");
                jo= new JSONObject(hl);
                //Toast.makeText(c,jo.toString(), Toast.LENGTH_SHORT).show();
                if(!hl.isEmpty()||jo==null||jo.names().length()<0) {
                    ed.remove("jsonCalc"+b);
                    ed.apply();
                }
            }
            catch(Exception ex){ex.printStackTrace();ed.remove("jsonCalc"+b);
            }
        }
    }
    
    public static boolean setCBM(Context c, String s, Object o) {
        SharedPreferences.Editor ed = getCalcSet(c).edit();
        boolean bln = true;
        if(o instanceof Boolean) {
            ed.putString(s, Boolean.valueOf(o.toString())?"On":"Off");
        } else if(o instanceof Integer) {
            ed.putInt(s, Integer.parseInt(o.toString()));
        } else if(o instanceof String) {
            ed.putString(s, o.toString());
        } else {
            bln=false;
        }
        
        ed.apply();
        
        return bln;
    }
    
    public static void setDecimalAlways(Context c, boolean b) {
        decimalAlways=b;
        //setCBM(mContext.getResources().getString(R.string.decimal_always_switch,"Switch_Decimal_Always"), "On");
    }
    
    public static boolean getDecimalAlways(Context c) {
        return getCalcSet(c).getString("Switch_Decimal_Always", "Switch_Decimal_Always").equals("On");
    }
    
    public static void updateSettings(Context c) {
        if(settings==null) {
            settings = c.getResources().getStringArray(R.array.app_settings);
        }
    }
    
    public String editNowValue(String value, boolean clear) {
        if(clear) {
            cTxt = ""; isDotd=false;
        }
        
        return editNowValue(value);
    }
    
    public String editNowValue(String value) {
        String str1 = "";
        if(cTxt.isEmpty()) {
            if(value.equals(".")) {
                value = "0.";
            } else {
                cTxt += value;
            }
        } else {
            if(cTxt.equals("0")) {
                cTxt = value.equals(".")?cTxt+value:value;
            } else {
                cTxt += value;
            }
        }
        
        if(!cTxt.isEmpty()&&cTxt.contains(".")) {
            //cTxt = Double.parseDouble(cTxt)+"";
            isDotd = true;
        } else {
            //ASelf.get().cTxt = cTxt;
            isDotd = false;
        }
        
        return cTxt;
    }
    
    public void	entry(Context c, String str) {
        entry(c, str, new Object[]{((MainActivity)c).tvCurrent, ((MainActivity)c).llNC, ((MainActivity)c).btnPercentage, ((MainActivity)c).btnSignChange, ((MainActivity)c).hsvNC});
    }
    
    public void	entry(Context c, String str, Object[] os) {
        phOn=false;
        if(((TextView)os[0]).getText().toString().isEmpty()||isNumber(((TextView)os[0]).getText().toString())) {
        if(!isStandBy()&&((TextView)os[0])!=null) {
        tvCInd = Integer.parseInt(((TextView)os[0]).getTag().toString());
    			    if(str.equals("*")){str="×";}
            else if(str.equals("/")){str="÷";}
            //if(str.equals(".")){btnPoint.callOnClick(); return;}
            if(arr!=null&&((LinearLayout)os[1])!=null) {
            if(isNumber(str)||str.equals(".")) {
                if(arr.size()<=Integer.parseInt(((TextView)os[0]).getTag().toString())) {
                    arr.add(str);
                    if(arr.size()>((LinearLayout)os[1]).getChildCount()){
                                CurrentComponents.addCurrentTV(c,"");}
                        ((TextView)os[0]).setText(editNowValue(str, true));
                } else {
                    arr.set(tvCInd, editNowValue(str));
                    ((TextView)os[0]).setText(arr.get(tvCInd));
                }
                ((Button)os[2]).setEnabled(true);
            } else {
                if(arr.size()-1>=0) {
                    if (isNumber(arr.get(arr.size() - 1))) {
                        if (str.equals("%")) {
                            if(arr.size()>1&&(arr.contains("/")||arr.contains("÷"))) {
                                arr.add("×");
                                arr.add("100");
                                CurrentComponents.addCurrentTV(c, "×", 2);
                                CurrentComponents.addCurrentTV(c, "100");
                            } else {
                                arr.add("×");
                                arr.add("0.01");
                                CurrentComponents.addCurrentTV(c, "×", 2);
                                CurrentComponents.addCurrentTV(c, "0.01");
                            }
                        } else {
                            if (tvCInd <= arr.size()) arr.add(tvCInd + 1, str);
                            else arr.add(str);
                            //arr.add(str);
                            CurrentComponents.addCurrentTV(c, tvCInd + 1, str, 2);
                        }
                        if (!Ut.measureSymbol(str)) {
                            CurrentComponents.addCurrentTV(c, tvCInd + 2, "", 0);
                            arr.add(tvCInd + 2, "");
                        }
                        indexify(c);
                    } else {
                        String s = arr.get(arr.size() - 1);
                        if (s.equals("%")) {
                            arr.add(str);
                            CurrentComponents.addCurrentTV(c, str, 2);
                        } else {
                            entry(c, "0", os);
                            entry(c, str, os);
                        }
                    }
                } else {
                    entry(c, "0", os);
                    entry(c, str, os);
                }
            }
        }
        try{
                if(!isNumber(str)){((HorizontalScrollView)os[4]).smoothScrollTo(((HorizontalScrollView)os[4]).getMaxScrollAmount(), 0);}else {((HorizontalScrollView)os[4]).smoothScrollTo(Math.round(((TextView)os[0]).getX()), 0);}} catch(Exception ex) {ex.printStackTrace();
                Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        
        if(arr.size()>0)((Button)os[3]).setEnabled(true);
        else ((Button)os[3]).setEnabled(false);
        }
        arrAct(c);
    }
    
    public void indexify(Context c) {
        for(int x = 0; x < ((MainActivity)c).llNC.getChildCount(); x++) {
            ((MainActivity)c).llNC.getChildAt(x).setTag(x);
        }
    }
    
    public void arrAct(Context c) {
        try {
            if(arr.size()>0) {
        BigDecimal ans = isNumber(aSelf.arr.get(0))?new BigDecimal(aSelf.arr.get(0)):new BigDecimal(0);
                BigDecimal ans2 = new BigDecimal(0);
                 String currentSign="", FA="", answ, answ2, answ3; 
        
        for(int x = 0; x < aSelf.arr.size(); x++) {
                        ans2 = ans;
                        if(!ASelf.isNumber(aSelf.arr.get(x))) {
                            currentSign = aSelf.arr.get(x);
                            //ans = ans + Integer.parseInt(aSelf.arr.get(x+1));
                        } else {
                            BigDecimal anso = new BigDecimal(0);
                            if(x>1)anso = new BigDecimal(aSelf.arr.get(x));
                            ans = ASelf.calculate(currentSign, ans, anso);
                            
            }}
        //FA=mDF.format(Double.parseDouble(ans+""));
                            //if(ans>=0||ans<=0) {
                                answ = (ans2+"").endsWith(".0")?Integer.parseInt((ans2+"").substring(0, (ans2+"").indexOf(".")))+"":(ans2+"");
                                answ2 = (ans+"").contains(".")?((ans+"").endsWith(".0")?Integer.parseInt((ans+"").replace(".0",""))+"":(ans+"")):(ans+"");
                                if(getCalcSet(c).getString(c.getResources().getString(R.string.decimal_always_switch), "Switch_Numbers_+Decimal_Always").equals("On")) {
                                    answ = mDF.format(Double.parseDouble(ans2+""));
                                    answ2 = mDF.format(Double.parseDouble(ans+""));
                                }
                            //}
            ((MainActivity)c).tvDoneCalculation.setText(answ2 + " | Write an expression");
        }} catch (Exception ex) {ex.printStackTrace();}
    }

    public static double calculate(String s, double ans, double ans2) {
        return calculate(s, new BigDecimal(ans), new BigDecimal(ans2)).doubleValue();
    }
    public static BigDecimal calculate(String s, BigDecimal ans, BigDecimal ans2) {
        switch(s) {
            case "+":
                ans = ans.add(ans2);
                break;
            case "-":
                ans = ans.subtract(ans2);
                break;
            case "×":
                ans = ans.multiply(ans2);
                break;
            case "÷":
                ans = ans.divide(ans2);
                break;
            case "%":
                ans = ans.multiply(new BigDecimal(0.01));
                break;
            case "√":
                ans = ans2.pow(ans.intValue());//Math.pow(ans2, Double.parseDouble((1.0 / ans) + ""));
                //ans = (ans + "").contains(".9999999999") ? ans.round(MathContext.UNLIMITED)/*Math.round(ans)*/ : ans;
                break;
            case "e":
                ans = ans.pow(ans2.intValue());
                break;
            default:
                ans = ans.add(ans2);
                break;
        }
                         
        return ans;
    }
    
    public void getTheSaved(final Context c, final View... vs) {
        new AsyncTask<String, String, String>() {
            ProgressDialog pd = new ProgressDialog(c);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd.show();
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    SharedPreferences sph = ASelf.getCalcHistory(c);
                    int ssz = Integer.parseInt(ASelf.getCalcSet(c).getString(c.getResources().getString(R.string.history_limit_textn),"10"));
                    int sz = Math.min(ssz, sph.getAll().size());
                    //sz=ssz<=sph.getAll().size()?ssz:sph.getAll().size();
                    //int b = -1;
                    //Toast.makeText(c, "Limit: " +sz+" & All Size: " +sph.getAll().size(), Toast.LENGTH_SHORT).show();
                    if(sz>0) {
                        for(int b = 0; b<sz; b++) {
                            //while(((LinearLayout)vs[0]).getChildCount()!=sz) {
                            final int bb = b;
                            String str = "";
                            //String ttl = sph.getAll().size()>=1?(sph.getAll().size()-bb)+"":"1";
                            String ttl = (sph.getAll().size()-bb)+"";
                            String[] answer = new String[2];
                            List<String> l = (List<String>)Ut.toArray(sph.getString("jsonCalc"+(sph.getAll().size()-bb), ""));
                            //if(bb==-1){ttl="1";l=(List<String>)Ut.toArray(sph.getString("jsonCalcA"+1, ""));}
                            //Toast.makeText((MainActivity)c, "List: " +l+" & All Size: " +l.toArray(new String[sph.getAll().size()]), Toast.LENGTH_SHORT).show();
                            if(l!=null&&l.size()>0) {
                                answer = ASelf.calculation(l);
                            }
                            try{
                                if(!answer[0].isEmpty()) {
                                    publishProgress(ttl, String.valueOf(sz - bb), answer[0], answer[1]);
                                } else {
                                    sph.edit().remove("jsonCalc"+b);
                                    sph.edit().apply();
                                }
                                //b+=1;
                            }
                            catch(Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText((MainActivity)c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    /*Handler h = new Handler(this.getMainLooper());
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {},0);*/
                        }
                    }
                    //return ja;
                } catch(Exception ex) {
                    ex.printStackTrace();
                    //Toast.makeText((MainActivity)c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(final String... values) {
                super.onProgressUpdate(values);
                    new Handler(c.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                            CurrentComponents.addCalculatedTV((MainActivity) c,
                                    new JSONObject()
                                            .put(ASelf.Calculation.Ix.toString(), values[0])
                                            .put(ASelf.Calculation.LmtIx.toString(), Integer.parseInt(values[1]))
                                            .put(ASelf.Calculation.Exp.toString(), values[2])
                                            .put(ASelf.Calculation.ExpAns.toString(), values[3])
                                            .put(ASelf.Calculation.DoneCalc.toString(), ""));
                            } catch(Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                vs[1].setAlpha(standBy?0.5f:1f);
                ((TextView)vs[1]).setText("Select an expression");

                pd.dismiss();
            }
        }.execute();


    }
    
    public static String[] calculation(List<String> l) {
        String answer[] = new String[]{"","0"};
        double ans = Double.parseDouble(l.get(0));
        String currentSign = "!+";
        for(int x = 0; x<l.size(); x++) {
            answer[0] += l.get(x);
            if(!ASelf.isNumber(l.get(x))) {
                currentSign = l.get(x);
                //ans = ans + Integer.parseInt(aSelf.arr.get(x+1));
            } else {
                double anso = 0;
                if(x>1)anso = Double.parseDouble(l.get(x));
                ans = ASelf.calculate(currentSign, ans, anso);
            }
        }
        answer[1] = String.valueOf(ans);
        answer[1] = answer[1].endsWith(".0")?String.valueOf(Math.round(ans)):answer[1];

        return answer;
    }
    
    public int getSBEg(Context c) {
        sbEmpty = getCalcSet(c).getString(c.getResources().getString(R.string.stand_by_example_switch),"Off").equals("On")?1:0;
        return sbEmpty;
    }
    
    public void setSBEg(Context c, int a) {
        SharedPreferences.Editor ed = getCalcSet(c).edit();
        ed.putString(c.getResources().getString(R.string.stand_by_example_switch), a==1?"On":"Off");
        ed.apply();
        sbEmpty = getCalcSet(c).getString(settings[0], "Off").equals("On")?1:0;
        
    }
    
    public static boolean isNumber(String s) {
        try {new BigDecimal(s); return true;}
        catch(Exception ex){return false;}
    }
    
    public void setStandBy(boolean b) {
        standBy=b;
        phOn=b;
        //((Button)findViewById(R.id.btnMPercentage)).setEnabled(false);
    }
    
    public boolean isStandBy() {
        return standBy;
    }
    
    public void setHCB(boolean z) {
        hcb=z;
        
    }
    
    public boolean getHCB() {
        return hcb;
    }
    
    public void setHCB(Context c, boolean z) {
        hcb=z;
        if(((Activity)c).equals(((MainActivity)c))) {
            ((Button)((MainActivity)c).llScifra.getChildAt(0)).setSelected(z);
            if(z) {
                ((MainActivity)c).clearC();
            }
            
        }
    }
    
    public int getHC(Context c) {
        return getCalcHistory(c).getAll().size();
    }
    
}
