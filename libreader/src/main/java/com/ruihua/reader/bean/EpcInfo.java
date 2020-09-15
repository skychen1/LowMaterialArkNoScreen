package com.ruihua.reader.bean;

import java.io.Serializable;

/**
 * describe ： epc 标签数据的信息，用于扫描完成后回调
 *
 * @author : Yich
 * data: 2019/2/26
 */
public class EpcInfo implements Serializable {
    /**
     * ant 扫描到该标签的天线
     * rssi 该标签的rssi值
     * pc  该标签的pc值
     */
    private int ant;
    private int rssi;
    private String pc;


    public int getAnt() {
        return ant;
    }

    public void setAnt(int ant) {
        this.ant = ant;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public EpcInfo(int ant, int rssi, String pc) {
        this.ant = ant;
        this.rssi = rssi;
        this.pc = pc;
    }

    public EpcInfo(){}

}
