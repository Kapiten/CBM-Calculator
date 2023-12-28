package com.cbm.android.cbmcalculator;

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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbm.android.cbmcalculator.R;
import com.cbm.android.cbmcalculator.anim.*;
import com.cbm.android.cbmcalculator.custom.*;
import com.cbm.android.cbmcalculator.extended.*;
import com.cbm.android.cbmcalculator.settings.*;
import com.cbm.android.cbmcalculator.utility.*;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ASelf aSelf;
    private AlertDialog ad;
    public Button btnSignChange, btnEquals, btnPoint, btnMASMD, btnPercentage, btnHistory, btnSettings;
    
    public ImageButton btnBackspace, btnRefresh, btnScifra;
    public TextView tvCurrent, tvDoneCalculation, tvCTitle;
    public LinearLayout llToolbar, llEntry, llCalculation, llNC, llLM, llScifra;
    public HorizontalScrollView hsvNC, llDoneCalculation;
    public ScrollView svC, svScifra;
    
    private int currIndex = 0, cSI = 0, hc=0, bracketOpen=0, bracketIndex=0; //current Selected Index
    private BigDecimal bracketAnsw = new BigDecimal(0);
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
            llDoneCalculation = (HorizontalScrollView) this.findViewById(R.id.llDoneCalculation);
            tvDoneCalculation = (TextView)this.findViewById(R.id.tvDoneCalculation);
            //tvNow = this.findViewById(R.id.etNow);
            btnSettings = (Button)this.findViewById(R.id.btnMSettings);
            btnMASMD = (Button)this.findViewById(R.id.btnMASMD);
            btnScifra = (ImageButton)this.findViewById(R.id.btnScifra);
            btnSignChange = (Button)this.findViewById(R.id.btnMInverse);
            btnPercentage = ((Button)this.findViewById(R.id.btnMPercentage));
            //tvCalculated = (TextView)LayoutInflater.from(this).inflate(R.layout.calculated_text, null);
            hsvNC = (HorizontalScrollView)this.findViewById(R.id.hsvNowCalculation);
            svC = (ScrollView)this.findViewById(R.id.svCalculation);
            btnHistory = (Button)this.findViewById(R.id.btnMHistory);
            btnRefresh =(ImageButton) this.findViewById(R.id.btnRefresh);
            //btnEquals = (Button)this.findViewById(R.id.btnEquals);
            //btnPoint = (Button)this.findViewById(R.id.btnPoint);
            btnBackspace =(ImageButton) this.findViewById(R.id.btnBackspace);
            llLM = ((LinearLayout)this.findViewById(R.id.llLM));
            llEntry = (LinearLayout) this.findViewById(R.id.llEntry);
            llToolbar = (LinearLayout)this.findViewById(R.id.llToolbar);
            svScifra = (ScrollView)this.findViewById(R.id.svScifra);
            llScifra = (LinearLayout)this.findViewById(R.id.llScifra);
            llCalculation = (LinearLayout)this.findViewById(R.id.llCalculation);
            llNC = (LinearLayout)this.findViewById(R.id.llNC);
            tvDoneCalculation.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
//            llNC.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//                        int screenH = getWindowManager().getMaximumWindowMetrics().getBounds().height();
//                        int appWH = getWindow().getDecorView().getHeight();
//
//                        if(appWH<(Float.parseFloat(screenH+"")/2f)) {
////                            llNC.
//                            Toast.makeText(MainActivity.this, "CBMCalc Height less than half of screen", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });
            llScifra.setFocusable(true);
            llScifra.setClickable(true);
            aSelf.setStandBy(true);
            MediumAnims.scifraMenu(svScifra, false);
            initEntry();
            applyOnClick();
            glBtns();
            hc = 0;
            hca=false;
            clearC();
            registerForContextMenu(btnSignChange);
            applySettings();
            
            Drawable dHistory = getResources().getDrawable(R.drawable.svg_history);
            //Drawable dOptions = getResources().getDrawable(R.drawable.svg_options);
            Drawable dSettings = getResources().getDrawable(R.drawable.svg_settings);
            
            dHistory.setBounds(0,0,100,100);
            //dOptions.setBounds(0,0,100,100);
            dSettings.setBounds(0,0,100,100);
            
            btnSettings.setCompoundDrawables(dSettings,null,null,null);
            //btnScifra.setCompoundDrawables(dOptions,null,null,null);
            btnHistory.setCompoundDrawables(dHistory,null,null,null);
            
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
            if(!sp.getString("RecentExp","").isEmpty()) {
                aSelf.setStandBy(false);
                //aSelf.phOn = false;
                List<String> l = (List<String>)Ut.toArray(sp.getString("RecentExp", ""));
                for(int x = 0; x<l.size(); x++) {
                    String str = l.get(x).toString();
                    if(!str.isEmpty()){aSelf.entry(this, str);}

                }
                sp.edit().putString("RecentExp","").apply();
                //btnEquals.callOnClick();
            } else if(sp.getString(getResources().getString(R.string.stand_by_example_switch), "On").equals("On")) {
                if(aSelf.getSBEg(this)==1)placeholders();
            } else {
                clearC();
                //aSelf.onSaved(MainActivity.this, 1, llCalculation, tvDoneCalculation);
                
            }
            
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
                aSelf.arrAct(MainActivity.this);
                return true;
        }
        
        return false;
    }
/*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //aSelf = ASelf.get(this);
        aSelf.setStandBy(false);
        clearC();
        String exp = savedInstanceState.getString("exp", "");
        for(int s=0; s<exp.length(); s++) {
            aSelf.entry(this,exp.charAt(s)+"");
        }

        Toast.makeText(this, "exp: "+exp+" arr:"+aSelf.arr.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String exp ="";
        for(int s=0; s<aSelf.arr.size(); s++) {
            exp+=aSelf.arr.get(s);
        }
        outState.putString("exp", exp);
        super.onSaveInstanceState(outState);

        Toast.makeText(this, aSelf.arr.toString(), Toast.LENGTH_LONG).show();
    }*/

    private void placeholders() {
        placeholders = true;
        aSelf.setStandBy(false);
        String exp = ASelf.getCalcSet(this).getString(getResources().getString(R.string.stand_by_example_default_expression_texttcn),"TextTCN_Stand_By_+Example_Default_Expression");
        if(exp.isEmpty()){exp="1+1-1×1÷1";}
        for(int s=0; s<exp.length(); s++) {
            aSelf.entry(MainActivity.this,exp.charAt(s)+"");
        }
        
        btnEquals.callOnClick();
            
        placeholders = false;
        aSelf.phOn=true;
    }
    
    private void applySettings() {
        try {
            if(!ASelf.getCalcSet(this).contains(getResources().getString(R.string.stand_by_example_default_expression_texttcn))) {
                SettingsActivity.resetSettings(this);
            }
            int dl = Integer.parseInt(ASelf.getCalcSet(this).getString(getResources().getString(R.string.decimal_count_textn), "1"));
            ASelf.mDF.setMaximumFractionDigits(dl);
            ASelf.mDF.setMinimumFractionDigits(dl);
            ASelf.carryAnswer = ASelf.getCalcSet(this).getString(getResources().getString(R.string.history_carry_answer_switch), "Off").equals("On");
            /*llDoneCalculation.setBackground(DefaultThemeSet.mkShape(new JSONObject(ASelf.getCalcSet(this).getString(getResources().getString(R.string.answer_color),""))));
            tvDoneCalculation.setTextColor(ASelf.getCalcSet(this).getInt(getResources().getString(R.string.answer_textc),Color.BLACK));
            ((LinearLayout)llEntry.getChildAt(3)).getChildAt(2).setBackgroundResource(R.drawable.bg_accent_sr);
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
            btnHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CBMOptions.funHistory(MainActivity.this, new Object[]{llCalculation, tvDoneCalculation});
                    }});
                    //btnMASMD=((Button)findViewById(R.id.btnMASMD));
                    //btnMASMD.setEnabled(false);
            btnMASMD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*if (aSelf.arr.size() > 0&&aSelf.isStandBy())
                        CBMOptions.funMADS(MainActivity.this, new Object[]{llCalculation, llNC, btnEquals, aSelf.calcNm, ad});
                        */
                        try {
                            if (aSelf.arr.size() > 0&&aSelf.isStandBy()) {
                                btnMASMD.setSelected(!btnMASMD.isSelected());
                                if (btnMASMD.isSelected()) {tvDoneCalculation.setText(Ut.toBodmas(aSelf.arr).get(0));}
                                else {tvDoneCalculation.setText(ASelf.calculation(aSelf.arr)[1]);}
                            }
                        } catch(Exception ex) {Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();}
                }});
               ((Button)findViewById(R.id.btnMPie)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                            CBMOptions.funPie(MainActivity.this, null);
                        }
                    });
                btnPercentage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(aSelf.isStandBy()){clearC();}
                            if(aSelf.arr.size()<1||(!aSelf.arr.get(aSelf.arr.size()-1).equals("%")))
                            {aSelf.entry(MainActivity.this,"%");}
                        }
            });
            btnPercentage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(aSelf.isStandBy()){clearC();}
                    if(aSelf.arr.size()<1||(!aSelf.arr.get(aSelf.arr.size()-1).equals("%")))
                    {aSelf.entry(MainActivity.this,"%");}
                }
            });
                ((Button)findViewById(R.id.btnMERt)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                            if(aSelf.arr.size()<1){aSelf.entry(MainActivity.this,"2");}
                            aSelf.entry(MainActivity.this,"√");
                            
                        }
                    });
                ((Button)findViewById(R.id.btnMPowr)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            //if(!aSelf.arr.get(aSelf.arr.size()-1).equals("√"))
                            if(aSelf.isStandBy()){clearC(); aSelf.setStandBy(false);}
                            if(aSelf.arr.size()<1){aSelf.entry(MainActivity.this,"1");}
                    aSelf.entry(MainActivity.this,"e");
                            
                        }
                    });
            
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
    
    private void applyOnClick() {
        try {
           /* for(int a = 0; a < llEntry.getChildCount(); a++) {
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
                                     if(ASelf.carryAnswer&&!ASelf.isNumber(str)){
                                         if(!aSelf.carriedAnswer.isEmpty()&&ASelf.isNumber(aSelf.carriedAnswer)) {
                                             aSelf.entry(MainActivity.this,aSelf.carriedAnswer);
                                         }
                                     }
                                 }
                                 hc=1;
                                 hca=true;
                                 aSelf.entry(MainActivity.this,str);
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
                        aSelf.arrAct(MainActivity.this);
                        } catch(Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                     }
                });
            }
               
                tvCurrent.addTextChangedListener(new TextWatcher() {
									@Override
										   public void beforeTextChanged(CharSequence t, int a, int b, int cc) {}
															
										   @Override
									     public void onTextChanged(CharSequence t, int a, int b, int cc) {
										       try {
                            if(aSelf.arr.size()<llNC.getChildCount()){//(((TextView)((MainActivity)c).llNC.getChildAt(((MainActivity)c).llNC.getChildCount()-1)).getText().toString().equals("=")) {
                                t="=";
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    			
										   @Override
									     public void afterTextChanged(Editable e) {}
            });
            }*/
            
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
                        CBMOptions.funSignChange(MainActivity.this, new Object[]{tvCurrent});
                        aSelf.arrAct(MainActivity.this);
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
            }aSelf.arrAct(MainActivity.this);
                        }    
        });
                
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearC();
                if(aSelf.getSBEg(getApplicationContext())==1) placeholders();
                aSelf.carriedAnswer="";
    
            }
        });
            
            btnBackspace.setOnClickListener(new View.OnClickListener() {
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
                                        //aSelf.arr.remove(value);
                                        aSelf.arr.remove(value);
                                        aSelf.arr.remove(value-1);
                                    }
                                    tvCurrent = (TextView)llNC.getChildAt(value-2);
                                    
                                }
                                for(int xx=0; xx<llNC.getChildCount(); xx++) {
                                    llNC.getChildAt(xx).setTag(xx);
                                }
                                aSelf.cTxt=tvCurrent.getText().toString();
                                //tvCurrent.callOnClick();
                            }
                            
                            if(llNC.getChildCount()<=0){CurrentComponents.addCurrentTV(MainActivity.this, "");}
                            if(aSelf.cTxt.contains(".")) {
                                isDotd = true;
                            } else {
                                isDotd = false;
                            }
                        }
                            aSelf.arrAct(MainActivity.this);
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
           
            btnBackspace.setOnLongClickListener(new View.OnLongClickListener() {
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
                        aSelf.arrAct(MainActivity.this);
                        if(llNC.getChildCount()==1) {
                                    if(((TextView)llNC.getChildAt(0)).getText().toString().isEmpty()){btnRefresh.callOnClick();}
                                }
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
                        aSelf.entry(MainActivity.this,"0");
                    }
                    
                    clearCTVS();
                    llCalculation.removeAllViews();
                    aSelf.setHCB(false);
                    BigDecimal ans = new BigDecimal(aSelf.arr.get(0));
                    BigDecimal ans2 = new BigDecimal(0);
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
                    CurrentComponents.tvCalcTitle(MainActivity.this, placeholders?standByPH:aSelf.calcNm);
                    for(int x = 0; x < aSelf.arr.size(); x++) {
                        ans2 = ans;
                        if(!ASelf.isNumber(aSelf.arr.get(x))) {
                            currentSign = aSelf.arr.get(x);
                            //ans = ans + Integer.parseInt(aSelf.arr.get(x+1));
                        } else {
                            BigDecimal anso = new BigDecimal(0);
                            if(x>1)anso = new BigDecimal(aSelf.arr.get(x));
                            ans = ASelf.calculate(currentSign, ans, anso);
                            
                            String c = "";
                            if(x>=0) {
                               if(cAns>1)c = "(@"+(cAns-1)+")";
                                        
                                answ = (ans2+"").endsWith(".0")?Integer.parseInt((ans2+"").substring(0, (ans2+"").indexOf(".")))+"":(ans2+"");
                                answ2 = (ans+"").contains(".")?((ans+"").endsWith(".0")?Integer.parseInt((ans+"").replace(".0",""))+"":(ans+"")):(ans+"");
                                answ3 = aSelf.arr.get(x);
                                if(ASelf.getCalcSet(getApplicationContext()).getString(getResources().getString(R.string.decimal_always_switch), "Switch_Numbers_+Decimal_Always").equals("On")) {
                                    answ = ASelf.mDF.format(new BigDecimal(ans2+""));
                                    answ2 = ASelf.mDF.format(new BigDecimal(ans+""));
                                    answ3 = ASelf.mDF.format(new BigDecimal(aSelf.arr.get(x)));
                                }
                                if(x>0) {
                                    CurrentComponents.addCalculatedTV(MainActivity.this, new JSONObject()
                                            .put(ASelf.Calculation.Ix.toString(),cAns+"")
                                            .put(ASelf.Calculation.LmtIx.toString(),cAns+"")
                                            .put(ASelf.Calculation.Exp.toString(),answ+currentSign+answ3+"=")
                                            .put(ASelf.Calculation.ExpAns.toString(),answ2)
                                            .put(ASelf.Calculation.DoneCalc.toString(),answ2)
                                            .put(getResources().getString(R.string.answer_for), new JSONArray().put(x-2).put(x-1).put(x).toString()));
                                cAns+=1;}
                            }
                        }
                    }
                        
                       //String answ = String.valueOf(ans).endsWith(".0")?String.valueOf(Integer.parseInt(ans+"")+""):String.valueOf(ans);
                        if(ASelf.getCalcSet(getApplicationContext()).getString(getResources().getString(R.string.decimal_always_switch),"").equals("On")) {
                                    //answ2 = ans+"";
                        answ2 = ASelf.mDF.format(ans);
                                    }
                        if(currentSign.equals("%"))answ=new BigDecimal((ans)+"")+"("+(ans.multiply(new BigDecimal(100)))+"%)";
                        tvDoneCalculation.setAlpha(placeholders?0.5f:1f);
                        tvDoneCalculation.setText(answ2);
                        if(ASelf.carryAnswer)aSelf.carriedAnswer=answ2;
                        //tvNowCalculation.setText(tvNowCalculation.getText()+"=");
                        if(!((TextView)llNC.getChildAt(llNC.getChildCount()-1)).getText().toString().equals("=")){
                                CurrentComponents.addCurrentTV(MainActivity.this, "=", 1);}
                        //aSelf.arr.add("=");
                        btnSignChange.setEnabled(false);
                            //if(Ut.containsMads(aSelf.arr.toString()))btnMASMD.setEnabled(true);
                        if((cAns-1)<1){cAns=1;} else if(cAns>=llCalculation.getChildCount()){cAns=cAns-1;}
                        CurrentComponents.addCalculatedTV(MainActivity.this, new JSONObject().put(ASelf.Calculation.Ix.toString(),"FA")
                            .put(ASelf.Calculation.LmtIx.toString(),cAns+"")
                            .put(ASelf.Calculation.Exp.toString(),"Final Answer: @"+cAns+"=")
                            .put(ASelf.Calculation.ExpAns.toString(),answ2)
                            .put(ASelf.Calculation.DoneCalc.toString(),answ2)
                            .put(ASelf.Calculation.dType.toString(), 1)
                            .put(getResources().getString(R.string.answer_for), new JSONArray().put(aSelf.arr.size()).toString()));
                        //new String[]{"FA", "Final Answer: @"+cAns+"=", ans+""}), 1);
                        if(llCalculation.getChildCount()>0){
                            if(tvDoneCalculation.getAlpha()!=1) {tvDoneCalculation.setText(tvDoneCalculation.getText().toString() + " | Write an expression");}
                            standByPH+=" +Example";
                            
                        }
                    
                        //tvCTitle.setText(tvDoneCalculation.getAlpha()==1?"Calculation "+calcN:standByPH);
                        tvCTitle.setText(tvDoneCalculation.getAlpha()==1?aSelf.calcNm:standByPH);
                        btnPercentage.setEnabled(false);
                        aSelf.setStandBy(true);
                        isDotd=false;
                        //aSelf.aSelf.editNowValue("", true);
                        //aSelf.setHCB(MainActivity.this, false);
                        //if(expCont){
                            //onSave(getHC()+1);
                            if(!placeholders) {
                                ASelf.phOn=false;
                                aSelf.onSave(MainActivity.this, aSelf.getHC(MainActivity.this)+1);
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

    private Button createEntry(String title, int type) {
        Button btn = new Button(this);
        btn.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        int dim4dp = Math.round(getResources().getDimension(R.dimen.dim4dp));
        lp.setMargins(dim4dp,dim4dp,dim4dp,dim4dp);
        lp.weight=1;
        btn.setLayoutParams(lp);
        btn.setText(title);
        btn.setTextSize(22f);
        if(type==0){btn.setBackground(getResources().getDrawable(R.drawable.bg_white_green_r));btn.setTextColor(Color.BLACK);}
        else if(type==1){btn.setBackground(getResources().getDrawable(R.drawable.bg_black_blue_sr));btn.setTextColor(Color.WHITE);}

        return btn;
    }

    private void initEntry() {
        String mads = getResources().getString(R.string.MADS);
        for(String text: mads.split(",")) {
            Button btn = createEntry(text, 1);
            btn.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            int dim4dp = Math.round(getResources().getDimension(R.dimen.dim4dp));
            lp.setMargins(dim4dp,dim4dp,dim4dp,dim4dp);
            lp.weight=1;
            btn.setLayoutParams(lp);
            btn.setOnClickListener(onEntryClick());
            ((LinearLayout)llEntry.getChildAt(1)).addView(btn);
        }
        int eRow = 2;
        int eColumn=3;
        for(int n=-2;n<=9;n++) {
            if(n<1) {
                if(n==-2){
                    btnPoint = createEntry(".", 0);
                    btnPoint.setBackground(getResources().getDrawable(R.drawable.bg_black_blue_sr));btnPoint.setTextColor(Color.WHITE);
                    ((LinearLayout)((LinearLayout)llEntry.getChildAt(0)).getChildAt(3)).addView(btnPoint);}
                if(n==-1){
                    Button btn = createEntry("0", 0);
                    btn.setOnClickListener(onEntryClick());
                    ((LinearLayout)((LinearLayout)llEntry.getChildAt(0)).getChildAt(3)).addView(btn);
                }
                if(n==0){
                    btnEquals = createEntry("=", 0);
                    btnEquals.setBackground(getResources().getDrawable(R.drawable.bg_accent_sr));btnEquals.setTextColor(Color.WHITE);
                    ((LinearLayout)((LinearLayout)llEntry.getChildAt(0)).getChildAt(3)).addView(btnEquals);
                }
            } else {
                eColumn-=1;
                Button btn = createEntry(String.valueOf(n), 0);
                btn.setOnClickListener(onEntryClick());
                ((LinearLayout)((LinearLayout)llEntry.getChildAt(0)).getChildAt(eRow)).addView(btn);

                if(eColumn==0){eRow-=1;eColumn=3;}
            }
        }
    }

    private View.OnClickListener onEntryClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = ((Button)v).getText().toString();
                    if(aSelf.isStandBy()) {
                        clearC();
                        aSelf.setStandBy(false);
                        if(ASelf.carryAnswer&&!ASelf.isNumber(str)){
                            if(!aSelf.carriedAnswer.isEmpty()&&ASelf.isNumber(aSelf.carriedAnswer)) {
                                aSelf.entry(MainActivity.this,aSelf.carriedAnswer);
                            }
                        }
                    }
                    hc=1;
                    hca=true;
                    aSelf.entry(MainActivity.this,str);
                    aSelf.arrAct(MainActivity.this);
                } catch(Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void clearCalculation(){llCalculation.removeAllViews();
        tvDoneCalculation.setAlpha(0.5f);}

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
        btnMASMD.setSelected(false);
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
