package com.cbm.android.cbmcalculator.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.cbmcalculator.ASelf;
import com.cbm.android.cbmcalculator.R;

public class AlertColorChooseB extends LinearLayout {
    
    private ScrollView svColors, svDCCBM;
    private LinearLayout llColors;
    private TextView tvChkBG, tvChkTxt,tvChk;
    private LinearLayout glCC;
    public Button btnCCancel, btnCCApply;
    private View[] vs;
    private int zz = 0, pc=0;
    public int sbgi=0, sti=0, sbgs=0, sts=0, ct=0, cbgt=0;//si=Selected Index, ct=Color Type
    //private ArrayList<Color> arrC = new ArrayList<>();
        
    public AlertColorChooseB(Context c, AttributeSet a, int b) {
        super(c,a,b);
    }
    
    public AlertColorChooseB(Context c, AttributeSet a) {
        super(c,a);
    }
    
    public AlertColorChooseB(Context c) {
        super(c);
        initialisation();
    }
    
    public AlertColorChooseB(Context c, View...vs) {
        super(c);
        initialisation();
        this.vs = vs;
        
    }
    
    public void initialisation() {
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_color_choose_b, this, true);
        
        svColors = findViewById(R.id.svColors);
        llColors = findViewById(R.id.llChosenColor);
        glCC = findViewById(R.id.glColors);
        svDCCBM = findViewById(R.id.svDCCBMain);
        tvChk = findViewById(R.id.tvChk);
        tvChkTxt = findViewById(R.id.tvDCCTC);
        tvChkBG = findViewById(R.id.tvDCCBGC);
        btnCCancel = findViewById(R.id.btnCCancel);
        btnCCApply = findViewById(R.id.btnColorApply);
        ASelf.setting=ASelf.getCalcSet(getContext()).getString(ASelf.stitle, "#ffffffff");
        //applyAdjustBtns();
        tvChkTxt.setOnClickListener(onAdjust());
        tvChkBG.setOnClickListener(onAdjust());
        //btnCCApply.setOnClickListener(onActBtn());
        //btnCCancel.setOnClickListener(onActBtn());
        //tvChkBG.setOnClickListener(onAdjust());
        //vColor.setBackgroundColor(Color.parseColor(ASelf.cColor));
        applyAdjustBtns();
        svColors.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View v) {
                svColors.setFocusedByDefault(true);
                svColors.setSelected(true);
            }
        });
    }
    
    private void applyAdjustBtns() {
        try{
            for(int z = 0; z < ASelf.arrC().size(); z++) {
            zz=z;
            View vc = new View(getContext());
            vc.setBackgroundResource(ASelf.arrC().get(z));
            vc.setTag(z);
            vc.setOnClickListener(new View.OnClickListener() {
                @Override 
                public void onClick(View v) {
                    mkSelectd(false);
                    v.setSelected(true);
                    if(tvChkBG.isSelected()) {
                        llColors.setBackground(v.getBackground());
                        sbgs=ASelf.arrC().get(Integer.parseInt(String.valueOf(v.getTag())));
                        sbgi=Integer.parseInt(String.valueOf(v.getTag()));
                            
                     } else {
                            int zzz = Integer.parseInt(v.getTag()+"");
                            int mlt = zzz>=7?((-(zzz/7)*7)+(zzz<=7?0:zzz)):zzz;
                            if(mlt>=0&&mlt<=7) {
                                tvChk.setTextColor(ASelf.arrClr().get(mlt));
                                sts=ASelf.arrClr().get(mlt);
                                sti=mlt;
                                ct=1;
                            }
                    }
                    
                    //Toast.makeText(getContext(),"V#"+(int)v.getTag()+" Val"+ASelf.arrC().get(zz), Toast.LENGTH_SHORT).show();
                }
            });
            
            glCC.addView(vc, new LayoutParams(LayoutParams.MATCH_PARENT,60));
        }
        } catch (Exception ex) {
            Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void mkSelectd(boolean b) {
        for(int y = 0; y < glCC.getChildCount(); y++) {
            //if(llCC.getChildAt(y).isSelected()){pc=y; continue;}
            glCC.getChildAt(y).setSelected(b);
        }     
    }
    
    private View getSelectdV() {
        for(int y = 0; y < glCC.getChildCount(); y++) {
            if(glCC.getChildAt(y).isSelected()){pc=y; return glCC.getChildAt(y);}
        }
        
        return null;
    }
    /*
    private void updateTV() {
        Color clr = Color.valueOf(Color.parseColor(ASelf.cColor));
        for(int y = 0; y < llCC.getChildCount(); y++) {
            switch(y) {
                        case 0:((TextView)llCC.getChildAt(y)).setText("R:"+Math.round(clr.red()*255)); break;
                        case 1:((TextVfalseiew)llCC.getChildAt(y)).setText("G:"+Math.round(clr.green()*255)); break;
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
                    Color clr = Color.valueOf(Color.parseColor(ASelf.cColor));
                    switch(Integer.parseInt(getSelectdV().getTag()+"")) {
                        case 0:clr=Color.valueOf(clr.argb(255, v.getProgress(), Math.round(clr.green()*255), Math.round(clr.blue()*255))); break;
                        case 1:clr=Color.valueOf(clr.argb(255, Math.round(clr.red()*255), v.getProgress(), Math.round(clr.blue()*255))); break;
                        case 2:clr=Color.valueOf(clr.argb(255, Math.round(clr.red()*255), Math.round(clr.green()*255), v.getProgress())); break;
                    }
                    
                    String sr = Integer.toHexString(Math.round(clr.red()*255));
                    String sg = Integer.toHexString(Math.round(clr.green()*255));
                    String sb = Integer.toHexString(Math.round(clr.blue()*255));
                    ASelf.cColor="#ff"+(sr.length()==1?"0"+sr:sr)+(sg.length()==1?"0"+sg:sg)+(sb.length()==1?"0"+sb:sb);
                    
                    vColor.setBackgroundColor(clr.toArgb());
                    //updateTV();
                } catch (Exception ex) {
                    
                    ex.printStackTrace();
                }
            }
        };
    }*/
    
    private View.OnClickListener onAdjust() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.equals(tvChkTxt) || v.equals(tvChkBG)) {
                    tvChkTxt.setSelected(false);
                    tvChkBG.setSelected(false);
                    mkSelectd(false);
                    v.setSelected(true);
                    
                    //tvChkTxt.setTextColor(tvChkTxt.isSelected()?Color.WHITE:Color.BLACK);
                    //tvChkBG.setTextColor(tvChkBG.isSelected()?Color.WHITE:Color.BLACK);
                    
                } else {
                    //int aa = sb.getProgress()+1>=255?255:sb.getProgress()+1;
                    //sb.setProgress(aa);
                }
            }
        };
    }
    
    private View.OnClickListener onActBtn() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.equals(btnCCApply)) {
                    
                } else {
                    cancel();
                }
            }
        };
    }
    
    public void cancel() {
        ((LinearLayout)this.getParent()).removeView(this);
    }
    
    public TextView getTVChk() {
        return tvChk;
    }
    
    public LinearLayout getBGChk() {
        return llColors;
    }
}

