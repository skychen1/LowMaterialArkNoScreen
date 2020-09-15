package com.rivamed.libdevicesbase.base;

import java.io.Serializable;

/**
 * describe ： 设备信息数据类
 *
 * @author : Yich
 * data: 2019/1/29
 */
public class DeviceInfo implements Serializable {

    /**
     * identification 设备的id标识
     * remoteIP   ip地址
     * product;生产厂家
     * version 版本号
     * isStart 是否在 运行
     * mType 设备类型
     */
    private String identification;
    private String remoteIP;
    private String product;
    private String version;
    private boolean isStart = false;
    private int mType;

    private  DeviceType deviceType;

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }


    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public DeviceInfo(String identification, String remoteIP) {
        this.identification = identification;
        this.remoteIP = remoteIP;
    }

    public DeviceInfo(String identification, String remoteIP, String product, String version) {
        this.identification = identification;
        this.remoteIP = remoteIP;
        this.product = product;
        this.version = version;
    }

    public DeviceInfo(String identification, String remoteIP, String product, String version, int type) {
        this.identification = identification;
        this.remoteIP = remoteIP;
        this.product = product;
        this.version = version;
        this.mType = type;
    }
    public enum DeviceType {
        /**
         * 通用标识为 超高频阅读器
         */
        UHFREADER,
        /**
         * 导引屏
         */
        Hmi,
        /***
         * ETH002 类型的设备，包含 两种类型 V2,V2.6
         */
        Eth002
    }
}
