package com.cbm.android.alpha.cbmcalculator.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.alpha.cbmcalculator.R;

import java.util.Arrays;

public class AlertColorChooseC extends LinearLayout {
    
    public String setTitle="";
    LinearLayout llPC, llObjects;
    RelativeLayout rlVA;
    Button btnM, btnP, btnCCancel, btnCCApply;
    SeekBar sb;
    public ShapeDrawable sd;
    public RoundRectShape rs;
    public TextView tvTxt;
    private TextView tvA,tvR,tvG,tvB,tvAmount;
    public View theVA;
    public Color tClr, sClr, bClr; //tClr=textColor, solidColor, borderColor
    public int selctd = 0, selctdV = 0, selctdC 	= 0;
        
    public String[] ss = new String[]{"ID","BGColor","BGType","TxtColor"};//ID,BGColor,BGType,TxtColor
    
    public AlertColorChooseC(Context c, AttributeSet a, int b) {
        super(c,a,b);
    }
    
    public AlertColorChooseC(Context c, AttributeSet a) {
        super(c,a);
    }
    
    public AlertColorChooseC(Context c) {
        super(c);
        initialisation();
    }
    
    private void initialisation() {
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_color_chooser_c,this,true);
        
        theVA = findViewById(R.id.vA);
        
        llPC = findViewById(R.id.llPC);
        llObjects = findViewById(R.id.llObjects);
        rlVA = findViewById(R.id.llDCCVA);
        tvTxt = findViewById(R.id.tvDCCTC);
        sb = findViewById(R.id.sbColorAdjust);
        btnM = findViewById(R.id.btnM);
        btnP = findViewById(R.id.btnP);
        tvAmount = findViewById(R.id.tvColorAmt);
        btnCCancel = findViewById(R.id.btnCCancel);
        btnCCApply = findViewById(R.id.btnColorApply);
        sb.setMax(255);
        sb.setMin(0);
        applyOnClick();
        sb.setOnSeekBarChangeListener(onAdjustBarChange());
        theVA.setOnClickListener(onSelectV());
        btnM.setOnClickListener(onAdjust());
        btnP.setOnClickListener(onAdjust());
        tClr = Color.valueOf(Color.argb(1f,0f,0f,0f));
        sClr = Color.valueOf(Color.argb(1f,0.5f,0.5f,0.5f));
        bClr = Color.valueOf(Color.argb(1f,0.5f,0.5f,0.5f));
        
        sd = new ShapeDrawable();
        rs = new RoundRectShape(
        new float[]{10f,10f,10f,10f,10f,10f,10f,10f},
        null,
        null);
        sd.setShape(rs);
        sd.getPaint().setStyle(Style.FILL);
        sd.getPaint().setColor(sClr.toArgb());
        sd.getPaint().setStrokeWidth(5f);
        tvTxt.setTextColor(Color.BLACK);
        
        theVA.setBackground(sd);
        llPC.getChildAt(0).setSelected(true);
        llObjects.getChildAt(0).setSelected(true);
       
    }
    
    public void applyOnClick() {
        for(int x=0;x<llPC.getChildCount();x++) {
            llPC.getChildAt(x).setOnClickListener(onSelectP());
        }
        
        /*for(int x=0;x<rlVA.getChildCount();x++) {
            rlVA.getChildAt(x).setOnClickListener(onSelectV());
        }*/
        
        for(int x=0;x<llObjects.getChildCount();x++) {
            llObjects.getChildAt(x).setOnClickListener(onSelectQ());
        }
    }
    
    public void selectP(View v) {
        for(int x=0;x<llPC.getChildCount();x++) {
            llPC.getChildAt(x).setSelected(false);
            if(v.equals(llPC.getChildAt(x))) selctdC=x;
        }
        
        v.setSelected(true);
    }
    
    public void selectLV(Object l, View v) {
        for(int x=0;x<(l instanceof RelativeLayout?((RelativeLayout)l):((LinearLayout)l)).getChildCount();x++) {
            View rlV = l instanceof RelativeLayout?((RelativeLayout)l).getChildAt(x):((LinearLayout)l).getChildAt(x);
            rlV.setSelected(false);
            if(v.equals(rlV))selctdV=x;
        }
        v.setSelected(true);
    }
    
    public void selectO(View v) {
        for(int x=0;x<llObjects.getChildCount();x++) {
            llObjects.getChildAt(x).setSelected(false);
            if(v.equals(llObjects.getChildAt(x))) {
                selctdV=x;
                if(x==1) {
                    sd.getPaint().setStyle(Style.FILL);
                    sd.getPaint().setColor(sClr.toArgb());
                    ss[2]="SOLID";
                } else if(x==2) {
                    sd.getPaint().setStyle(Style.STROKE);
                    sd.getPaint().setColor(bClr.toArgb());
                    ss[2]="STROKE";
                    
                }
            }
        }
        
        sd.setShape(rs);
        theVA.setBackground(sd);
        //setClr(
        //new String[]{ss[0],
        //    new JSONObject().put("A",)});
        v.setSelected(true);
    }
    
    public void setClr(String[] s) {
        ss=s;
        
    }
    
    private void adjust(int aa) {
        int z = aa;
        float zf = 0;
        try {zf=Float.parseFloat((z/255f) +"");}catch(Exception ex){ex.printStackTrace();}
            switch(selctdC) {
            case 0:
                if(selctdV==0) {
                    tClr = Color.valueOf(Color.argb(zf,tClr.red(),tClr.green(),tClr.blue()));
                    getTVChk().setTextColor(tClr.toArgb());
                    
                } else if(selctdV==1){
                    sClr=Color.valueOf(Color.argb(z/255f,sClr.red(),sClr.green(),sClr.blue()));
                			sd.getPaint().setColor(sClr.toArgb());
                } else if(selctdV==2){
                    bClr=Color.valueOf(Color.argb(z/255f,bClr.red(),bClr.green(),bClr.blue()));
                    sd.getPaint().setColor(bClr.toArgb());
                			
                }
            break;
            case 1:
                if(selctdV==0){
                    tClr = Color.valueOf(Color.argb(tClr.alpha(),zf,tClr.green(),tClr.blue()));
                    getTVChk().setTextColor(tClr.toArgb());
                }	else if(selctdV==1)	{
                    sClr=Color.valueOf(Color.argb(sClr.alpha(),z/255f,sClr.green(),sClr.blue()));
                    sd.getPaint().setColor(sClr.toArgb());
                }	else if(selctdV==2)	{
                    bClr=Color.valueOf(Color.argb(bClr.alpha(),z/255f,bClr.green(),bClr.blue()));
                    sd.getPaint().setColor(bClr.toArgb());
                }
            break;
            case 2:
                if(selctdV==0)	{
                    tClr = Color.valueOf(Color.argb(tClr.alpha(),tClr.red(),zf,tClr.blue()));
                    getTVChk().setTextColor(tClr.toArgb());
                }	else if(selctdV==1)	{
                    sClr=Color.valueOf(Color.argb(sClr.alpha(), sClr.red(),z/255f,sClr.blue()));
                    sd.getPaint().setColor(sClr.toArgb());
                }	else if(selctdV==2)	{
                    bClr=Color.valueOf(Color.argb(bClr.alpha(), bClr.red(),z/255f,bClr.blue()));
                    sd.getPaint().setColor(bClr.toArgb());
                }
            break;
            case 3:
                if(selctdV==0)	{
                    tClr = Color.valueOf(Color.argb(tClr.alpha(),tClr.red(),tClr.green(),zf));
                    getTVChk().setTextColor(tClr.toArgb());
                } else if(selctdV==1)	{
                    sClr=Color.valueOf(Color.argb(sClr.alpha(),sClr.red(),sClr.green(),z/255f));
                    sd.getPaint().setColor(sClr.toArgb());
                    
                }	else if(selctdV==2)	{
                    bClr=Color.valueOf(Color.argb(bClr.alpha(),bClr.red(),bClr.green(),z/255f));
                    sd.getPaint().setColor(bClr.toArgb());
                }
            break;
        }
        
        if(selctdV==1)ss[1]="{'A':'"+sClr.alpha()+"','R':'"+sClr.red()+"','G':'"+sClr.green()+"','B':'"+sClr.blue()+"'}";
        if(selctdV==2)ss[1]="{'A':'"+bClr.alpha()+"','R':'"+bClr.red()+"','G':'"+bClr.green()+"','B':'"+bClr.blue()+"'}";
        ss[3]="{'A':'"+tClr.alpha()+"','R':'"+tClr.red()+"','G':'"+tClr.green()+"','B':'"+tClr.blue()+"'}";
        Toast.makeText(getContext(), Arrays.asList(ss).toString(), Toast.LENGTH_SHORT).show();
        sd.setShape(rs);
        theVA.setBackground(sd);
    }
    
    private View.OnClickListener onSelectP() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Color clr = Color.valueOf(((ShapeDrawable)theVA.getBackground()).getPaint().getColor());
                selectP(v);
                try {
                    switch(selctdC) {
                        case 0:if(selctdV==0){sb.setProgress(Math.round(tClr.alpha()*255));}else if(selctdV==1){sb.setProgress(Math.round(sClr.alpha()*255));}else if(selctdV==2){sb.setProgress(Math.round(bClr.alpha()*255));}break;
                        case 1:if(selctdV==0){sb.setProgress(Math.round(tClr.red()*255));}else if(selctdV==1){sb.setProgress(Math.round(sClr.red()*255));}else if(selctdV==2){sb.setProgress(Math.round(bClr.red()*255));}break;
                        case 2:if(selctdV==0){sb.setProgress(Math.round(tClr.green()*255));}else if(selctdV==1){sb.setProgress(Math.round(sClr.green()*255));}else if(selctdV==2){sb.setProgress(Math.round(bClr.green()*255));}break;
                        case 3:if(selctdV==0){sb.setProgress(Math.round(tClr.blue()*255));}else if(selctdV==1){sb.setProgress(Math.round(sClr.blue()*255));}else if(selctdV==2){sb.setProgress(Math.round(bClr.blue()*255));}break;
                        //case 4:sb.setProgress(Integer.parseInt(theVA.fR+"")); break;
                    }
                    
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
    
    private View.OnClickListener onSelectV() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLV(rlVA, v);
            }
        };
    }
    
    private View.OnClickListener onSelectQ() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectO(v);
                onSelectP().onClick(llPC.getChildAt(selctdC));
                
                
            }
        };
    }
    
    private View.OnClickListener onAdjust() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.equals(btnM)) {
                    sb.setProgress(sb.getProgress()-1<=sb.getMin()-1?sb.getMax():sb.getProgress()-1);
                } else if(v.equals(btnP)) {
                    sb.setProgress(sb.getProgress()+1>=sb.getMax()+1?0:sb.getProgress()+1);
                }
            }
        };
    }
    
    private OnSeekBarChangeListener onAdjustBarChange() {
        return new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar v) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar v) {}

            @Override
            public void onProgressChanged(SeekBar v, int x, boolean b) {
                try {
                    tvAmount.setText(x+" || "+(Math.round(v.getProgress()/255f*100))+"%");
                    adjust(x);
                } catch (Exception ex) {
                    Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            }
        };
    }
    
    public TextView getTVChk() {
        return tvTxt;
    }
    
    public View getBGChk() {
        return theVA;
    }
    
    /*@Override
    public void draw(Canvas cnvs) {
        Path pthBG = new Path();
        Paint pntBG = new Paint();
        pntBG.setAntiAlias(true);
        pntBG.setStyle(Style.FILL);
        pntBG.setColor(Color.argb(1f, 1f, 0.5f, 1f));
        pthBG.addRoundRect(10f,10f,Float.parseFloat(cnvs.getWidth()+"")-20f,Float.parseFloat(cnvs.getHeight()+"")-20f,10f,10f, Direction.CW);
        cnvs.drawPath(pthBG, pntBG);
        
    }*/
}
