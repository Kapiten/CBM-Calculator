package com.cbm.android.alpha.cbmcalculator.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cbm.android.alpha.cbmcalculator.R;

import java.util.ArrayList;

public class CBMColorView extends LinearLayout {
    public Paint pntBG, pntS;
    public Path pthBG, pthS;
    public ArrayList<Path> ps;
    public float fR=10f;
    public int fCA=255,fCR=255,fCG=255,fCB=255,sCA=0,sCR=255,sCG=255,sCB=255;
    public Canvas canvs;
    
    public CBMColorView(Context context) {
        super(context); 
        
        init(context);
    }
    
    public CBMColorView(Context context, AttributeSet attr) {
        super(context, attr);
        
        init(context);
    }
    
    public CBMColorView(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        
        init(context);
    }
    
    private void init(Context c) {
        LayoutInflater.from(c).inflate(R.layout.cbmcolorview, this, true);
        ps = new ArrayList<>();
        
        pntBG = new Paint();
        pntBG.setAntiAlias(true);
        pntBG.setStyle(Style.FILL);
        
        pntS = new Paint();
        pntS.setAntiAlias(true);
        pntS.setStyle(Style.STROKE);
        pntS.setStrokeWidth(10f);
        pntS.setStrokeJoin(Join.ROUND);
        pntS.setStrokeCap(Cap.ROUND);
    }
    
    @Override
    public void draw(Canvas cnvs) {
        pthBG = new Path();
        pntBG.setColor(Color.argb(fCA, fCR, fCG, fCB));
        pthBG.addRoundRect(10f,10f,Float.parseFloat(cnvs.getWidth()+"")-20f,Float.parseFloat(cnvs.getHeight()+"")-20f,fR,fR, Direction.CW);
        cnvs.drawPath(pthBG, pntBG);
        
        pthS = new Path();
        pntS.setColor(Color.argb(sCA, sCR, sCG, sCB));
        pthS.addRoundRect(5f,5f,Float.parseFloat(cnvs.getWidth()+"")-15f,Float.parseFloat(cnvs.getHeight()+"")-15f,fR,fR, Direction.CW);
        cnvs.drawPath(pthS, pntS);
        canvs = cnvs;
        							
        ps.add(0,pthBG);
        ps.add(1,pthS);
    }
    
}
