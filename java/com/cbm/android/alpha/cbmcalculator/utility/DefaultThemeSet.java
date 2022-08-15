package com.cbm.android.alpha.cbmcalculator.utility;

import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;

import com.cbm.android.alpha.cbmcalculator.ASelf;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class DefaultThemeSet {
    
    public static ArrayList<JSONObject> defaultBGFormats() {
        ArrayList<JSONObject> dbgf = new ArrayList<>();
        try {
            dbgf.add(new JSONObject()
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,"DefaultA")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,"{'A':'1.0', 'R':'1.0', 'B':'1.0', 'G':'1.0'}")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,ASelf.Constants.SOLID)
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,"{'A':'1.0', 'R':'0.0', 'B':'0.0', 'G':'0.0'}"));
            dbgf.add(new JSONObject()
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,"DefaultB")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,"{'A':'1.0', 'R':'1.0', 'B':'1.0', 'G':'1.0'}")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,ASelf.Constants.SOLID)
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,"{'A':'1.0', 'R':'0.0', 'B':'0.0', 'G':'0.0'}"));
            dbgf.add(new JSONObject()
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,"DefaultC")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,"{'A':'1.0', 'R':'0.5', 'B':'0.5', 'G':'0.5'}")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,ASelf.Constants.SOLID)
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,"{'A':'1.0', 'R':'0.0', 'B':'0.0', 'G':'0.0'}"));
            dbgf.add(new JSONObject()
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,"DefaultD")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,"{'A':'1.0', 'R':'0.5', 'B':'0.5', 'G':'0.5'}")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,ASelf.Constants.SOLID)
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,"{'A':'1.0', 'R':'1.0', 'B':'1.0', 'G':'1.0'}"));
            dbgf.add(new JSONObject()
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,"DefaultE")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,"{'A':'1.0', 'R':'1.0', 'B':'1.0', 'G':'1.0'}")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,ASelf.Constants.BORDER)
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,"{'A':'1.0', 'R':'1.0', 'B':'1.0', 'G':'1.0'}"));
            dbgf.add(new JSONObject()
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,"DefaultF")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,"{'A':'1.0', 'R':'0.0', 'B':'0.0', 'G':'0.0'}")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,ASelf.Constants.BORDER)
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,"{'A':'1.0', 'R':'0.0', 'B':'0.0', 'G':'0.0'}"));
            dbgf.add(new JSONObject()
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,"DefaultG")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,"{'A':'1.0', 'R':'0.5', 'B':'0.5', 'G':'0.5'}")
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,ASelf.Constants.BORDER)
            .put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,"{'A':'1.0', 'R':'0.5', 'B':'0.5', 'G':'0.5'}"));
            
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return dbgf;
    }
    
    public static String getID(JSONObject jo) throws JSONException {
        return jo.getString(ASelf.Constants.DEFAULT_JSONCOLOR_ID);
    }
    
    public static String getBGCOLOR(JSONObject jo) throws JSONException {
        return jo.getString(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR);
    }
    
    public static String getBGTYPE(JSONObject jo) throws JSONException {
        return jo.getString(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE);
    }
    
    public static String getTXTCOLOR(JSONObject jo) throws JSONException {
        return jo.getString(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR);
    }
    
    public static ShapeDrawable mkShape(JSONObject jo) throws JSONException {
        return mkShape(jo, 10f);
    }
    
    public static ShapeDrawable mkShape(JSONObject jo, float r) throws JSONException {
        ShapeDrawable sd = new ShapeDrawable();
        
        RoundRectShape rs = new RoundRectShape(
        new float[]{r,r,r,r,r,r,r,r},
        null,
        null);
        sd.setShape(rs);
        sd.getPaint().setStyle(getBGTYPE(jo).equals(ASelf.Constants.SOLID)?Style.FILL:Style.STROKE);
        sd.getPaint().setColor(CBMPaint.getJSONColor(new JSONObject(getBGCOLOR(jo))).toArgb());
        
        sd.getPaint().setStrokeWidth(5f);
        
        return sd;
    }
    
    /*public static ShapeDrawable mkOShape(JSONObject jo, float r) throws JSONException {
        ShapeDrawable sd = new ShapeDrawable();
        
        OvalShape rs = new OvalShape(
        new float[]{r,r,r,r});
        sd.setShape(rs);
        sd.getPaint().setStyle(getBGTYPE(jo).equals(ASelf.Constants.SOLID)?Style.FILL:Style.STROKE);
        sd.getPaint().setColor(CBMPaint.getJSONColor(new JSONObject(getBGCOLOR(jo))).toArgb());
        
        sd.getPaint().setStrokeWidth(5f);
        
        return sd;
    }*/
    
}
