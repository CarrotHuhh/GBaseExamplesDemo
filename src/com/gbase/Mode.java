package com.gbase;

/**
 * @ClassName Mode.java
 * @Description 本类中封装了JDBC各种特性的开启方法
 */
public class Mode {
    /**
     * 开启高可用，该方法的调用一般伴随着下方setHostList(String hostList)方法的调用
     */
    public static void enableHighAvailability() {
        ConnectionConfig.URL = ConnectionConfig.URL + "&failoverEnable=true&gclusterId=gcl1";
    }

    /**
     * 设置高可用服务器
     *
     * @param hostList: 传入服务器集群IP地址,多个IP之间用","隔开，如0.0.0.1,0.0.0.2
     */
    public static void setHostList(String hostList) {
        ConnectionConfig.URL = ConnectionConfig.URL + "&hostList=" + hostList;
    }


    /**
     * 开启SSL连接,SSL连接需配置所用keyStore和trustStore及其密码，对应参数在ConnectionConfig.java中进行配置。
     */
    public static void enableSSL() {
        ConnectionConfig.URL = ConnectionConfig.URL + "&useSSL=true&requireSSL=true";
        //配置keyStore和trustStore的路径
        System.setProperty("javax.net.ssl.keyStore", ConnectionConfig.keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", ConnectionConfig.keyStorePassword);
        System.setProperty("javax.net.ssl.trustStore", ConnectionConfig.trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", ConnectionConfig.trustStorePassword);
    }

    /**
     * 进行批量插入
     */
    public static void enableBatchMode() {
        ConnectionConfig.URL = ConnectionConfig.URL + "&rewriteBatchedStatements=true";
    }

    /**
     * 开启SSH隧道连接
     */
    public static void enableSSHTunneling() {
        ConnectionConfig.isTunneling = true;
    }

    /**
     * 关闭SSH隧道连接
     */
    public static void stopSSHTunneling() {
        ConnectionConfig.isTunneling = false;
    }

    /**
     * 开启驱动日志
     */
    public static void enableProfile() {
        ConnectionConfig.URL = ConnectionConfig.URL + "&profileSql=true";
    }

    /**
     * 设置字符集
     */
    public static void setCharacterSet(String characterEncoding, String characterSetResults) {
        ConnectionConfig.URL = ConnectionConfig.URL + "&useUnicode=true&characterEncoding=" + characterEncoding + "&characterSetResults=" + characterSetResults;
    }

}
