package cn.rivamed.test;

import com.rivamed.libdevicesbase.base.FunctionCode;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import cn.rivamed.Eth002Manager;
import cn.rivamed.callback.Eth002HandlerInterface;
import cn.rivamed.callback.Eth002MessageListener;

public class TestEth002Handler implements Eth002HandlerInterface{

    private String identification = "B0550101030006";
    private String fingerData = "B005010120000200280000000000000000000000000000004B01B055010103000600000001000000000000000000000000000B01C455010103000655F30101030001000000000000000000000000000000000005015D55010103000600000001000000000000000000000000000B01AA55010120000200280000000000000000000000000000004B01AA55010103000600000001000000000000000000000000000B016H55010103000655AA010103000100000000000000000000000000000000000501AA55010103000600000001000000000000000000000000000B01";
    private Eth002Manager eth002Manager;
    boolean fingerRegisterModel;
    boolean waitFingerReg;
    private Eth002MessageListener eth002MessageListener;
    private ScheduledThreadPoolExecutor scheduled;

    public TestEth002Handler(Eth002Manager eth002Manager) {
        this.eth002Manager = eth002Manager;
    }

    @Override
    public String getIdentification() {
        return identification;
    }

    @Override
    public int openDoor() {
        return FunctionCode.SUCCESS;
    }

    @Override
    public int checkLockState() {
        return 0;
    }

    @Override
    public int openLight() {
        return 0;
    }

    @Override
    public int closeLight() {
        return 0;
    }

    @Override
    public String getRemoteIP() {
        return "192.168.XXX.10";
    }

    @Override
    public String getProducer() {
        return "Eth";
    }

    @Override
    public String getVersion() {
        return "V2.0";
    }

    @Override
    public int closeChannel() {
        return 0;
    }

    @Override
    public int fingerReg() {
        if (fingerRegisterModel || waitFingerReg){
            return FunctionCode.DEVICE_BUSY;
        }
        if (scheduled == null){
            ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("test-eth002-schedule-pool-%d").daemon(true).build();
            scheduled = new ScheduledThreadPoolExecutor(1,namedThreadFactory);
        }
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (eth002Manager.getEth002CallBack() != null){
                    eth002Manager.getEth002CallBack().onFingerRegisterRet(identification,true,fingerData);
                }
            }
        });
        return FunctionCode.SUCCESS;
    }

    @Override
    public void registerMessageListener(Eth002MessageListener eth002MessageListener){
        this.eth002MessageListener = eth002MessageListener;
        this.eth002MessageListener.setEth002HandlerInterface(this);
    }
}
