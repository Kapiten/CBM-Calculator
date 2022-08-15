package com.cbm.android.alpha.cbmcalculator.utility;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import org.json.JSONObject;

import com.cbm.android.alpha.cbmcalculator.ASelf;

public class CBMPaint extends Paint {
    private Paint solid;
    private Paint stroke;
    
    public CBMPaint() {
        solid = new Paint();
        solid.setStyle(Style.FILL);
        
        stroke = new Paint();
        stroke.setStyle(Style.STROKE);
        
        set(this);
    }
    
    public Paint getSolid() {
        return solid;
    }
    
    public void setSolid(Paint p) {
        this.solid = p;
    }
    
    public Paint getStroke() {
        return stroke;
    }
    
    public void setStroke(Paint p) {
        this.stroke = p;
    }
    
    public static Color getJSONColor(JSONObject jo) {
        try {
            return Color.valueOf(Color.argb(
        Float.parseFloat(jo.getString("A")),
        Float.parseFloat(jo.getString("R")),
        Float.parseFloat(jo.getString("G")),
        Float.parseFloat(jo.getString("B"))));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return Color.valueOf(1.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public static JSONObject toJSONObject(String[] ss) {
        try {
            return new JSONObject()
																		.put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,ss[0])
																		.put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,ss[1])
																		.put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,ss[2])
																		.put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,ss[3]);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return new JSONObject();
    }
    
}
