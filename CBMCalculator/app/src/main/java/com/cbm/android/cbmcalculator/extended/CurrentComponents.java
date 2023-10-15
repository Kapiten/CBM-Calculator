package com.cbm.android.cbmcalculator.extended;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;

import com.cbm.android.cbmcalculator.ASelf;
import com.cbm.android.cbmcalculator.MainActivity;
import com.cbm.android.cbmcalculator.R;
import com.cbm.android.cbmcalculator.custom.AnswerListItem;
import com.cbm.android.cbmcalculator.utility.Ut;

import org.json.JSONArray;
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
                    for(int a=1; a<((MainActivity)c).llCalculation.getChildCount(); a++) {
                        AnswerListItem ansV = (AnswerListItem)((MainActivity)c).llCalculation.getChildAt(a);
                        JSONArray ja = new JSONArray(ansV.getOtherInfo());
                        boolean found = false;
                        if(ASelf.get(c).tvCInd<((MainActivity)c).llNC.getChildCount()-2) {
                            if(ja.getInt(0)==ASelf.get(c).tvCInd||ja.getInt(1)==ASelf.get(c).tvCInd) {
                                found=true;
                            }
                        } else {
                            if(ja.getInt(2)==ASelf.get(c).tvCInd) {found=true;}
                        }
                        if(found) {
                            ansV.callOnClick();
                            ((MainActivity)c).svC.smoothScrollTo(0,Math.round(ansV.getY()));
                            break;
                        }
                    }
                        }
            } catch(Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((MainActivity)c).llNC.addView(tv, indx);
        
        //hsvNC.smoothScrollTo(llNC.getChildAt(Integer.parseInt(tv.getTag()+"")).getWidth()*llNC.getChildCount(),0);
        //Toast.makeText(getApplicationContext(), "ll = " + llNC.getChildAt(Integer.parseInt(tv.getTag()+"")).getWidth()+ ", llCC = " + llNC.getChildAt(Integer.parseInt(tv.getTag()+"")).getLayoutParams().width, Toast.LENGTH_SHORT).show();
        } catch(Exception ex) {
            ex.printStackTrace();
            Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
            //tv.setTag(((MainActivity)c).llCalculation.getChildCount()+"");
            if(ASelf.isNumber(jo.getString(ASelf.Calculation.Ix.toString()))) {
                tv.setOnClickListener(onAnsCalcClick(c, Integer.parseInt(jo.getString(ASelf.Calculation.Ix.toString()))));
                //tv.tvAnsCalc.setOnClickListener(onAnsCalcClick(c, Integer.parseInt(jo.getString(ASelf.Calculation.Ix.toString()))));
                tv.setOnLongClickListener(onExpCopyClick(c));
            }
            if(jo.has(c.getResources().getString(R.string.answer_for)))
            {tv.setOtherInfo(jo.get(c.getResources().getString(R.string.answer_for)).toString());}
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
                    if (((MainActivity) c).llCalculation.getChildCount() > 0) {
                        if (ASelf.get(c).hcb) {
                            ((MainActivity) c).clearC();
                            ASelf.get(c).onSaved(c, ansIndx, ((MainActivity) c).llCalculation, ((MainActivity) c).tvDoneCalculation);
                            ASelf.get(c).setHCB(false);
                            //Toast.makeText(c, ansIndx+":@", Toast.LENGTH_SHORT).show();
                        } else {
                            ((MainActivity) c).clearCTVS();
                            if (!((MainActivity) c).llCalculation.getChildAt(((MainActivity) c).llCalculation.getChildCount() - 1).equals(v)) {
                                ((MainActivity) c).llCalculation.getChildAt(ansIndx).setSelected(true);
                                for (int n = 0; n < ((MainActivity) c).llNC.getChildCount(); n++) {
                                    ((MainActivity) c).llNC.getChildAt(n).setSelected(false);
                                }
                                for (int x = 1; x < ((MainActivity) c).llCalculation.getChildCount(); x++) {
                                    AnswerListItem ansV = (AnswerListItem) ((MainActivity) c).llCalculation.getChildAt(x);
                                    if (ansV.isSelected()) {
                                        int showIndx=0;
                                        JSONArray ja = new JSONArray(ansV.getOtherInfo());
                                        if (ja.length() > 0)
                                            ((MainActivity)c).llNC.getChildAt(ja.getInt(0)).setSelected(true);
                                        if (ja.length() > 1) {
                                            ((MainActivity)c).llNC.getChildAt(ja.getInt(1)).setSelected(true);

                                            //showIndx = ((MainActivity) c).llNC.getChildAt(ja.getInt(0)).getX()<((MainActivity) c).hsvNC.getScrollX()?ja.getInt(0):ja.getInt(1);
                                        }
                                        if (ja.length() > 2) {
                                            ((MainActivity)c).llNC.getChildAt(ja.getInt(2)).setSelected(true);
                                            //showIndx = ((MainActivity) c).llNC.getChildAt(ja.getInt(0)).getX()<0?ja.getInt(0):ja.getInt(2);
                                        }
                                        ((MainActivity)c).svC.smoothScrollTo(0, Math.round(ansV.getY()));
                                        ((MainActivity)c).hsvNC.smoothScrollTo(Math.round(((MainActivity) c).llNC.getChildAt(ja.getInt(0)).getX()), 0);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch(Exception ex) {
                    Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public static View.OnLongClickListener onExpCopyClick(final Context c) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                try {
                    new AlertDialog.Builder(c)
                            .setItems(R.array.copy_express, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClipboardManager cm = (ClipboardManager)c.getSystemService(Context.CLIPBOARD_SERVICE);
                                    switch(which) {
                                        case 0:
                                            cm.setText(((AnswerListItem)v).tvAnsCalc.getText().toString());
                                            Toast.makeText(c, "Copied: '" + ((AnswerListItem)v).tvAnsCalc.getText().toString()+"'", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 1:
                                            cm.setText(((AnswerListItem)v).tvAns.getText().toString());
                                            Toast.makeText(c, "Copied: '" + ((AnswerListItem)v).tvAns.getText().toString()+"'", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            })
                            .create().show();
                } catch(Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(c, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        };
    }

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
