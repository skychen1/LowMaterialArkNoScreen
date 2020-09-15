package com.ruihua.reader.test;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.ruihua.reader.ReaderManager;
import com.ruihua.reader.bean.EpcInfo;
import com.ruihua.reader.net.callback.ReaderHandler;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * describe 测试通道，返回假数据
 *
 * @author : Yich
 * date: 2019/4/1
 */
public class TestHandler implements ReaderHandler {
    public static final String MID = "reader_test";
    private String[] mEpcs = new String[]{"00021320180104000030", "00021320180104000031", "00021320180104000032", "00021320180104000033", "00021320180104000034",
            "00021320180104000035", "00021320180104000036", "00021320180104000037", "00021320180104000038", "00021320180104000039"};
    private ScheduledThreadPoolExecutor scheduled;
    private Map<String, List<EpcInfo>> epcList = new HashMap<>();
    private ReaderManager mManager;
    private volatile boolean isScan = false;

    /**
     * 构造函数，传递mananger进来，用于数据回调
     *
     * @param manager 管理类
     */
    public TestHandler(ReaderManager manager) {
        mManager = manager;
        if (scheduled == null) {
            ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("test-reader-schedule-pool-%d").daemon(true).build();
            scheduled = new ScheduledThreadPoolExecutor(3, namedThreadFactory);
        }
    }


    @Override
    public int closeChannel() {
        return 0;
    }

    @Override
    public int startScan(int timeout) {
        if (isScan) {
            return -1;
        }
        isScan = true;
        epcList.clear();
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                for (String epc : mEpcs) {
                    //延时500ms返回标签
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        LogUtils.e(e.toString());
                    }
                    //如果有回调就回调数据,并且将数据保存在集合中
                    if (mManager.getCallBack() != null) {
                        mManager.getCallBack().onScanNewEpc(MID, epc, 2);
                        EpcInfo info = new EpcInfo(110, 2, epc);
                        ArrayList<EpcInfo> list = new ArrayList<>();
                        list.add(info);
                        epcList.put(epc, list);
                    }
                }
                //循环处理完以后停止传入的时间
                try {
                    Thread.sleep(timeout);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调所有的数据
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onScanResult(MID, epcList);
                }
                isScan = false;
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public int startScan(int timeout, byte[] ants) {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int stopScan() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int setPower(byte power) {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调设置成功的回调
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onSetPower(MID, true);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public int setPower(byte[] powers) {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int getPower() {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调设置成功的回调
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onGetPower(MID, 18);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public int checkAnts() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int reset() {
        return FunctionCode.SUCCESS;
    }

    @Override
    public int openLock() {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调设置成功的回调
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onLockOpen(MID, true);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public int closeLock() {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调设置成功的回调
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onLockClose(MID, true);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public int openLight() {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调设置成功的回调
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onLightOpen(MID, true);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public int closeLight() {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调设置成功的回调
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onLightClose(MID, true);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public int checkLockState() {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调设置成功的回调
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onLockState(MID, false);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public int checkLightState() {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //回调设置成功的回调
                if (mManager.getCallBack() != null) {
                    mManager.getCallBack().onLightState(MID, true);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public String getRemoteIP() {
        return "test_192.168.100.20";
    }

    @Override
    public String getProducer() {
        return "test";
    }

    @Override
    public String getVersion() {
        return "test 1.0";
    }
}
