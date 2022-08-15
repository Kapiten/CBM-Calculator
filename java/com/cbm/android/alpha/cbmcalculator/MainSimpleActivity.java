package com.cbm.android.alpha.cbmcalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.alpha.cbmcalculator.R;
import com.cbm.android.alpha.cbmcalculator.anim.*;
import com.cbm.android.alpha.cbmcalculator.custom.*;
import com.cbm.android.alpha.cbmcalculator.extended.*;
import com.cbm.android.alpha.cbmcalculator.settings.*;
import com.cbm.android.alpha.cbmcalculator.utility.*;

import java.util.List;

import org.json.JSONObject;

public class MainSimpleActivity extends Activity {
    private ASelf aSelf;
    
    private AlertDialog ad;
    public Button btnSignChange, btnEquals, btnPoint, /*btnMASMD,*/ btnPercentage, btnMHistory, btnSettings;
    public ImageButton btnScifra, btnRefresh;
    public Backspacer btnBackspacer;
    public TextView tvCurrent, tvDoneCalculation, tvCTitle; 
    public LinearLayout llToolbar, llEntry, llCalculation, llNC, llLM, llScifra;
    public HorizontalScrollView hsvNC, llDoneCalculation;
    public ScrollView svC, svScifra;
    
    private int currIndex = 0, cSI = 0, hc=0, bracketOpen=0, bracketIndex=0; //current Selected Index
    private double bracketAnsw = 0;
    private String nowValue = "";
    public boolean isSFMShowing = false, 
    isDotd = false, hca=false, placeholders=false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_main);
        
            init();
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    
    private void init() {
        try {
            aSelf = ASelf.get(this);
            ad = new AlertDialog.Builder(this).create();
            llDoneCalculation = this.findViewById(R.id.llDoneCalculation);
            tvDoneCalculation = this.findViewById(R.id.tvDoneCalculation);
            //tvNow = this.findViewById(R.id.etNow);
            btnSettings = this.findViewById(R.id.btnMSettings);
            btnScifra = this.findViewById(R.id.btnScifra);
            btnSignChange = this.findViewById(R.id.btnMInverse);
            btnPercentage = ((Button)this.findViewById(R.id.btnMPercentage));
            btnMHistory = findViewById(R.id.btnMHistory);
            //tvCalculated = (TextView)LayoutInflater.from(this).inflate(R.layout.calculated_text, null);
            hsvNC = this.findViewById(R.id.hsvNowCalculation);
            svC = this.findViewById(R.id.svCalculation);
            btnRefresh = this.findViewById(R.id.btnRefresh);
            btnEquals = this.findViewById(R.id.btnEquals);
            btnPoint = this.findViewById(R.id.btnPoint);
            btnBackspacer = this.findViewById(R.id.btnBackspacer);
            llLM = ((LinearLayout)this.findViewById(R.id.llLM));
            llEntry = this.findViewById(R.id.llEntry);
            llToolbar = this.findViewById(R.id.llToolbar);
            svScifra = this.findViewById(R.id.svScifra);
            llScifra = this.findViewById(R.id.llScifra);
            llCalculation = this.findViewById(R.id.llCalculation);
            llNC = this.findViewById(R.id.llNC);
            Drawable d = getResources().getDrawable(R.drawable.svg_settings);
            d.setBounds(0,0,100,100);
            btnSettings.setCompoundDrawables(
            d,null,null,null);
            
            llScifra.setFocusable(true);
            llScifra.setClickable(true);
            aSelf.setStandBy(true);
            MediumAnims.scifraMenu(svScifra, false);
            applyOnClick();
            glBtns();
            hc = 0;
            hca=false;
            clearC();
            registerForContextMenu(btnSignChange);
            applySettings();
            
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        try {
            SharedPreferences.Editor sped=ASelf.getCalcSet(this).edit();
            if(aSelf.arr.size()>0&&!ASelf.phOn) {
                sped.putString("RecentExp", Ut.toJSON(aSelf.arr));
            } else {
                sped.putString("RecentExp", "");
            
            }
            sped.apply();
            
            //aSelf.onSave(MainActivity.this, -1);
            
            clearC();
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            applySettings();
            clearC();
            SharedPreferences sp = ASelf.getCalcSet(this);
            if(sp.getString(getResources().getString(R.string.stand_by_example_switch), "On").equals("On")) {
                if(aSelf.getSBEg(this)==1)placeholders();
            } else if(!sp.getString("RecentExp","").isEmpty()) {
                aSelf.setStandBy(false);
                List<String> l = (List<String>)Ut.toArray(sp.getString("RecentExp", ""));
                for(int x = 0; x<l.size(); x++) {
                    String str = l.get(x).toString();
                    if(!str.isEmpty()){aSelf.entry(this, str);}
                        
                }
                btnEquals.callOnClick();
            } else {
                clearC();
                //aSelf.onSaved(MainActivity.this, 1, llCalculation, tvDoneCalculation);
                
            }
            
             /*else {
                aSelf.setStandBy(true);
                
            }*/
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onBackPressed() {
        if(svScifra.getVisibility()==View.VISIBLE) {
            MediumAnims.scifraMenu(svScifra, false);
            return;
        }
        
        super.onBackPressed();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu cm, View v, ContextMenuInfo mi) {
        //((Menu)cm).add(21, 1, Menu.NONE, "Make StandBy Expression");
        for(int x=0; x<ASelf.strSigns().size();x++) {
            ((Menu)cm).add(41+x, 0+x, Menu.NONE, ASelf.strSigns().get(x));
        }
        
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem mi) {
        switch(mi.getGroupId()) {
            //case 11:showBTools(mi.getItemId()-1);
                //return true;
            case 41:case 42:case 43:case 44:case 45:case 46:
                aSelf.arr.set(aSelf.tvCInd, ASelf.strSigns().get(mi.getItemId()));
                tvCurrent.setText(aSelf.arr.get(aSelf.tvCInd));
                aSelf.arrAct(MainSimpleActivity.this);
                return true;
        }
        
        return false;
    }
    
    private void placeholders() {
        placeholders = true;
        aSelf.setStandBy(false);
        String exp = ASelf.getCalcSet(this).getString(getResources().getString(R.string.stand_by_example_default_expression_texttcn),"TextTCN_Stand_By_+Example_Default_Expression");
        if(exp.isEmpty()){exp="1+1-1×1÷1";}
        for(int s=0; s<exp.length(); s++) {
            aSelf.entry(MainSimpleActivity.this,exp.charAt(s)+"");
        }
        
        btnEquals.callOnClick();
            
        placeholders = false;
    }
    
    private void applySettings() {
        try {
            int dl = Integer.parseInt(ASelf.getCalcSet(this).getString(getResources().getString(R.string.decimal_count_textn), "1"));
            ASelf.mDF.setMaximumFractionDigits(dl);
            ASelf.mDF.setMinimumFractionDigits(dl);
            
            llDoneCalculation.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.answer_color),""))));
            tvDoneCalculation.setTextColor(ASelf.getCalcSet(this).getInt(getResources().getString(R.string.answer_textc),Color.BLACK));
            /*((LinearLayout)llEntry.getChildAt(3)).getChildAt(2).setBackgroundResource(R.drawable.bg_accent_sr);
            ((Button)((LinearLayout)llEntry.getChildAt(3)).getChildAt(2)).setTextColor(Color.WHITE);
            llLM.setBackgroundColor(ASelf.getCalcSet(this).getInt(getResources().getString(R.string.background_color),Color.WHITE));
            llEntry.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.background_entry_color),""))));
            llToolbar.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.background_toolbar_color),""))));
            aSelf.setEntryNumberColors(getApplicationContext(), DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.numbers_color),""))), ASelf.getCalcSet(this).getInt(getResources().getString(R.string.numbers_textc),0));
            aSelf.setSymbolColors(getApplicationContext(), DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.symbols_color),""))), ASelf.getCalcSet(this).getInt(getResources().getString(R.string.symbols_textc),0));
            
            hsvNC.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.calculation_now_color),""))));
            svC.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.calculation_details_color),""))));
            */
        } catch(Exception ex) {
            //Toast.makeText(this, ex.getMessage(),Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
    
    private void glBtns() {
        try {
            Drawable d = getResources().getDrawable(R.drawable.svg_history);
            d.setBounds(0,0,100,100);
            btnMHistory.setCompoundDrawables(
            d,null,null,null);
            //getResources().getDrawable(R.drawable.svg_history),
            //getResources().getDrawable(R.drawable.svg_history),
            //getResources().getDrawable(R.drawable.svg_history));
            btnMHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          CBMOptions.funHistory(MainSimpleActivity.this, new Object[]{llCalculation, tvDoneCalculation});
                        }
                    });
                    /*btnMASMD=((Button)findViewById(R.id.btnMASMD));
                    btnMASMD.setEnabled(false);
            btnMASMD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CBMOptions.funMADS(MainSimpleActivity.this, new Object[]{llCalculation, llNC, btnEquals, aSelf.calcNm, ad});
                    
                }});*/
               ((Button)findViewById(R.id.btnMPie)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                            CBMOptions.funPie(MainSimpleActivity.this, null);
    	                    }
                    });
                btnPercentage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(aSelf.isStandBy()){clearC();}
                            if(aSelf.arr.size()<1||(!aSelf.arr.get(aSelf.arr.size()-1).equals("%")))
                                {//int ci=0;try{ci=Integer.parseInt(tvCurrent.getTag().toString());}catch(Exception ex){ex.printStackTrace();}
                                //if(!aSelf.standBy&&ASelf.isNumber(ci>=0?aSelf.arr.get(ci):"1"))aSelf.entry(MainSimpleActivity.this,"%");
                                //if(ASelf.isNumber(aSelf.arr.get(aSelf.arr.size()-1)))
                                aSelf.entry(MainSimpleActivity.this,"%");
                        
                            }
                        }
            });
                ((Button)findViewById(R.id.btnMERt)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                            if(aSelf.arr.size()<1){aSelf.entry(MainSimpleActivity.this,"2");}
                            aSelf.entry(MainSimpleActivity.this,"√");
                            
                        }
                    });
                ((Button)findViewById(R.id.btnMPowr)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            //if(!aSelf.arr.get(aSelf.arr.size()-1).equals("√"))
                            if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                            if(aSelf.arr.size()<1){aSelf.entry(MainSimpleActivity.this,"1");}
                    aSelf.entry(MainSimpleActivity.this,"e");
                            
                        }
                    });
            ((Button)findViewById(R.id.btnMCos)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                    //aSelf.entry(MainSimpleActivity.this,"cos");
                        tvCurrent.setText("cos");
                        aSelf.arr.add("cos");
                        CurrentComponents.addCurrentTV(MainSimpleActivity.this,"");
                    
                        Toast.makeText(getApplicationContext(), "Cos", Toast.LENGTH_SHORT).show();
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((Button)findViewById(R.id.btnMSin)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                    aSelf.entry(MainSimpleActivity.this,"sin");
                        Toast.makeText(getApplicationContext(), "Sin", Toast.LENGTH_SHORT).show();
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((Button)findViewById(R.id.btnMTan)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    //if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                    //aSelf.entry(MainSimpleActivity.this,"tan");
                        Toast.makeText(getApplicationContext(), "Tan", Toast.LENGTH_SHORT).show();
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
    
    private void applyOnClick() {
        try {
            for(int a = 0; a < llEntry.getChildCount(); a++) {
                LinearLayout ll = ((LinearLayout)llEntry.getChildAt(a));
            
                for(int b = 0; b < ll.getChildCount(); b++) {
                    final int mb = b;
                    
                    ((Button)ll.getChildAt(b)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                             if(aSelf.mKeypadMode.equals(ASelf.KeypadModeOpt.Numbers)) {
                                 String str = ((Button)v).getText().toString();
                                 if(aSelf.isStandBy()) {
                                     clearC();
                                     aSelf.setStandBy(false);
                                 }
                                 hc=1;
                                 hca=true;
                                 aSelf.entry(MainSimpleActivity.this,str);
                                    //Toast.makeText(getApplicationContext(), aSelf.arr.toString(), Toast.LENGTH_SHORT).show();
                             } else if(aSelf.mKeypadMode.equals(ASelf.KeypadModeOpt.Selection)) {
                                    switch (mb) {
                                     case 0: //Copy, Paste, Options
                                         
                                         break;
                                     case 1: //Move Up
                                        
                                         break;
                                     case 2: //Close Mode
                                        
                                         break;
                                     case 3: //Move LeftgetStandBy()
                                         //tvCurrent.setSelection()
                                         break;
                                     case 4: //Select, Select All
                                        
                                         break;
                                     case 5: //Move Right
                                        
                                         break;
                                     case 6: //Plain
                                        
                                         break;
                                     case 7: //Move Down
                                        
                                         break;
                                     case 8: //Plain
                                        
                                         break;
                                 }
                             }
                        aSelf.arrAct(MainSimpleActivity.this);
                        } catch(Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                     }
                });
            }
               
                /*tvCurrent.addTextChangedListener(new TextWatcher() {
									@Override
										   public void beforeTextChanged(CharSequence t, int a, int b, int cc) {}
															
										   @Override
									     public void onTextChanged(CharSequence t, int a, int b, int cc) {
										       try {
                            if(aSelf.arr.size()<llNC.getChildCount()){//(((TextView)((MainSimpleActivity)c).llNC.getChildAt(((MainSimpleActivity)c).llNC.getChildCount()-1)).getText().toString().equals("=")) {
                                t="=";
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    			
										   @Override
									     public void afterTextChanged(Editable e) {}
            });*/
            }
            
                btnScifra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        MediumAnims.scifraMenu(svScifra, (svScifra.getAlpha()==1)?false:true);
                    }
                });
            
                btnSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                    
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    }
                });
            
            btnSignChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ASelf.isNumber(tvCurrent.getText().toString())) {
                        CBMOptions.funSignChange(MainSimpleActivity.this, new Object[]{tvCurrent});
                        aSelf.arrAct(MainSimpleActivity.this);
                    } else {v.showContextMenu();}
                    
                }
            });
            
        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(tvCurrent==null) return;
                    if(!aSelf.isStandBy()) {
                        String s = ((Button)v).getText().toString();
                    String tvc = tvCurrent.getText().toString();
                if(((Button)v).getText().toString().equals(".")) {
                    if(!isDotd) {
                        //tvCurrent.setText(aSelf.editNowValue(((Button)v).getText().toString()));
                        isDotd=true;
                        tvCurrent.setText(tvc+s);
                        aSelf.editNowValue(((Button)v).getText().toString());
                    } else if(tvCurrent.getText().toString().isEmpty() || aSelf.cTxt.isEmpty()) {
                        //tvCurrent.setText(tvCurrent.getText() + "0.");
                        tvCurrent.setText(tvc.isEmpty()?"0":tvc+s);
                        isDotd = true;
                        aSelf.editNowValue(((Button)v).getText().toString());
                    } else {
                            int i = Integer.parseInt(tvc.replace(".",""));
                        tvCurrent.setText(i+"");
                        isDotd = false;
                        aSelf.cTxt=i+"";
                        aSelf.editNowValue(i+"", true);
                    
                    }
                }
            }aSelf.arrAct(MainSimpleActivity.this);
                        }    
        });
                
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearC();
                if(aSelf.getSBEg(getApplicationContext())==1) placeholders();
    
            }
        });
            
            btnBackspacer.btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)	 {
                    try {
                        if(tvCurrent==null)return;
                        int value = Integer.parseInt(tvCurrent.getTag().toString());
                        if(!aSelf.isStandBy()&&(ASelf.isNumber(aSelf.arr.get(value))||aSelf.arr.get(value).isEmpty())) {
                            if(tvCurrent.getText().toString().length()>0) {
                                String str1 = tvCurrent.getText().toString();
                                aSelf.editNowValue("", true);
                                str1 = aSelf.editNowValue(str1.substring(0,str1.length()-1));
                                if(aSelf.arr.size()>value) {
                                    aSelf.arr.set(value, str1);
                                    tvCurrent.setText(str1);
                                }
                            } else {
                                if(llNC.getChildCount()>1&&(ASelf.isNumber(aSelf.arr.get(value))||aSelf.arr.get(value).isEmpty())&&!ASelf.isNumber(aSelf.arr.get(value-1))) {
                                    llNC.removeView((TextView)llNC.getChildAt(value));
                                    llNC.removeView((TextView)llNC.getChildAt(value-1));
                                    if(aSelf.arr.size()>value){
                                        aSelf.arr.remove(value);
                                        aSelf.arr.remove(value-1);
                                    }
                                    tvCurrent = (TextView)llNC.getChildAt(value-2);
                                    
                                }
                                for(int xx=0; xx<llNC.getChildCount(); xx++) {
                                    llNC.getChildAt(xx).setTag(xx);
                                }
                                aSelf.cTxt=tvCurrent.getText().toString();
                            }
                            
                            if(llNC.getChildCount()<=0){CurrentComponents.addCurrentTV(MainSimpleActivity.this, "");}
                            if(aSelf.cTxt.contains(".")) {
                                isDotd = true;
                            } else {
                                isDotd = false;
                            }
                        }
                        
                    aSelf.arrAct(MainSimpleActivity.this);
                    if(llNC.getChildCount()==1) {
                                    if(((TextView)llNC.getChildAt(0)).getText().toString().isEmpty()){btnRefresh.callOnClick()	;}
                                }
                        //Toast.makeText(getApplicationContext(), "tvCurrent="+tvCurrent.getText().toString(), Toast.LENGTH_SHORT).show();
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
               
                    }
                                
                }
            });
           
            btnBackspacer.btnBackspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)	 {
                    try {                        
                        if(tvCurrent==null)return false;
                        int value = Integer.parseInt(tvCurrent.getTag().toString());
                                if(tvCurrent.getText().toString().length()>0) {
                                aSelf.editNowValue("", true);
                                if(aSelf.arr.size()>value) {
                                    aSelf.arr.set(value, "");
                            tvCurrent.setText("");
                            }
                        }
                        aSelf.arrAct(MainSimpleActivity.this);
                        //Toast.makeText(getApplicationContext(), "Backspace Option", Toast.LENGTH_SHORT).show();
                   } catch(Exception ex){ex.printStackTrace();}
                return true;
            }});
        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(aSelf.arr.size()>0&&!aSelf.isStandBy()) {
                           //equate();
                    if(aSelf.cTxt.isEmpty()) {
                        aSelf.entry(MainSimpleActivity.this,"0");
                    }
                    
                    clearCTVS();
                    double ans = 0.0;//Double.parseDouble(aSelf.arr.get(0));
                    if(aSelf.arr.size()==1){if(ASelf.isNumber(aSelf.arr.get(0))){ans=Double.parseDouble(aSelf.arr.get(0));}}
                            else if(aSelf.arr.size()>1){if(ASelf.isNumber(aSelf.arr.get(1))){ans=Double.parseDouble(aSelf.arr.get(1));} else if(ASelf.isNumber(aSelf.arr.get(0))){ans=Double.parseDouble(aSelf.arr.get(0));}}
                            double ans2 = 0;
                    int cAns = 1, calcN=1;
                    String answ="", answ2="", answ3="";    
                    String currentSign = "!+";
                    String conjSign = "";
                    //Toast.makeText(getApplicationContext(), "= Calculate", Toast.LENGTH_LONG).show();
                    String standByPH = "Empty";
                            boolean expCont=false;
                            
                        if(aSelf.arrS.size()==0){expCont=true;
                                aSelf.addListItem(aSelf.arrS, new JSONObject().put("CalculationName","#"+calcN+" Calculation").put("Calculation",aSelf.arr));
                            aSelf.allTypeCalculations();//aSelf.addListItem(aSelf.arrS, new JSONObject().put("CalculationName",aSelf.getCurrentCalculationType().getString("name")+" Calculation").put("Calculation",Ut.toBodmas(aSelf.arr)));
                        }
                    CurrentComponents.tvCalcTitle(MainSimpleActivity.this, placeholders?standByPH:aSelf.calcNm);
                    for(int x = 0; x < aSelf.arr.size(); x++) {
                        ans2 = ans;
                        if(!ASelf.isNumber(aSelf.arr.get(x))) {
                            currentSign = aSelf.arr.get(x);
                            //ans = ans + Integer.parseInt(aSelf.arr.get(x+1));
                        } else {
                                    double anso = 0;
                            if(x>1)anso = Double.parseDouble(aSelf.arr.get(x));
                            ans = aSelf.calculate(currentSign, ans, anso);
                            
                                    String c = "";
                            if(x>=0) {
                               if(cAns>1)
                                    c = "(@"+(cAns-1)+")";
                                        
                                answ = (ans2+"").endsWith(".0")?Integer.parseInt((ans2+"").substring(0, (ans2+"").indexOf(".")))+"":(ans2+"");
                                answ2 = (ans+"").contains(".")?((ans+"").endsWith(".0")?Integer.parseInt((ans+"").replace(".0",""))+"":(ans+"")):(ans+"");
                                answ3 = aSelf.arr.get(x);
                                if(ASelf.getCalcSet(getApplicationContext()).getString(getResources().getString(R.string.decimal_always_switch), "Switch_Numbers_+Decimal_Always").equals("On")) {
                                    answ = ASelf.mDF.format(Double.parseDouble(ans2+""));
                                    answ2 = ASelf.mDF.format(Double.parseDouble(ans+""));
                                    answ3 = ASelf.mDF.format(Double.parseDouble(aSelf.arr.get(x)));
                                }
                                if(x>0){
                                    CurrentComponents.addCalculatedTV(MainSimpleActivity.this, new JSONObject().put(ASelf.Calculation.Ix.toString(),cAns+"").put(ASelf.Calculation.LmtIx.toString(),cAns+"").put(ASelf.Calculation.Exp.toString(),answ+currentSign+answ3+"=").put(ASelf.Calculation.ExpAns.toString(),answ2).put(ASelf.Calculation.DoneCalc.toString(),answ2));
                                cAns+=1;}
                            }
                        }
                    }
                        
                       //String answ = String.valueOf(ans).endsWith(".0")?String.valueOf(Integer.parseInt(ans+"")+""):String.valueOf(ans);
                        if(ASelf.getCalcSet(getApplicationContext()).getString(getResources().getString(R.string.decimal_always_switch),"").equals("On")) {
                                    //answ2 = ans+"";
                        answ2 = ASelf.mDF.format(Double.parseDouble(ans+""));
                                    }
                        if(currentSign.equals("%"))answ=Double.parseDouble((ans)+"")+"("+(ans*100)+"%)";
                        tvDoneCalculation.setAlpha(placeholders?0.5f:1f);
                        tvDoneCalculation.setText(answ2);
                        //tvNowCalculation.setText(tvNowCalculation.getText()+"=");
                        if(!((TextView)llNC.getChildAt(llNC.getChildCount()-1)).getText().toString().equals("=")){
                                CurrentComponents.addCurrentTV(MainSimpleActivity.this, "=", 1);}
                        //aSelf.arr.add("=");
                        btnSignChange.setEnabled(false);
                            //if(Ut.containsMads(aSelf.arr.toString()))btnMASMD.setEnabled(true);
                        if((cAns-1)<1){cAns=1;} else if(cAns>=llCalculation.getChildCount()){cAns=cAns-1;}
                        CurrentComponents.addCalculatedTV(MainSimpleActivity.this, new JSONObject().put(ASelf.Calculation.Ix.toString(),"FA")
                            .put(ASelf.Calculation.LmtIx.toString(),cAns+"")
                            .put(ASelf.Calculation.Exp.toString(),"Final Answer: @"+cAns+"=")
                            .put(ASelf.Calculation.ExpAns.toString(),answ2)
                            .put(ASelf.Calculation.DoneCalc.toString(),answ2)
                            .put(ASelf.Calculation.dType.toString(), 1)); 
                        //new String[]{"FA", "Final Answer: @"+cAns+"=", ans+""}), 1);
                        if(llCalculation.getChildCount()>0){
                            if(tvDoneCalculation.getAlpha()!=1)
                            tvDoneCalculation.setText(tvDoneCalculation.getText().toString() + " | Write an expression");
                            standByPH+=" +Example";
                            
                        }
                    
                        //tvCTitle.setText(tvDoneCalculation.getAlpha()==1?"Calculation "+calcN:standByPH);
                        tvCTitle.setText(tvDoneCalculation.getAlpha()==1?aSelf.calcNm:standByPH);
                        btnPercentage.setEnabled(false);
                        aSelf.setStandBy(true);
                        isDotd=false;
                        //aSelf.aSelf.editNowValue("", true);
                        aSelf.setHCB(MainSimpleActivity.this, false);
                        //if(hca&&expCont){
                            //onSave(getHC()+1);
                            if(!placeholders) {
                                ASelf.phOn=false;
                                aSelf.onSave(MainSimpleActivity.this, aSelf.getHC(MainSimpleActivity.this)+1);
                            }
                        //}
                    }
                } catch(Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
            });
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
                
        }
    }

    public void clearC() {
        llNC.removeAllViews();
        llCalculation.removeAllViews();
        hsvNC.smoothScrollTo(0,0);
        svC.smoothScrollTo(0,0);
        CurrentComponents.addCurrentTV(this, "");
        currIndex = 0;
        aSelf.arr.clear();
        aSelf.arrS.clear();
        //btnMASMD.setEnabled(false);
        btnSignChange.setEnabled(false);
        clearCTVS();
        aSelf.setStandBy(true);
        aSelf.calcNm="#1 Calculation";
        tvDoneCalculation.setAlpha(aSelf.isStandBy()?0.5f:1f);
        tvDoneCalculation.setText(aSelf.isStandBy()?"Write an expression":"");
        btnPercentage.setEnabled(false);
    }
    
    public void clearCTVS() {
        for(int x = 0; x < llNC.getChildCount(); x++) {
            ((TextView)llNC.getChildAt(x)).setSelected(false);
            ((TextView)llNC.getChildAt(x)).setTextColor(Color.WHITE);
        }
        
        for(int x = 1; x < llCalculation.getChildCount(); x++) {
            ((AnswerListItem)llCalculation.getChildAt(x)).setSelected(false);
            //((AnswerListItem)llCalculation.getChildAt(x)).tvAnsCalc.setTextColor(Color.WHITE);
            //((AnswerListItem)llCalculation.getChildAt(x)).tvAnsNr.setTextColor(Color.WHITE);
        }
        
        Drawable tvD = getResources().getDrawable(R.drawable.bg_blk_green_sr, null);
        int tvClr = Color.WHITE;
        if(aSelf.arr.size()>0){
        if(((TextView)llNC.getChildAt(llNC.getChildCount()-1)).getText().toString().equals("=")) {
            ((TextView)llNC.getChildAt(llNC.getChildCount()-1)).setBackground(getResources().getDrawable(R.drawable.bg_accent_c, null));
            ((TextView)llNC.getChildAt(llNC.getChildCount()-1)).setTextColor(Color.WHITE);
        }
        }                    
    }
}
