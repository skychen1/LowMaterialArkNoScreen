package com.ruihua.reader.local.corelinks;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;
import com.ruihua.reader.local.LocalReaderManager;
import com.ruihua.reader.local.callback.LocalReaderOperate;
import com.uhf.api.cls.BackReadOption;
import com.uhf.api.cls.ReadExceptionListener;
import com.uhf.api.cls.ReadListener;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe ： 芯联USB连接reader管理类
 *
 * @author : Yich
 * date: 2019/4/18
 */
public class CoreLinksManager1 implements LocalReaderOperate {

    /**
     * deviceId 设备id，唯一标识
     * hReader 设备打开的标识，操作需要用
     */
    private LocalReaderManager localReaderManager;
    private int antNum = 16;
    private Reader reader;
    private String deviceId;
    private Handler mHandler;
    private Map<String, List<EpcInfo>> epcList = new HashMap<>();
    private int timeOut;
    private BackReadOption option;
    private volatile boolean isBusy = false;
    private String filter = "";


    public CoreLinksManager1(LocalReaderManager manager) {
        localReaderManager = manager;
        reader = new Reader();
        //注册读取监听
        reader.addReadListener(mReadListener);
        reader.addReadExceptionListener(mExceptionListener);
        option = new BackReadOption();
        option.IsFastRead = false;
        option.ReadInterval = 0;
        HandlerThread mHandlerThread = new HandlerThread(" CoreLinks1" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public int connect(String port, int baud) {
        //调用本地方法进行连接
        Reader.READER_ERR err = reader.InitReader_Notype(port, antNum);
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.CONNECT_FAILED;
        }
        //连接成功就获取设备号
        Reader.DeviceSerialNumber serialNumber = reader.new DeviceSerialNumber();
        err = reader.GetSerialNumber(serialNumber);
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.CONNECT_FAILED;
        }
        //记录设备号，唯一标识
        deviceId = serialNumber.serailNumber;
        Reader.Inv_Potls_ST ipst = reader.new Inv_Potls_ST();
        ipst.potlcnt = 1;
        ipst.potls = new Reader.Inv_Potl[ipst.potlcnt];
        for (int i = 0; i < ipst.potlcnt; i++) {
            Reader.Inv_Potl ipl = reader.new Inv_Potl();
            ipl.weight = 30;
            ipl.potl = Reader.SL_TagProtocol.SL_TAG_PROTOCOL_GEN2;
            ipst.potls[i] = ipl;
        }
        reader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
        int[] av = new int[1];
        reader.ParamGet(Reader.Mtr_Param.MTR_PARAM_READER_AVAILABLE_ANTPORTS, av);
        //添加到已连接的集合中
        localReaderManager.addConnectReader(deviceId, CoreLinksManager1.this);
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onConnectState(deviceId, true);
        }
        return FunctionCode.SUCCESS;
    }

    @Override
    public int disConnect() {
        reader.CloseReader();
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onConnectState(deviceId, false);
        }
        return FunctionCode.SUCCESS;
    }

    @Override
    public int setPower(int powerInt) {
        if (isBusy) {
            return FunctionCode.DEVICE_BUSY;
        }
        isBusy = true;
        //准备参数实例类
        Reader.AntPowerConf powerConf = reader.new AntPowerConf();
        powerConf.antcnt = antNum;
        short power = (short) (powerInt * 100);
        for (int i = 0; i < powerConf.antcnt; i++) {
            //每一根天线都要设备一个
            Reader.AntPower jaap = reader.new AntPower();
            jaap.antid = i + 1;
            jaap.readPower = power;
            jaap.writePower = power;
            powerConf.Powers[i] = jaap;
        }
        //设置参数
        Reader.READER_ERR err = reader.ParamSet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, powerConf);
        isBusy = false;
        //回调设置是否成
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onSetPower(deviceId, err == Reader.READER_ERR.MT_OK_ERR);
        }
        //根据返回结果确定返回码
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.OPERATION_FAIL;
        }
        return FunctionCode.SUCCESS;
    }

    @Override
    public int getPower() {
        if (isBusy) {
            return FunctionCode.DEVICE_BUSY;
        }
        isBusy = true;
        //准备实例类
        Reader.AntPowerConf powerConf = reader.new AntPowerConf();
        //读取功率
        Reader.READER_ERR err = reader.ParamGet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, powerConf);
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.OPERATION_FAIL;
        }
        if (powerConf.antcnt <= 0) {
            return FunctionCode.OPERATION_FAIL;
        }
        //所有的天线都是统一的功率值，所以拿到一个就足够了
        int power = powerConf.Powers[0].readPower;
        //根据规则转换回去；
        power = power / 100;
        isBusy = false;
        //回调结果
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onGetPower(deviceId, power);
        }
        return FunctionCode.SUCCESS;
    }

    @Override
    public int checkAnt() {
        if (isBusy) {
            return FunctionCode.DEVICE_BUSY;
        }
        isBusy = true;
        Reader.ConnAnts_ST val = reader.new ConnAnts_ST();
        //获取已连接天线
        Reader.READER_ERR err = reader.ParamGet(Reader.Mtr_Param.MTR_PARAM_READER_CONN_ANTS, val);
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.OPERATION_FAIL;
        }
        //准备返回的集合，加上所有数据
        List<AntInfo> list = new ArrayList<>();
        for (int i = 0; i < antNum; i++) {
            AntInfo antInfo = new AntInfo();
            antInfo.setAntNum(i + 1);
            antInfo.setUsable(false);
            list.add(antInfo);
        }
        //根据拿到连接的数据设置连接的天线
        for (int i = 0; i < val.antcnt; i++) {
            //设置天线正确（因为天线值是1开始，集合是0开始，所以要减1）
            list.get(val.connectedants[i] - 1).setUsable(true);
        }
        isBusy = false;
        //回调数据
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onCheckAnt(deviceId, list);
        }
        return FunctionCode.SUCCESS;
    }

    @Override
    public int getFrequency() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int startScan(int timeOut) {
        if (isBusy) {
            return FunctionCode.DEVICE_BUSY;
        }
        isBusy = true;
        //拿到时间
        this.timeOut = timeOut;
        epcList.clear();
        //先要获取有用的天线
        Reader.ConnAnts_ST val = reader.new ConnAnts_ST();
        Reader.READER_ERR err = reader.ParamGet(Reader.Mtr_Param.MTR_PARAM_READER_CONN_ANTS, val);
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.OPERATION_FAIL;
        }
        //没有可用的天线，返回设备故障
        if (val.antcnt <= 0) {
            return FunctionCode.DEVICE_ERROR;
        }

        //准备可用的天线
        int[] ant = new int[val.antcnt];
        System.arraycopy(val.connectedants, 0, ant, 0, val.antcnt);
        //根据天线根数设置时长
        option.ReadDuration = (short) (20 * ant.length);
        err = reader.StartReading(ant, ant.length, option);
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.OPERATION_FAIL;
        }
        //拿到过滤规则
        filter = localReaderManager.getFilter();
        //延时发送结果
        sendComplete();
        return FunctionCode.SUCCESS;
    }

    @Override
    public int stopScan() {
        //停止扫描
        Reader.READER_ERR err = reader.StopReading();
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.OPERATION_FAIL;
        }
        return FunctionCode.SUCCESS;
    }

    @Override
    public int reScan() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int reset() {
        //执行重启命令
        Reader.READER_ERR err = Reader.RebootReader(CoreLinksConfig.READER_COM_STR);
        if (err != Reader.READER_ERR.MT_OK_ERR) {
            return FunctionCode.OPERATION_FAIL;
        }
        return FunctionCode.SUCCESS;
    }

    @Override
    public int delOneEpc(String epc) {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public String getProducer() {
        return "coreLinks";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * 延时发送结果；
     */
    private void sendComplete() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //停止扫描,修改标识，回调结果
                isBusy = false;
                if (localReaderManager.getCallback() != null) {
                    localReaderManager.getCallback().onScanResult(deviceId, epcList);
                }
                stopScan();
            }
        }, timeOut);
    }

    /**
     * 取消发送结果
     */
    private void cancelSendComplete() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private ReadListener mReadListener = new ReadListener() {

        @Override
        public void tagRead(Reader reader, Reader.TAGINFO[] taginfos) {
            //循环扫描到的数据
            for (Reader.TAGINFO taginfo : taginfos) {
                String epc = Reader.bytes_Hexstr(taginfo.EpcId);
                //如果需要过滤（过滤规则不为空），并且标签不是以规则开始的，就去掉标签
                if (!TextUtils.isEmpty(filter) && !epc.startsWith(filter)) {
                    return;
                }
                EpcInfo info = new EpcInfo(taginfo.RSSI, taginfo.AntennaID, epc);
                //如果有这个标签就增加扫到一次
                if (epcList.containsKey(epc)) {
                    epcList.get(epc).add(info);
                } else {
                    //收到新标签就取消延时发送结果
                    cancelSendComplete();
                    if (localReaderManager.getCallback() != null) {
                        localReaderManager.getCallback().onScanNewEpc(deviceId, epc, taginfo.AntennaID);
                    }
                    //创建一个新的集合，保存数据，并存入list中
                    ArrayList<EpcInfo> list = new ArrayList<>();
                    list.add(info);
                    epcList.put(epc, list);
                    //处理完数据以后延时发送最终结果
                    sendComplete();
                }
            }
        }
    };
    /***
     * 异步盘点方式监听异常事件
     * */
    private ReadExceptionListener mExceptionListener = new ReadExceptionListener() {

        @Override
        public void tagReadException(Reader r, final Reader.READER_ERR re) {
            LogUtils.e("设备异常：：；" + re.value());
        }
    };

}
