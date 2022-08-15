package com.cbm.android.alpha.cbmcalculator.settings;

import android.app.Activity;
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

import com.cbm.android.alpha.cbmcalculator.ASelf;
import com.cbm.android.alpha.cbmcalculator.R;
import com.cbm.android.alpha.cbmcalculator.custom.Setter;
import com.cbm.android.alpha.cbmcalculator.utility.*;

import org.json.JSONObject;

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
        //btnSetEg = this.findViewById(R.id.btnSetEg);
        llSettings = this.findViewById(R.id.llSetThings);
        //llSetColor = this.findViewById(R.id.llSetColor);
        //llSetSwitch = this.findViewById(R.id.llSetSwitch);
        btnReset = this.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(onResetSettings());
        /*createCartegory("Limits of structure");
        addCartegoryItem("Limits of structure","History Records", "Texttcn");
        if(isCartegory("Theme Colors")==false){createCartegory("Theme Colors");}
        if(isCartegory("Theme Colors")==false){createCartegory("Theme Colors2");}
        addCartegoryItem("Theme Colors","Background", "Texttcn");
        addCartegoryItem("Theme Colors","Text", "Texttcn");
        if(isCartegory("Switch")==false)createCartegory("Switch");
        if(isCartegory("Switch")==true)addCartegoryItem("Switch","Stand By", "Switch");
        if(isCartegory("Switch")==true)addCartegoryItem("Switch","Decimal", "Switch");
        addCartegoryItem("Switch","Decimal Place", "Texttcn");
        //lvSetColor = this.findViewById(R.id.lvSetColor);
        //btnSetEg.setTextColor(R.drawable.ctext_black_press);
        //setSBEg(sp.getInt("StandBy_Examples", -1));
        //changeBtns();
        btnSetEg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getInt("StandBy_Examples", -1)==-1) {
                    ed.putInt("StandBy_Examples", 0);
                    ed.apply();
                }
                
                setSBEg(!btnSetEg.isSelected()?1:0);
                changeBtns();
                ASelf.get().sbEmpty=sp.getInt("StandBy_Examples", -1);
            }
            
        });*/
        updateSettings();
        applySettings();
    }
    
    public void onResume() {
        super.onResume();
        applySettings();
    }
    
    public void applySettings() {
        try {
            llSettings.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.background_color), ""))));
            
        } catch(Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void updateSettings() {
        try {
        //lvSetColor.setAdapter(new SetterAdapter(this, ASelf.clrStates));
             int x = 0;
             for(String s: ASelf.settings) {
             if(!getSetType(s).equals("Text")&&!getSetType(s).equals("Textc")) {
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
    
    private View.OnClickListener onResetSettings() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                                 
             AlertDialog.Builder adbr = new AlertDialog.Builder(getApplicationContext());
                adbr.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                   @Override
                    public void onClick(DialogInterface d, int aa) {
                      SharedPreferences.Editor ed = ASelf.getCalcSet(getApplicationContext()).edit();
                int x = 0;
                int y = 0;
                //List<String> arra = Arrays.asList(ASelf.settings);
                //arra.sort(Comparator.comparing().nullsFirst());
                Toast.makeText(getApplicationContext(), Arrays.asList(ASelf.settings).toString(), Toast.LENGTH_SHORT).show();
                for(String s: ASelf.settings) {
                    String type = s.substring(0, s.indexOf("_"));
                    try{
                        
                    switch(type) {
                        case "Color":
                            ed.putInt(s, R.drawable.bg_green_r);
                            ((LinearLayout)((Setter)llSetColor.getChildAt(x)).getChildAt(1)).getChildAt(0).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.bg_green_s));
                            if(x==llSetColor.getChildCount()-1){x=-1;}
                    x=x+1;
                            break;
                        case "Switch":
                            ed.putString(s, "Off");
                            ((Setter)llSetSwitch.getChildAt(y)).set(type,ASelf.getCalcSet(getApplicationContext()).getString(s,"Off"));
                            //((LinearLayout)((LinearLayout)llSetSwitch.getChildAt(y)).getChildAt(1)).getChildAt(0).setSelected(false);
                            //((LinearLayout)llSetSwitch.getChildAt(y)).getChildAt(0).setSelected(false);
                            if(y==llSetSwitch.getChildCount()-1){y=-1;}
                    y=y+1;
                            break;
                    }
                    } catch (Exception ex){
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(), ex.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    
                }  
                    }
                });
                   adbr.setMessage("Reset to Factory Default Settings");
                adbr.create().show();
        */        
        Toast.makeText(getApplicationContext(), "Will one day reset the settings of this app. [Deep husk]", Toast.LENGTH_SHORT).show();
            }
        };
        
    }
    
}
