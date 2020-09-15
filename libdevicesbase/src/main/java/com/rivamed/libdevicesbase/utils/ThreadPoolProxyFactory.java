package com.rivamed.libdevicesbase.utils;

/**
 * 描述:     线程池工厂类
 *
 * @author : YiCH
 * @date : 2018/8/30 0030.
 */
public class ThreadPoolProxyFactory {
    private static ThreadPoolProxy mNormalThreadPoolProxy;

    private ThreadPoolProxyFactory() {

    }

    /**
     * 得到普通线程池代理对象mNormalThreadPoolProxy
     */
    public static synchronized ThreadPoolProxy getThreadPoolProxy() {

        if (mNormalThreadPoolProxy == null) {
            mNormalThreadPoolProxy = new ThreadPoolProxy(16, 16);
        }
        return mNormalThreadPoolProxy;
    }
}

