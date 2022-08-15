package com.cbm.android.alpha.cbmcalculator.utility;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Ut {
    
    int symDInd=0, symMInd=0, symAInd=0, symSInd=0;
    
    public static String toJSON(List l) {
        JSONObject json = new JSONObject();
        try{
            for(int x = 0; x<l.size(); x++) {
                json.put(""+x, l.get(x).toString());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return json.toString();
    }
    
    public static SuccessMsg toJSONSmsg(List l) {
        SuccessMsg smsg = new SuccessMsg();
        
        smsg.setCode(l!=null?1f:0f);
        smsg.setObj(toJSON(l));
        
        return smsg;
    }
    
    public static List<String> toArray(String j) {
        List<String> l = new ArrayList<String>();
        try{
            JSONObject json = new JSONObject(j);
            for(int x = 0; x<json.names().length(); x++) {
                l.add(json.getString(""+x));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            
        }
        
        return l;
    }
    
    public static SuccessMsg toArraySmsg(String s) {
        SuccessMsg smsg = new SuccessMsg();
        
        smsg.setCode(s!=null&&!s.isEmpty()?1f:0f);
        smsg.setObj(toArray(s));
        
        return smsg;
    }
    
    public static ArrayList<String> toBodmas(ArrayList<String> arr) {
        String[] mSigns= new String[] {"÷", "×", "+", "-"};
        return toCalculationType(arr,mSigns);
    }
    
    public static ArrayList<String> toCalculationType(ArrayList<String> arr, String[] mSigns) {
        ArrayList<String> lst = new ArrayList<>();
        for(String s: mSigns) {
            for(int x = 0; x < arr.size(); x++) {
                if(arr.get(x).equals(s)) {
                    boolean nega = false;
                    //if(x-2>=0){if(arr.get(x-2).equals("-")){nega=true;}}
                    if(x-1==0){if(arr.get(x).equals("-")){nega=true;}}
                    
                    if(lst.size()==0){lst.add(lst.size(), arr.get(x-1));}
                    lst.add(lst.size(), arr.get(x));
                    lst.add(lst.size(), arr.get(x+1));
                
                }
            }
        }
        
        return lst;
    }
    
    public static boolean measureSymbol(String s){
        boolean b = false;
        switch(s) {
            case "%": return true;
            case "(": return true;
            case ")": return true;
        }
       
        return b;
    }
    
    public static boolean containsMads(String s) {
        if(s.contains("+")){return true;}
        if(s.contains("-")){return true;}
        if(s.contains("×")){return true;}
        if(s.contains("÷")){return true;}
        if(s.contains("e")){return true;}
        if(s.contains("√")){return true;}
        return false;
    }
    
    public static boolean containsJustMads(String s) {
        if(s.contains("e")||s.contains("√")){return false;}
        if(s.contains("+")){return true;}
        if(s.contains("-")){return true;}
        if(s.contains("×")){return true;}
        if(s.contains("÷")){return true;}
        return false;
    }
    
    public static boolean jsonContainsP(JSONObject jo, String n) {
        try {
            for(int x = 0; x < jo.names().length(); x++) {
                if(jo.names().get(x).equals(n)){return true;}
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
}
