package com.cbm.android.alpha.cbmcalculator.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbm.android.alpha.cbmcalculator.R;

public class AnswerListItem extends RelativeLayout {
    
    public TextView tvAnsNr, tvAnsCalc, tvAns;
    public LinearLayout rlALI;
    
    public AnswerListItem(Context context) {
        super(context);
        
        init(context);
    }
    
    public AnswerListItem(Context context, AttributeSet attr) {
        super(context, attr);
        
        init(context);
    }
    
    public AnswerListItem(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        
        init(context);
    }

    private void init(final Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_answer_list_item_b, this, true);
        
        rlALI = v.findViewById(R.id.rlALI);
        tvAnsNr = v.findViewById(R.id.tvAnsNr);
        tvAnsCalc = v.findViewById(R.id.tvAnsCalc);
        tvAns = v.findViewById(R.id.tvAns);
        
        tvAnsNr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(context, tvAnsNr.getText().toString(), Toast.LENGTH_SHORT).show();
                
                } catch(Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvAnsCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //tvAnsNr.setSelected(true);
                    //tvAnsCalc.setSelected(true);
                    //tvAns.setSelected(true);
                    Toast.makeText(context, tvAnsCalc.getText().toString(), Toast.LENGTH_SHORT).show();
                
                } catch(Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(context, tvAns.getText().toString(), Toast.LENGTH_SHORT).show();
                
                } catch(Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    public void createAnswerItem(String[] values) {
        tvAnsNr.setText(values[0]);
        tvAnsCalc.setText(values[1]);
        tvAns.setText(values[2]);
    }
}
