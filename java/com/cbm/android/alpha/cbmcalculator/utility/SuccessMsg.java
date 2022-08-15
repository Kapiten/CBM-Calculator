package com.cbm.android.alpha.cbmcalculator.utility;

public class SuccessMsg {
    float code = 0;
    String msg = "";
    Object obj = new Object();
    
    public SuccessMsg() {
        
    }
    
    public SuccessMsg(float code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }
    
    public void setCode(float code) {
        this.code = code;
        if(this.code>=1f)
            setMsg("Successful");
        else
            setMsg("Unsuccessful");
            
    }
    
    public float getCode() {
        return this.code;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
        
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public void setObj(Object obj) {
        this.obj = obj;
        
    }
    
    public Object getObj() {
        return this.obj;
    }
    
}
