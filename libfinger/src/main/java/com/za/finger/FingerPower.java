/**
 * @author zhaohongwei
 * @author zhaohongwei
 * @author zhaohongwei
 * @author zhaohongwei
 */
/**
 * @author zhaohongwei
 *
 */
package com.za.finger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FingerPower {


    private static final String FINGER_POWER_PATCH = "/sys/zhwpower/zhw_power_finger";

    private static int IO_Switch(String cardpowerPath, int on) {
        try {
            File powerFile = new File(cardpowerPath);
            if (!powerFile.exists()) {
                return 0;
            }
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter(powerFile));
            bufWriter.write(Integer.toString(on));
            bufWriter.close();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }


    /**
     * 关闭指纹模块电源
     */
    public static int close() {
        return IO_Switch(FINGER_POWER_PATCH, 0);
    }


}