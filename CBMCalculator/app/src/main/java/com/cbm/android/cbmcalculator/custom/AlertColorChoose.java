package com.cbm.android.cbmcalculator.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.cbmcalculator.ASelf;
import com.cbm.android.cbmcalculator.R;

public class AlertColorChoose extends LinearLayout {
    
    private View vColor;
    private LinearLayout llCC;
    private SeekBar sb;
    public Button btnDCCCancel, btnAMns, btnAPls,btnCCApply;
    private int zz = 0, pc=0;
        
    public AlertColorChoose(Context c, AttributeSet a, int b) {
        super(c,a,b);
    }
    
    public AlertColorChoose(Context c, AttributeSet a) {
        super(c,a);
    }
    
    public AlertColorChoose(Context c) {
        super(c);
        initialisation();
    }
    
    public void initialisation() {
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_color_choose, this, true); 
        
        vColor = findViewById(R.id.vChosenColor);
        llCC = findViewById(R.id.llPrimeColor);
        sb = findViewById(R.id.sbColorAdjust);
        btnAMns = findViewById(R.id.btnColorAdjustMns);
        btnAPls = findViewById(R.id.btnColorAdjustPls);
        btnDCCCancel = findViewById(R.id.btnDCCCancel);
        btnCCApply = findViewById(R.id.btnColorApply);
        ASelf.setting=ASelf.getCalcSet(getContext()).getString(ASelf.stitle, "#ffffffff");
        applyAdjustBtns();
        sb.setMin(0);
        sb.setMax(255);
        sb.setOnSeekBarChangeListener(onAdjustBarChange());
        btnAMns.setOnClickListener(onAdjust());
        btnAPls.setOnClickListener(onAdjust());
        vColor.setBackgroundColor(Color.parseColor(ASelf.setting));
        updateTV();
        
    }
    
    private void applyAdjustBtns() {
        for(int z = 0; z < llCC.getChildCount(); z++) {
            zz=z;
            llCC.getChildAt(z).setTag(z);
            llCC.getChildAt(z).setOnClickListener(new View.OnClickListener() {
                @Override 
                public void onClick(View v) {
                    mkSelectd(false);
                    v.setSelected(true);
                    Color clr = Color.valueOf(Color.parseColor(ASelf.setting));
                    switch((int)v.getTag()) {
                        case 0:sb.setProgress(Math.round(clr.red()*255)); break;
                        case 1:sb.setProgress(Math.round(clr.green()*255)); break;
                        case 2:sb.setProgress(Math.round(clr.blue()*255)); break;
                    }
                    
                    Toast.makeText(getContext(),"V#"+(int)v.getTag()+" Val"+sb.getProgress(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    
    private void mkSelectd(boolean b) {
        for(int y = 0; y < llCC.getChildCount(); y++) {
            //if(llCC.getChildAt(y).isSelected()){pc=y; continue;}
            llCC.getChildAt(y).setSelected(b);
        }
                    
    }
    
    private View getSelectdV() {
        for(int y = 0; y < llCC.getChildCount(); y++) {
            if(llCC.getChildAt(y).isSelected()){pc=y; return llCC.getChildAt(y);}
        }
        
        return null;
    }
    
    private void updateTV() {
        Color clr = Color.valueOf(Color.parseColor(ASelf.setting));
        for(int y = 0; y < llCC.getChildCount(); y++) {
            switch(y) {
                        case 0:((TextView)llCC.getChildAt(y)).setText("R:"+Math.round(clr.red()*255)); break;
                        case 1:((TextView)llCC.getChildAt(y)).setText("G:"+Math.round(clr.green()*255)); break;
                        case 2:((TextView)llCC.getChildAt(y)).setText("B:"+Math.round(clr.blue()*255)); break;
                    }
        }
        
        
    }
    
    private OnSeekBarChangeListener onAdjustBarChange() {
        return new OnSeekBarChangeListener() {
            
            @Override
            public void onStartTrackingTouch(SeekBar v) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar v) {
                
            }

            @Override
            public void onProgressChanged(SeekBar v, int x, boolean b) {
                try {
                    Color clr = Color.valueOf(Color.parseColor(ASelf.setting));
                    switch(Integer.parseInt(getSelectdV().getTag()+"")) {
                        case 0:clr=Color.valueOf(clr.argb(255, v.getProgress(), Math.round(clr.green()*255), Math.round(clr.blue()*255))); break;
                        case 1:clr=Color.valueOf(clr.argb(255, Math.round(clr.red()*255), v.getProgress(), Math.round(clr.blue()*255))); break;
                        case 2:clr=Color.valueOf(clr.argb(255, Math.round(clr.red()*255), Math.round(clr.green()*255), v.getProgress())); break;
                    }
                    
                    String sr = Integer.toHexString(Math.round(clr.red()*255));
                    String sg = Integer.toHexString(Math.round(clr.green()*255));
                    String sb = Integer.toHexString(Math.round(clr.blue()*255));
                    ASelf.setting="#ff"+(sr.length()==1?"0"+sr:sr)+(sg.length()==1?"0"+sg:sg)+(sb.length()==1?"0"+sb:sb);
                    
                    vColor.setBackgroundColor(clr.toArgb());
                    updateTV();
                } catch (Exception ex) {
                    
                    ex.printStackTrace();
                }
            }
        };
    }
    
    private View.OnClickListener onAdjust() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.equals(btnAMns)) {
                    int aa = sb.getProgress()-1<=0?0:sb.getProgress()-1;
                    sb.setProgress(aa);
                } else {
                    int aa = sb.getProgress()+1>=255?255:sb.getProgress()+1;
                    sb.setProgress(aa);
                }
            }
        };
    }
}

