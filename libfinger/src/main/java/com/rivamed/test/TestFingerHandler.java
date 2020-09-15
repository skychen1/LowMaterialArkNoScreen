package com.rivamed.test;

import com.rivamed.FingerManager;
import com.rivamed.libdevicesbase.base.FunctionCode;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import com.rivamed.net.callback.FingerHandler;

public class TestFingerHandler implements FingerHandler {

    /**
     * ScheduledThreadPoolExecutor用于定时任务，这里的定时意义在于：
     * 1、指定延时后执行任务。
     * 2、周期性重复执行任务。
     * 继承了ThreadPoolExecutor，实现了ScheduledExecutorService。在线程池的基础上，实现了可调度的线程池功能
     */
    public static final String M_ID = "AA550101030006";

    private FingerManager mFingerManager;
    private String fingerData = "AA55010120000200280000000000000000000000000000004B01AA55010103000600000001000000000000000000000000000B01AA55010103000655AA010103000100000000000000000000000000000000000501AA55010103000600000001000000000000000000000000000B01AA55010120000200280000000000000000000000000000004B01AA55010103000600000001000000000000000000000000000B01AA55010103000655AA010103000100000000000000000000000000000000000501AA55010103000600000001000000000000000000000000000B01";
    private ScheduledThreadPoolExecutor scheduled;

    public TestFingerHandler(FingerManager fingerManager) {
        this.mFingerManager = fingerManager;
    }

    public int register() {

        if (scheduled == null) {
            ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("test-finger-schedule-pool-%d").daemon(true).build();
            scheduled = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        }

        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mFingerManager.getCallBack() != null) {
                    mFingerManager.getCallBack().onRegisterResult(M_ID, 0, fingerData, new ArrayList<String>(), "成功");
                }
            }
        });
        return FunctionCode.SUCCESS;
    }


    @Override
    public int closeChannel() {
        return 0;
    }

    @Override
    public int startReadFinger() {
        return 0;
    }

    @Override
    public int stopReadFinger() {
        return 0;
    }

    @Override
    public int startRegisterFinger(int timeOut, String filePath) {
        return 0;
    }

    @Override
    public int stopRegisterFinger() {
        return 0;
    }

    @Override
    public String getRemoteIP() {
        return "test:192.168.100.13";
    }

    @Override
    public String getProducer() {
        return "testFinger";
    }

    @Override
    public String getVersion() {
        return "testFinger1.0";
    }
}
