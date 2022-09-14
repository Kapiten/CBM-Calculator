package com.cbm.android.cbmcalculator.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.cbmcalculator.ASelf;
import com.cbm.android.cbmcalculator.R;
import com.cbm.android.cbmcalculator.custom.Setter;

import java.util.Arrays;

public class SettingsActivity extends Activity {
    private Button btnSetEg, btnReset;
    private ListView lvSetColor;
    private LinearLayout llSettings, llSet, llSetColor,llSetSwitch;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    private String cSetting;
    private TextView tvSetTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        
        try{init();}catch(Exception ex){Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show(); ex.printStackTrace();}
    }
    
    private void init() {
        llSettings = this.findViewById(R.id.llSetThings);
        btnReset = this.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(onResetSettings());
        
        updateSettings();
        applySettings();
    }
    
    public void onResume() {
        super.onResume();
        applySettings();
    }
    
    public void applySettings() {
        try {
            //llSettings.setBackgroundColor(ASelf.getCalcSet(this).getInt(getResources().getString(R.string.background_color), Color.WHITE));
            
        } catch(Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void updateSettings() {
        try {
        //lvSetColor.setAdapter(new SetterAdapter(this, ASelf.clrStates));
             
            clearAllItem();
            int x = 0;
             for(String s: ASelf.settings) {
             if(!getSetType(s).equals("Text")&&!getSetType(s).equals("Textc")&&!getSetType(s).equals("Color")) {
                 if(isCartegory(getSetCartegory(s))==false)createCartegory(getSetCartegory(s));
                 addCartegoryItem(getSetCartegory(s),getSetName(s),x);
             }
                /*if(isCartegory(getSetType(s))==false){createCartegory(getSetType(s));}/*llSetColor.addView(sclr);*
                addCartegoryItem(getSetType(s),getSetName(s),getSetType(s));
                Setter sclr = new Setter(this,x);
                 sclr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                 //sclr.setPView()
                
                switch(getSetType(s)) {
                    case "Color": if(isCartegory(getSetType(s)){createCartegory(getSetType(s));addCartegoryItem(getSetType(s),getSetName(s),getSetType(s));})/*llSetColor.addView(sclr);* break;
                    case "Switch": llSetSwitch.addView(sclr); break;
                    case "TextTCN": llSetSwitch.addView(sclr); break;
            }*/
            
                 
                 x+=1;
             }
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
    
    public static String getSetCartegory(String n) {
        return n.contains("+")?n.substring(n.indexOf("_")+1, n.indexOf("+")).replace("_", " "):n.substring(0, n.indexOf("_"));
    }
    
    public static String getSetType(String n) {
        return n.substring(0, n.indexOf("_"));
    }
    
    public static String getSetName(String n) {
        return n.contains("+")?n.substring(n.indexOf("+")+1).replace("_", " "):n.substring(n.indexOf("_")+1).replace("_", " ");
    }
    
    private void createCartegory(String title) {
        LinearLayout ll = new LinearLayout(this);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        ll.setLayoutParams(lp);
        ll.setBackground(getResources().getDrawable(R.drawable.bg_grey_s, null));
        ll.setPadding(10,10,10,10);
        ll.setOrientation(LinearLayout.VERTICAL);
        llSettings.addView(ll);
        
        TextView tv = new TextView(this);
        LayoutParams lptv = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lptv);
        tv.setBackground(getResources().getDrawable(R.drawable.bg_grey_r, null));
        tv.setPadding(5,5,5,5);
        tv.setGravity(Gravity.CENTER);
        tv.setText(title);
        tv.setTextColor(Color.WHITE);
        ll.addView(tv);
        
    }
    
    private void addCartegoryItem(String cartegory, String item, int it) {
        if(isCartegory(cartegory)) {
                //String s= ((TextView)((LinearLayout)llSettings.getChildAt(x)).getChildAt(0)).getText().toString();
                Setter set = new Setter(this,it);
                set.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                set.tvTitle.setText(item);
               //if(it instanceof Integer){set.initialiseView(it.toString());}
                //((TextView)((LinearLayout)llSettings.getChildAt(x)).getChildAt(0)).setText(item);
                if(getCartegory(cartegory)!=null)getCartegory(cartegory).addView(set);
            
        }
    }
    
    private void clearAllItem() {
        llSettings.removeAllViews();
    }
    
    private LinearLayout getCartegory(String n) {
        for(int x = 0; x < llSettings.getChildCount(); x++) {
            if(((TextView)((LinearLayout)llSettings.getChildAt(x)).getChildAt(0)).getText().toString().equals(n)) {
                return ((LinearLayout)llSettings.getChildAt(x));
            }
        }
        
        return null;
    }
    
    private boolean isCartegory(String n) {
        for(int x = 0; x < llSettings.getChildCount(); x++) {
            if(((TextView)((LinearLayout)llSettings.getChildAt(x)).getChildAt(0)).getText().toString().equals(n)) {
                return true;
            }
        }
        
        return false;
    }
    
    public View.OnClickListener onResetSettings() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AlertDialog.Builder adbr = new AlertDialog.Builder(SettingsActivity.this);
                adbr.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                   @Override
                    public void onClick(DialogInterface d, int aa) {
                        SharedPreferences.Editor ed = ASelf.getCalcSet(getApplicationContext()).edit();
                        
                        SharedPreferences.Editor edh = ASelf.getCalcHistory(getApplicationContext()).edit();
                        edh.clear();
                        edh.commit();
                        edh.apply();
                        int x = 0;
                int y = 0;
                ed.putString(getResources().getString(R.string.stand_by_example_switch), "Off");
                ed.putString(getResources().getString(R.string.stand_by_example_default_expression_texttcn), "1+2");
                ed.putString(getResources().getString(R.string.decimal_always_switch), "Off");
                ed.putString(getResources().getString(R.string.decimal_count_textn), "1");
                ed.putString(getResources().getString(R.string.history_limit_textn), "10");
                
                            ed.apply();
                        updateSettings();
                    }
                });
                adbr.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                   @Override
                    public void onClick(DialogInterface d, int aa) {
                        
                    }
                });
                adbr.setMessage("Reset to Factory Default Settings");
                adbr.create().show();
           
            }
        };
        
    }
    
}
