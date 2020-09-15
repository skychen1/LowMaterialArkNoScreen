
package com.raylinks;
/**
 * describe ： 加载睿芯联科reader动态连接库
 *
 * @author : Yich
 * date: 2019/3/22
 */
public class ModuleControl {


    static {
        System.loadLibrary("UhfReader");
    }

    private ModuleControl() {
    }

    public static native int UhfReaderConnect(String var0, int var1, byte var2);

    public static native boolean UhfReaderDisconnect(int var0, byte var1);

    public static native boolean UhfGetPaStatus(int var0, byte[] var1, byte var2);

    public static native boolean UhfGetPower(int var0, byte[] var1, byte var2);

    public static native boolean UhfSetPower(int var0, byte var1, byte var2, byte var3);

    public static native boolean UhfGetFrequency(int var0, byte[] var1, byte[] var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte var7);

    public static native boolean UhfSetFrequency(int var0, byte var1, byte var2, byte[] var3, byte var4, byte var5, byte var6, byte var7);

    public static native boolean UhfGetReaderUID(int var0, byte[] var1, byte var2);

    public static native boolean UhfStartInventory(int var0, byte var1, byte var2, byte var3);

    public static native boolean UhfReadInventory(int var0, byte[] var1, byte[] var2);

    public static native boolean UhfStopOperation(int var0, byte var1);

    public static native boolean UhfInventorySingleTag(int var0, byte[] var1, byte[] var2, byte var3);

    public static native boolean UhfReadDataByEPC(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte[] var5, byte[] var6, byte[] var7, byte var8);

    public static native boolean UhfWriteDataByEPC(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte[] var5, byte[] var6, byte[] var7, byte var8);

    public static native boolean UhfEraseDataByEPC(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte[] var5, byte[] var6, byte var7);

    public static native boolean UhfLockMemByEPC(int var0, byte[] var1, byte[] var2, byte[] var3, byte[] var4, byte var5);

    public static native boolean UhfKillTagByEPC(int var0, byte[] var1, byte[] var2, byte[] var3, byte var4);

    public static native boolean UhfGetVersion(int var0, byte[] var1, byte[] var2, byte var3);

    public static native boolean UhfReadDataFromSingleTag(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte[] var5, byte[] var6, byte[] var7, byte[] var8, byte var9);

    public static native boolean UhfWriteDataToSingleTag(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte[] var5, byte[] var6, byte[] var7, byte[] var8, byte var9);

    public static native boolean UhfBlockWriteDataByEPC(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte[] var5, byte[] var6, byte[] var7, byte[] var8, byte[] var9, byte[] var10, byte var11);

    public static native boolean UhfReadMaxDataByEPC(int var0, byte[] var1, byte var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7, byte var8);

    public static native boolean UhfReadMaxDataFromSingleTag(int var0, byte[] var1, byte var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7, byte[] var8, byte var9);

    public static native boolean UhfEraseDataFromSingleTag(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte[] var5, byte[] var6, byte var7);

    public static native boolean UhfLockMemFromSingleTag(int var0, byte[] var1, byte[] var2, byte[] var3, byte[] var4, byte var5);

    public static native boolean UhfKillSingleTag(int var0, byte[] var1, byte[] var2, byte[] var3, byte var4);

    public static native boolean UhfBlockWriteDataToSingleTag(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte[] var5, byte[] var6, byte[] var7, byte[] var8, byte[] var9, byte[] var10, byte var11);

    public static native boolean UhfStartReadDataFromMultiTag(int var0, byte[] var1, byte var2, byte[] var3, byte var4, byte var5, byte[] var6, byte var7);

    public static native boolean UhfGetDataFromMultiTag(int var0, byte[] var1, byte[] var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7);

    public static native boolean UhfGetRegister(int var0, int var1, int var2, byte[] var3, byte[] var4, byte var5);

    public static native boolean UhfSetRegister(int var0, int var1, int var2, byte[] var3, byte[] var4, byte var5);

    public static native boolean UhfResetRegister(int var0, byte var1);

    public static native boolean UhfSaveRegister(int var0, byte var1);

    public static native boolean UhfEnterSleepMode(int var0, byte var1);

    public static native int UhfUpdateInit(String var0, int var1, byte[] var2, byte[] var3, byte var4);

    public static native boolean UhfUpdateSendRN32(int var0, byte[] var1, byte[] var2, byte var3);

    public static native boolean UhfUpdateSendSize(int var0, byte[] var1, byte[] var2, byte var3);

    public static native boolean UhfUpdateSendData(int var0, byte[] var1, byte var2, byte var3, int var4, byte[] var5, byte var6);

    public static native boolean UhfUpdateCommit(int var0, byte[] var1, byte var2);

    public static native boolean UhfBlockWriteEPCToSingleTag(int var0, byte[] var1, byte var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7, byte[] var8, byte var9);

    public static native boolean UhfBlockWriteEPCByEPC(int var0, byte[] var1, byte var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7, byte[] var8, byte var9);

    public static native boolean UhfStartInventoryWithStop(int var0, byte var1, byte var2, byte[] var3, byte var4);

    public static native boolean UhfAddFilter(int var0, byte var1, byte var2, byte var3, byte var4, byte[] var5, byte[] var6, byte var7, byte[] var8, byte var9);

    public static native boolean UhfDeleteFilterByIndex(int var0, byte var1, byte[] var2, byte var3);

    public static native boolean UhfStartGetFilterByIndex(int var0, byte var1, byte var2, byte[] var3, byte var4);

    public static native boolean UhfReadFilterByIndex(int var0, byte[] var1, byte[] var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7, byte[] var8, byte[] var9, byte[] var10);

    public static native boolean UhfSelectFilterByIndex(int var0, byte var1, byte var2, byte[] var3, byte var4);

    public static native boolean UhfPsam(int var0, byte[] var1, byte var2);
}
