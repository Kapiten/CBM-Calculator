package com.cbm.android.alpha.cbmcalculator.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbm.android.alpha.cbmcalculator.R;

public class MsgBar extends LinearLayout {
    
    private TextView tvTtl, tvMsg;
    private Button btnCls;
    
    public MsgBar(Context context) {
        super(context);
        
        init(context);
    }
    
    public MsgBar(Context context, AttributeSet attr) {
        super(context, attr);
        
        init(context);
    }
    
    public MsgBar(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_answer_list_item, this, true);
        
        tvTtl = v.findViewById(R.id.tvTtl);
        tvMsg = v.findViewById(R.id.tvMsg);
        btnCls = v.findViewById(R.id.btnCls);
        
    }
    
    public MsgBar makeMsg(String title, String message, boolean cls) {
        tvTtl.setText(title);
        tvMsg.setText(message);
        if(cls){btnCls.setVisibility(View.VISIBLE);}else{btnCls.setVisibility(View.GONE);}
        
        return this;
    }
    
    public void show() {
        this.setVisibility(View.VISIBLE);
    }
    
    public void hide() {
        this.setVisibility(View.GONE);
    }
}
