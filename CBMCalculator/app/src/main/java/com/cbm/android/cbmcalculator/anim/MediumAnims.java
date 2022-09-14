package com.cbm.android.cbmcalculator.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

public class MediumAnims {
    
    public static void scifraMenu(final View v, final boolean show) {
        try {
            ObjectAnimator oa, oa1;
            if(show) {
                oa = ObjectAnimator.ofFloat(v, "alpha", 1);
                oa.setDuration(300l);
            //    oa1 = ObjectAnimator.ofFloat(llEntry, "x", v.getWidth());
            //    oa1.setDuration(400l);
            } else {
                oa = ObjectAnimator.ofFloat(v, "alpha", 0);
                oa.setDuration(300l);
            //    oa1 = ObjectAnimator.ofFloat(llEntry, "x", 0);
            //    oa1.setDuration(400l);
            }
            
            oa.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator anim) {
                    if(show) {
                        v.setVisibility(View.VISIBLE);
                    }
                }
                
                @Override
                public void onAnimationEnd(Animator anim) {
                    if(!show) {
                        v.setVisibility(View.GONE);
                    }
                }
            });
            
            oa.start();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
