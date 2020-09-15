package com.ruihua.reader.bean;

import java.io.Serializable;

/**
 * describe ： 天线状态实例类
 *
 * @author : Yich
 * data: 2019/2/27
 */
public class AntInfo implements Serializable {
    /**
     * antNum 第几根天线（天线的标识）
     * isUsable  （是否可用）
     */
    private int antNum;
    private boolean isUsable = false;

    public int getAntNum() {
        return antNum;
    }

    public void setAntNum(int antNum) {
        this.antNum = antNum;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }


}
