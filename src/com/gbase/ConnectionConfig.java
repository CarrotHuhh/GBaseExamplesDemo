package com.gbase;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 本类为ConnectionConfig类，为Operation的父类，负责完成数据库操作的前置连接配置
 */
//驱动基本配置类
public class ConnectionConfig {
    /**
     * URL中可进行模式配置,可支持ipv4或ipv6地址
     * user:数据库用户名，password:数据库用户密码
     * useSSL=true+requireSSL=true:开启与服务器的SSL连接
     * failoverEnable=true：开启高可用。若该参数设置为true，则需同时配置hostList参数，hostList中配置集群其他主机ip地址
     * rewriteBatchedStatements=true：开启批量插入
     * vcName=vc1:选择要操作的vc
     */
    final public static String ipv4Addr = "172.16.34.201";
    final public static String ipv6Addr = "[fe80::cfc6:e2e5:f035:a93e]";
    final public static String trustStorePath = "/Users/huyiquan/Study/gbase/configs/ssl/truststore";
    final public static String trustStorePassword = "Hu123456";
    final public static String keyStorePath = "/Users/huyiquan/Study/gbase/configs/ssl/keystore";
    final public static String keyStorePassword = "Hu123456";
    public static String URL = "jdbc:gbase://" + ipv4Addr + ":5258/db1?" + "vcName=vc1" + "&user=gbase" + "&password=Hu123456";
    public static Boolean isTunneling = false;
    public static Connection conn = null;
    final public String DRIVER = "com.gbase.jdbc.Driver";

    public ConnectionConfig() {
        try {
            //注册gbase的JDBC驱动
            Class.forName(DRIVER);

        } catch (Exception e) {
            e.getMessage();
            System.out.println("注册驱动失败");
        }

        //使用SSH隧道连接
        if (isTunneling) {
            try {
                //创建session连接
                JSch jsch = new JSch();
                Session session = jsch.getSession("gbase", "172.16.34.201", 22);
                session.setPassword("Hu123456");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                //端口映射，session.setPortForwardingL(localHost,localPort, remoteHost, remotePort)
                session.setPortForwardingL("172.16.34.14", 5258, "172.16.34.201", 5258);
                conn = DriverManager.getConnection(URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //采用直接连接
            try {
                conn = DriverManager.getConnection(URL);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("连接失败");
            }
        }
    }
}