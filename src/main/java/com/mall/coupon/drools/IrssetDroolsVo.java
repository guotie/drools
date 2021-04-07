package com.mall.coupon.drools;

import java.io.Serializable;

public class IrssetDroolsVo implements Serializable {
    private Integer surpDayCnt = null;
    private boolean mBlack = false;
    private String msg;


    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public IrssetDroolsVo() {
    }
    private static final long serialVersionUID = 1L;


    public Integer getSurpDayCnt() {
        return surpDayCnt;
    }
    public void setSurpDayCnt(Integer surpDayCnt) {
        this.surpDayCnt = surpDayCnt;
    }
    public boolean ismBlack() {
        return mBlack;
    }
    public void setmBlack(boolean mBlack) {
        this.mBlack = mBlack;
    }
}
