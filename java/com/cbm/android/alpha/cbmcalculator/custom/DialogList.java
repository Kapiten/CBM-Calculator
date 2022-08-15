package com.cbm.android.alpha.cbmcalculator.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbm.android.alpha.cbmcalculator.R;

import java.util.List;

public class DialogList extends LinearLayout {
    
    public LinearLayout llDLI;
    
    public DialogList(Context c, AttributeSet a, int b) {
        super(c,a,b);
        initialisation();
    }
    
    public DialogList(Context c, AttributeSet a) {
        super(c,a);
        initialisation();
    }
    
    public DialogList(Context c) {
        super(c);
        initialisation();
    }
    
    public void initialisation() {
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_list, this, true);
        
        llDLI = findViewById(R.id.llDLI);
    }
    
    public void addItem(String s) {
        addItem(s, null);
    }
    
    public void addItem(String s, View.OnClickListener onClick) {
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setPadding(5,5,5,5);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(18f);
        tv.setBackground(getResources().getDrawable(R.drawable.bg_grey_r, null));
        tv.setTextColor(Color.WHITE);
        tv.setText(s);
        tv.setOnClickListener(onClick);
        tv.setTag(llDLI.getChildCount()+"");
        llDLI.addView(tv);
    }
    
    public void addListItems(List l, View.OnClickListener onClick) {
        for(int x=0; x<l.size(); x++) {
            addItem(l.get(x).toString(), onClick);
        }
    }
}
