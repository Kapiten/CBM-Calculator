package com.cbm.android.cbmcalculator.extended;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.cbmcalculator.ASelf;
import com.cbm.android.cbmcalculator.MainActivity;
import com.cbm.android.cbmcalculator.R;
import com.cbm.android.cbmcalculator.custom.AnswerListItem;
import com.cbm.android.cbmcalculator.utility.Ut;

import org.json.JSONObject;

public class CurrentComponents {
    
    public static TextView addCurrentTV(final Context c, String txt, int val) {
        return addCurrentTV(c, ((MainActivity)c).llNC.getChildCount(), txt, val);
    }
    
    public static TextView addCurrentTV(final Context c, final int indx, final String txt, final int val) {
        final TextView tv = new TextView(c);
        try{
        ASelf.get(c).isDotd=false;
        
        final LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tv.setPadding(10,10,10,10);
        tv.setLayoutParams(lp);
        
        Drawable d = c.getResources().getDrawable(R.drawable.bg_blk_green_sr, null);
        int clr = Color.WHITE;
        
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20f);
        ((MainActivity)c).tvCurrent = tv;
        try {
                ViewTreeObserver viewTreeObserver = tv.getViewTreeObserver();
        if(viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                   if(val!=0) {
                       tv.setWidth(tv.getHeight());
                       tv.setPadding(0,0,1,1);
                
                                }} catch(Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
        });}
        } catch(Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
            
            if(val==1) {
                d=c.getResources().getDrawable(R.drawable.bg_accent_c, null);
            } else if(val==2) {
                d=c.getResources().getDrawable(R.drawable.bg_blk_green_c, null);
            }
            tv.setTextSize(18f);
            tv.setFocusable(false);
            tv.setTextIsSelectable(false);
                    
        tv.setAlpha(((MainActivity)c).placeholders?0.7f:1f);
        tv.setBackground(d);
        tv.setTextColor(clr);
        tv.setInputType(InputType.TYPE_CLASS_NUMBER);
        tv.setText(ASelf.get(c).editNowValue(txt, true));
        tv.setTag(indx>=0?indx+"":((MainActivity)c).llNC.getChildCount() + "");
        tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                ((MainActivity)c).tvCurrent = (TextView)v;
                ASelf.get(c).cTxt = ((MainActivity)c).tvCurrent.getText().toString();
                ASelf.get(c).tvCInd=Integer.parseInt(((MainActivity)c).tvCurrent.getTag().toString());
                ((MainActivity)c).clearCTVS();
                    
                //Drawable d = getResources().getDrawable(R.drawable.bg_blk_green_sr, null);
                //int clr = Color.WHITE;
                int calcCount = ((MainActivity)c).llNC.getChildCount()==0?0:((MainActivity)c).llNC.getChildCount()-1;
                
                ((MainActivity)c).tvCurrent.setSelected(true);
                if(((TextView)((MainActivity)c).llNC.getChildAt(calcCount)).getText().toString().equals("=")) {
                  if(Integer.parseInt(tv.getTag()+"")<3) {
                    if(ASelf.get(c).arr.size()==1){((MainActivity)c).llNC.getChildAt(0).setSelected(v.isSelected());((TextView)((MainActivity)c).llNC.getChildAt(0)).setTextColor(Color.WHITE);}
                    ((MainActivity)c).llNC.getChildAt(0).setSelected(true);
                    ((MainActivity)c).llNC.getChildAt(1).setSelected(true);
                    ((MainActivity)c).llNC.getChildAt(2).setSelected(true);
                    ((TextView)((MainActivity)c).llNC.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView)((MainActivity)c).llNC.getChildAt(1)).setTextColor(Color.WHITE);
                    ((TextView)((MainActivity)c).llNC.getChildAt(2)).setTextColor(Color.WHITE);
                } else if((Integer.parseInt(tv.getTag()+""))<((MainActivity)c).llNC.getChildCount()-1) {
                    if((Double.parseDouble(tv.getTag()+"")/2+"").endsWith(".0")) {
                        ((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")-2).setSelected(true);
                        ((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")-1).setSelected(true);
                        ((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")).setSelected(true);
                        ((TextView)((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")-2)).setTextColor(Color.WHITE);
                        ((TextView)((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")-1)).setTextColor(Color.WHITE);
                        ((TextView)((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+""))).setTextColor(Color.WHITE);
                    } else {
                        ((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")-1).setSelected(v.isSelected());
                        ((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")).setSelected(v.isSelected());
                        ((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")+1).setSelected(v.isSelected());
                        ((TextView)((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")-1)).setTextColor(Color.WHITE);
                        ((TextView)((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+""))).setTextColor(Color.WHITE);
                        ((TextView)((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag()+"")+1)).setTextColor(Color.WHITE);
                        //d = getResources().getDrawable(R.drawable.bg_blk_green_c, null);
                    }
                }
                        } 
                    //tvCurrent.setBackground(d);
                    //tvCurrent.setTextColor(clr);
                    //int arrIInd=0;
                    //if(arr.size()-1>=1){arrIInd=arr.size()-1;}else{arrIInd=0;}
                    if(((TextView)((MainActivity)c).llNC.getChildAt(calcCount)).getText().toString().equals("=")) {
                        ((TextView)((MainActivity)c).llNC.getChildAt(ASelf.get(c).arr.size())).setBackground(c.getResources().getDrawable(R.drawable.bg_accent_c, null));
                        ((TextView)((MainActivity)c).llNC.getChildAt(ASelf.get(c).arr.size())).setTextColor(Color.WHITE);
                    
                        double v1 = (Double.parseDouble(tv.getTag()+"")/2.0)+0.5;
                        v1=(v1+"").endsWith(".5")?v1-0.5:v1;
                        int v2 = Integer.parseInt((v1+"").replace(".0",""));
                        if(v2-1<0){v2=0;}else{v2=v2-1;}
                        v2+=1;
                        ((AnswerListItem)((MainActivity)c).llCalculation.getChildAt(v2)).setSelected(true);
                        if(((MainActivity)c).llCalculation.getChildCount()-1>=v2) {
                            ((AnswerListItem)((MainActivity)c).llCalculation.getChildAt(v2)).tvAnsNr.setTextColor(Color.BLACK);
                            ((AnswerListItem)((MainActivity)c).llCalculation.getChildAt(v2)).tvAnsCalc.setTextColor(Color.BLACK);
                        }
                            
                        try {
                            int scrollTo=0;
                            scrollTo = Math.round(((MainActivity)c).llNC.getChildAt(Integer.parseInt(tv.getTag().toString())-2).getX());
                            ((MainActivity)c).hsvNC.smoothScrollTo(scrollTo,0);
                            //svC.smoothScrollTo(0,((int)llCalculation.getChildAt(v2).getHeight() * v2)-tvCTitle.getHeight());
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                        ((MainActivity)c).svC.smoothScrollTo(0,Math.round(((MainActivity)c).llCalculation.getChildAt(v2).getY()));
                     }
                	} catch(Exception ex) {
                    ex.printStackTrace();
                            //Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
          /*tv.addTextChangedListener(new TextWatcher() {
									String txtb="";
									@Override
										   public void beforeTextChanged(CharSequence t, int a, int b, int cc) {
																		txtb=t.toString();
											 }
															
										   @Override
									     public void onTextChanged(CharSequence t, int a, int b, int cc) {
										       try {
                            if(ASelf.get(c).arr.size()<((MainActivity)c).llNC.getChildCount()){//(((TextView)((MainActivity)c).llNC.getChildAt(((MainActivity)c).llNC.getChildCount()-1)).getText().toString().equals("=")) {
                                t="=";
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    			
										   @Override
									     public void afterTextChanged(Editable e) {
																		
											  }
                });*/
        ((MainActivity)c).llNC.addView(tv, indx);
        
        //hsvNC.smoothScrollTo(llNC.getChildAt(Integer.parseInt(tv.getTag()+"")).getWidth()*llNC.getChildCount(),0);
        //Toast.makeText(getApplicationContext(), "ll = " + llNC.getChildAt(Integer.parseInt(tv.getTag()+"")).getWidth()+ ", llCC = " + llNC.getChildAt(Integer.parseInt(tv.getTag()+"")).getLayoutParams().width, Toast.LENGTH_SHORT).show();
        } catch(Exception ex) {
            ex.printStackTrace();
            //Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return tv;
    }
    
    public static TextView addCurrentTV(final Context c, String txt) {
        return addCurrentTV(c, txt, 0);
    }
    
    public static AnswerListItem addCalculatedTV(final Context c, JSONObject jo) {
        try{
            final AnswerListItem tv = new AnswerListItem(c);
            //((MainActivity)c).registerForContextMenu(tv);
        
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //lp.height = LayoutParams.WRAP_CONTENT;
        //lp.width = LayoutParams.MATCH_PARENT;
        //tv.setPadding(14,14,14,14);
        tv.setLayoutParams(lp);
        
        //Drawable d = getResources().getDrawable(R.drawable.bg_grey_green_cc, null);
        Drawable d = c.getResources().getDrawable(R.drawable.bg_grey_green_s, null);
        int clr = Color.parseColor("#EEFFFFFF");
            try {
                if(Ut.jsonContainsP(jo, ASelf.Calculation.dType.toString())&&
                    jo.getInt(ASelf.Calculation.dType.toString())==1) {
                    //d=getResources().getDrawable(R.drawable.bg_accent_cc, null);
                    d=c.getResources().getDrawable(R.drawable.bg_accent_s, null);
                    //clr = Color.BLACK;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                //d = getResources().getDrawable(R.drawable.bg_grey_green_cc, null);
                d = c.getResources().getDrawable(R.drawable.bg_grey_green_s, null);
            }
        
        tv.setAlpha(((MainActivity)c).placeholders?0.5f:1f);
        tv.setGravity(Gravity.CENTER);
        tv.rlALI.setBackground(d);
        tv.tvAns.setTextColor(clr);
        //tv.tvAnsNr.setText("@"+(ASelf.isNumber(jo.getString(ASelf.Calculation.Ix.toString()))?jo.getString(ASelf.Calculation.LmtIx.toString()):jo.getString(ASelf.Calculation.Ix.toString())));
        tv.tvAnsNr.setText("@"+jo.getString(ASelf.Calculation.LmtIx.toString()));
        tv.tvAnsCalc.setText(jo.getString(ASelf.Calculation.Exp.toString()));
        
            tv.tvAns.setText(Ut.jsonContainsP(jo, ASelf.Calculation.ExpAns.toString())?jo.getString(ASelf.Calculation.ExpAns.toString()):jo.getString(ASelf.Calculation.DoneCalc.toString()));
        tv.setTag(((MainActivity)c).llCalculation.getChildCount()+"");
            if(ASelf.isNumber(jo.getString(ASelf.Calculation.Ix.toString()))) {
                tv.setOnClickListener(onAnsCalcClick(c, Integer.parseInt(jo.getString(ASelf.Calculation.Ix.toString()))));
                tv.tvAnsCalc.setOnClickListener(onAnsCalcClick(c, Integer.parseInt(jo.getString(ASelf.Calculation.Ix.toString()))));
            }
            //tv.tvAnsCalc.setOnLongClickListener(onExpCopyClick(c));
        tv.tvAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(c, tv.tvAnsNr.getText().toString()+"="+((TextView)v).getText().toString(), Toast.LENGTH_SHORT).show();
                
                } catch(Exception ex) {
                    Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ((MainActivity)c).llCalculation.addView(tv);
        
        return tv;
        } catch(Exception ex) {ex.printStackTrace(); Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();}
        
        return null;
    }
    
    public static AnswerListItem addCalculatedTV(final Context c, JSONObject jo, int dType) {
        try {jo.put(ASelf.Calculation.dType.toString(), dType);} catch(Exception ex){ex.printStackTrace();}
        return addCalculatedTV(c, jo);
    }
    
    public static View.OnClickListener onAnsCalcClick(final Context c, final int ansIndx) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(((MainActivity)c).llCalculation.getChildCount()>0) {
                    if(ASelf.get(c).hcb) {
                        ((MainActivity)c).clearC();
                        ASelf.get(c).onSaved(c, ansIndx, ((MainActivity)c).llCalculation, ((MainActivity)c).tvDoneCalculation);
                        ASelf.get(c).setHCB(false);
                        //Toast.makeText(c, ansIndx+":@", Toast.LENGTH_SHORT).show();
                    } else {
                        ((MainActivity)c).clearCTVS();
                        if(!((MainActivity)c).llCalculation.getChildAt(((MainActivity)c).llCalculation.getChildCount()-1).equals(v)) {
                        ((MainActivity)c).llCalculation.getChildAt(ansIndx).setSelected(true);
                        for(int x=0;x<((MainActivity)c).llCalculation.getChildCount(); x++) {
                            if(((MainActivity)c).llCalculation.getChildAt(x).isSelected()) {
                            ((TextView)((MainActivity)c).llNC.getChildAt((x)*2)).callOnClick();
                            break;
                        }
                    }
                        //tv.tvAns.setTextColor(Color.BLACK);
                    } //else {
                      //  ((AnswerListItem)v).tvAns.setTextColor(Color.WHITE);
                      //  }
                        }
                    }
                } catch(Exception ex) {
                    Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
    /*
    public static View.OnLongClickListener onExpCopyClick(Context c) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    v.showContextMenu();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                
                return true;
            }
        };
    }
    */
    public static void tvCalcTitle(final Context c, String s) {
        TextView tv = new TextView(c);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setPadding(5,5,5,5);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16f);
        tv.setBackground(c.getResources().getDrawable(R.drawable.bg_grey_r, null));
        tv.setTextColor(Color.WHITE);
        tv.setText(s);
        ((MainActivity)c).tvCTitle=tv;
        ((MainActivity)c).llCalculation.addView(tv);
    }

    
}
