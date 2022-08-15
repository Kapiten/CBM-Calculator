package com.cbm.android.alpha.cbmcalculator.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cbm.android.alpha.cbmcalculator.R;

public class Backspacer extends LinearLayout {
    
    public ImageButton btnBackspace, btnBOption;
    
    public Backspacer(Context c, AttributeSet a, int b) {
        super(c,a,b);
        initialisation();
    }
    
    public Backspacer(Context c, AttributeSet a) {
        super(c,a);
        initialisation();
    }
    
    public Backspacer(Context c) {
        super(c);
        initialisation();
    }
    
    public void initialisation() {
        try{
        LayoutInflater.from(getContext()).inflate(R.layout.backspacer, this, true);
        btnBackspace = findViewById(R.id.btnBackspace);
        btnBOption = findViewById(R.id.btnBOption);
        } catch(Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            
            ex.printStackTrace();}
    }
}
