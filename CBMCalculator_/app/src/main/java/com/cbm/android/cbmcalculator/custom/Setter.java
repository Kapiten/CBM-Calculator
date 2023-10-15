package com.cbm.android.cbmcalculator.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.cbmcalculator.ASelf;
import com.cbm.android.cbmcalculator.R;
import com.cbm.android.cbmcalculator.settings.SettingsActivity;
import com.cbm.android.cbmcalculator.utility.*;

import org.json.JSONObject;

public class Setter extends RelativeLayout {
    public TextView tvTitle;
    //private View vSet;
    public RelativeLayout llSet;
    private AlertDialog ad;
    private String rtitle,stitle,title,vtype;//rtitle=raw title, stitle=simple title
    private int indx = 0;
    private Object o;
    
	  public Setter(Context c, AttributeSet a, int b) {
        super(c,a,b);
    }
    
    public Setter(Context c, AttributeSet a) {
        super(c,a);
    }
    
    public Setter(Context c, int z) {
        super(c);
        initialisation(z);
    }
    
    public Setter(Context c, int z, String s) {
        super(c);
        initialisation(z);
    }
    
    public void initialisation(int z) {
        try {
									LayoutInflater.from(getContext()).inflate(
									(SettingsActivity.getSetType(ASelf.settings[z]).contains("Text")?R.layout.layout_framed_items_b:R.layout.layout_framed_items_a),
									this, true); 
        indx = z;
        tvTitle = findViewById(R.id.tvSetTitle);
        //vSet = findViewById(R.id.vSet);
        llSet = findViewById(R.id.llSetv);
        rtitle="";
									if(z>=0) {
        rtitle=ASelf.settings[z];
        vtype = SettingsActivity.getSetType(rtitle);
        stitle=ASelf.settings[z].substring(ASelf.settings[z].indexOf("_"));
												title = SettingsActivity.getSetName(rtitle);
        
        tvTitle.setText(title);
        setPView(vtype);
        if(!ASelf.getCalcSet(getContext()).contains(rtitle)){
            SharedPreferences.Editor ed = ASelf.getCalcSet(getContext()).edit();
            
            switch(vtype) {
                case "Color": ed.putInt(rtitle, R.drawable.bg_green_r);break;
                case "Textc": ed.putInt(rtitle, ASelf.arrC().get(1));break;
                case "Text": ed.putString(rtitle, "");break;
                case "Textn": ed.putString(rtitle, "");break;
                case "Texttn": ed.putString(rtitle, "");break;
                case "Texttcn": ed.putString(rtitle, "");break;
                case "Switch": ed.putString(rtitle, "Off");break;
            }
            
            ed.apply();
					}
								
        initialiseView(vtype);
		     //llSet.getChildAt(0).setBackgroundResource(ASelf.arrC().get(0));
        }
								} catch(Exception ex) {
												ex.printStackTrace();
												Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
								}
    }
			
		public void initialiseView(String type) {
						setPView(type);
						if(type.equals("Switch")) {
        ASelf.setting = ASelf.getCalcSet(getContext()).getString(rtitle, "Off");
					    //o = new Button(getContext());
            ((Button)o).setLayoutParams(new LayoutParams(80,60));
							
							llSet.addView(((Button)o));
            setBG(0,R.drawable.bg_grey_green_r);
        			set(type,ASelf.setting);
										((Button)o).setSelected(ASelf.setting.equals("On"));							
					} /*else if(type.equals("Text")||type.equals("Textc")||type.equals("Textn")) {
											try{
            ((TextView)o).setLayoutParams(new LayoutParams(60,90));
							ASelf.setting = String.valueOf(ASelf.getCalcSet(getContext()).getString(rtitle, ""));
					   ((TextView)o).setGravity(type.equals("Textn")?Gravity.RIGHT:Gravity.LEFT);
																((TextView)o).setBackgroundResource(R.drawable.bg_white_r);
                           ((TextView)o).setTextSize(20f);
																((TextView)o).setTextColor(Color.BLACK);
																((TextView)o).setHint(type.equals("Textn")?"Write Number":"Write Expression");
																
																} catch (Exception ex) {
																				ex.printStackTrace();
																				//Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();	
																}
																llSet.setBackgroundResource(R.drawable.bg_green_s);
                           llSet.addView(((TextView)o));
												
        } */else if(type.equals("Color")){
            int drId=0, ti=0; JSONObject jo=new JSONObject();
																//View v = new View(getContext());
            try{
            				((View)o).setLayoutParams(new LayoutParams(60,60));
											//ASelf.setting = String.valueOf(ASelf.getCalcSet(getContext()).getInt(ASelf.settings[z], R.drawable.bg_green_r));
					   				//drId = ASelf.getCalcSet(getContext()).getInt("Color"+stitle, 0);
            								jo=new JSONObject(ASelf.getCalcSet(getContext()).getString("Color"+stitle, DefaultThemeSet.defaultBGFormats().get(0).toString()));
            									ti=ASelf.getCalcSet(getContext()).getInt("Textc"+stitle, 0);
																} catch (Exception ex) {
																				ex.printStackTrace();
																				//Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();	
																}
																llSet.setBackgroundResource(R.drawable.bg_green_s);
                           TextView tvA = new TextView(getContext());
																tvA.setLayoutParams(new LayoutParams(60,60));
																tvA.setTextSize(20f);
																tvA.setTextColor(ti);
																tvA.setText("A");
																tvA.setGravity(Gravity.CENTER);
									
																llSet.addView(((View)o));
																llSet.addView(tvA);
							try{
												ShapeDrawable sd = DefaultThemeSet.mkShape(jo);
								   setBG(sd);
									 setTV(ASelf.getCalcSet(getContext()).getInt("Textc"+stitle, Color.BLACK));
            } catch (Exception ex) {
																				ex.printStackTrace();
																				//Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();	
																}
						} else if(type.contains("Text")) {
												((EditText)o).setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

                            //((EditText)o).setBackground(new ColorDrawable(Color.parseColor("#FFFFFFFF")));
									((EditText)o).setText(ASelf.getCalcSet(getContext()).getString(rtitle,""));
									((EditText)o).setTextColor(Color.parseColor("#FF808080"));
											llSet.setBackground(new ColorDrawable(Color.parseColor("#00000000")));
                            int dim8 = Math.round(getContext().getResources().getDimension(R.dimen.dim8dp));
                            int dim16 = Math.round(getContext().getResources().getDimension(R.dimen.dim16dp));
                            ((EditText)o).setPadding(dim16,dim8,dim16,dim8);
        				if(vtype.equals("Textn")) {((EditText)o).setInputType(InputType.TYPE_CLASS_NUMBER);}
									else if(vtype.equals("Texttcn")) {}
												((EditText)o).setBackgroundResource(R.drawable.bg_green_s);
                    ((EditText)o).addTextChangedListener(onTexting());
									llSet.addView(((EditText)o));
												}
										llSet.getChildAt(0).setTag(ASelf.settings[indx]);
        				llSet.getChildAt(0).setOnClickListener(onClickT(vtype));
								
			}
    
    public void setPView(String s) { //Peripheral View
        if(s.equals("Color")) {
            o = new View(getContext());
            
        } else if(s.equals("Switch")) {
            o = new Button(getContext());
            
        } else if(s.contains("Text")) {
            o = new EditText(getContext());
            
        } else {
            o = new View(getContext());
            
        }
        
    }
				
    public void set(String t, String s) {
        if(t.equals("Switch")) {
						//((Button)v).setText(ASelf.get().getCalcSet(getContext()).getString(ASelf.settings[z],"Off"));
										ASelf.setting=s;
                ((Button)o).setTextColor(ASelf.setting.equals("On")?Color.BLACK:Color.WHITE);
               SharedPreferences.Editor ed = ASelf.getCalcSet(getContext()).edit();
                    ed.putString(rtitle, s);
                ed.apply();
									
                //SharedPreferences.Editor ed = ASelf.getCalcSet(getContext()).edit();
                if(s.equals("On")) {
                    //((Button)o).setText(s);
                   //ASelf.get().setSBEg(getContext(),1);	
												((Button)o).setSelected(true);
									 } else {
                    //((Button)o).setText(s);
                    //ASelf.get().setSBEg(getContext(),0);
												((Button)o).setSelected(false);
                }
									((Button)o).setText("");
                    
            //smsg.setObj(s);
									//ed.putString(rtitle, ((Button)o).getText().toString());
                //ed.apply();
            
					} else {
							//ASelf.setting=llSet.getChildAt(0).getTag().toString();
                AlertDialog.Builder adbr = new AlertDialog.Builder(getContext());
                ad = adbr.create();
                final AlertColorChooseC acc = new AlertColorChooseC(getContext());
                try{
                    //acc.sts=ASelf.getCalcSet(getContext()).getInt("Textc_"+stitle, Color.BLACK);
									     //acc.sbgs=ASelf.getCalcSet(getContext()).getInt(rtitle, R.drawable.bg_green_sr);
                 } catch (Exception ex) {
												ex.printStackTrace();
												//Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();	
										}
									 acc.getTVChk().setTextColor(((TextView)llSet.getChildAt(1)).getTextColors());
                acc.getBGChk().setBackground(llSet.getChildAt(0).getBackground());
                //Toast.makeText(getContext(), "Tag:"+llSet.getChildAt(0).getTag().toString()+" "+rtitle, Toast.LENGTH_SHORT).show();
                acc.btnCCApply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View vw) {
                try{
												SharedPreferences.Editor ed = ASelf.getCalcSet(getContext()).edit();
                //if(title.equals("Background")){acc.cbgt=1; acc.sbgs=acc.sbgi<=7?ASelf.arrClr().get(acc.sbgi):ASelf.arrC().get(acc.sbgi);}
												JSONObject jo = new JSONObject()
												.put(ASelf.Constants.DEFAULT_JSONCOLOR_ID,acc.ss[0])
												.put(ASelf.Constants.DEFAULT_JSONCOLOR_BGCOLOR,acc.ss[1])
												.put(ASelf.Constants.DEFAULT_JSONCOLOR_BGTYPE,acc.ss[2])
												.put(ASelf.Constants.DEFAULT_JSONCOLOR_TXTCOLOR,acc.ss[3]);
												ed.putString("Color"+stitle,jo.toString());
                ed.putInt("Textc"+stitle,acc.tClr.toArgb());
                ed.apply();
									 Toast.makeText(getContext(), acc.ss[1]+":"+" Color="+acc.sClr+" CBMColor="+CBMPaint.getJSONColor(new JSONObject(acc.ss[1])), Toast.LENGTH_SHORT).show();
                try{
             				  setBG(DefaultThemeSet.mkShape(jo));//acc.sd
												setTV(acc.tClr);
										} catch (Exception ex) {
													ex.printStackTrace();
									  }
																								ad.cancel();
                //getContext().finish();
										((SettingsActivity)getContext()).applySettings();
																				} catch (Exception ex) {
																								Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
																				}
            }
        });
                acc.btnCCancel.setOnClickListener(new OnClickListener() {
            @Override 
            public void onClick(View vw) {
                	ad.cancel();
                
            }
        });
                    adbr.setView(acc);
                ad = adbr.create();
                ad.show();
        }
								
								//return smsg;
    }
    
    private View.OnClickListener onClickT(String s) {
        if(s.equals("Color")) {
					    return onClickClr();
		     } else if(s.equals("Switch")) {
					    return onClickSwt();
		     }
					return null;
			}
			
		private View.OnClickListener onClickT(int aa) {
        if(aa==0) {
            return onClickClr();
        } else if(aa==1) {
            return onClickSwt();
        }
        
        return null;
    }
    
    private View.OnClickListener onClickSwt() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            set("Switch",!v.isSelected()?"On":"Off");
									/*if(v.isSelected()) {
                    ((Button)v).setText("On");
                    ASelf.get().sbEmpty=1;
                } else {
                    ((Button)v).setText("Off");
                    ASelf.get().sbEmpty=0;
										}*/
                
            }
        };
    }
    
    private View.OnClickListener onClickClr() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
																set("Color","");
									 } catch(Exception ex) {
																		ex.printStackTrace();
																				Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
																}/*ASelf.setting=llSet.getChildAt(0).getTag().toString();
                AlertDialog.Builder adbr = new AlertDialog.Builder(getContext());
                ad = adbr.create();
                AlertColorChooseB acc = new AlertColorChooseB(getContext());
                
                Toast.makeText(getContext(), "Tag:"+llSet.getChildAt(0).getTag().toString()+" "+rtitle, Toast.LENGTH_SHORT).show();
                acc.btnCCApply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed = ASelf.getCalcSet(getContext()).edit();
                ed.putInt(rtitle,Integer.parseInt(v.getTag()+""));
                ed.apply();
                llSet.getChildAt(0).setBackground(getContext().getResources().getDrawable(Integer.parseInt(v.getTag()+"")));
                //llSet.getChildAt(0).setBackgroundResource(ASelf.arrC().get(Integer.parseInt(v.getTag()+"")));
                Toast.makeText(getContext(), ASelf.setting, Toast.LENGTH_SHORT).show();
                ad.cancel();
                //getContext().finish();
            }
        });
                acc.btnCCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
                
            }
        });
                    adbr.setView(acc);
                ad = adbr.create();
                ad.show();*/
            }
												};
							
    }
			
			private TextWatcher onTexting() {
        return new TextWatcher() {
									String txtb="";
									@Override
										   public void beforeTextChanged(CharSequence t, int a, int b, int c) {
																		txtb=t.toString();
											 }
															
										   @Override
									public void onTextChanged(CharSequence t, int a, int b, int c) {
										       try{
															boolean bPattern=false;
															
															if(Regexp.matchNumberAndSigns(t.toString())){bPattern=true;}
															else if(Regexp.matchNumber(t.toString())){bPattern=true;}
															if(bPattern) {
												SharedPreferences.Editor ed = ASelf.getCalcSet(getContext()).edit();
                        ed.putString(rtitle, t.toString());
                        ed.apply();
															} else {
																		t = txtb;//t.toString().substring(0,t.length()-1);
																		
																		if(vtype.equals("textn")){t=Integer.parseInt(t.toString().isEmpty()?"0":t.toString())+"";}
															}
															
												} catch(Exception ex){
															ex.printStackTrace();
												}
												
											 }
															
										   @Override
									public void afterTextChanged(Editable e) {
																		
											 }
									 
				  };
			}
				
		private void setBG(int aa, int bb) {
				if(aa==0){llSet.getChildAt(0).setBackgroundResource(bb);}
       else {llSet.getChildAt(0).setBackgroundColor(bb);}
    }	
				
    private void setBG(Object v) {
				if(!v.equals(null))
				{if(v instanceof View){llSet.getChildAt(0).setBackground(((View)v).getBackground());}};
								
			}	
				
    private void setBG(Drawable d) {
				if(!d.equals(null))
				{llSet.getChildAt(0).setBackground(d);}
								
    }
				
    private void setTV(Color clr) {
				if(!clr.equals(null))
				{((TextView)llSet.getChildAt(1)).setTextColor(clr.toArgb());}
        			
    }
				
    private void setTV(View v) {
				if(!v.equals(null))
				{((TextView)llSet.getChildAt(1)).setTextColor(((TextView)v).getTextColors());}
        			
    }
    	
		 private void setTV(int bb) {
				((TextView)llSet.getChildAt(1)).setTextColor(bb);
       
		 }
			
    private View getBG() {
				return llSet.getChildAt(0);
    }
				
    private View getTV() {
				return llSet.getChildAt(1);
    }
}
