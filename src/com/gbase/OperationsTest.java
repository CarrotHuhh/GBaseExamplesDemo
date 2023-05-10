package com.gbase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 本类对JDBC各功能特性采用测试的形式进行使用演示，lib目录下junit-4.12.jar以及hamcrest-core-1.3.jar
 * 为本类测试所用包，与JDBC无关
 * 本类中每个样例若调用Mode类中方法对模式进行设置，则需在创建Operations对象之前进行，在创建Operations对象时便
 * 会建立与集群的连接对象，因此在创建对象之后调用的Mode类方法无效。
 */
public class OperationsTest {
    /**
     * 运行前初始化，查找数据库中是否存在示例用表，若无则调用建表方法进行创建。
     */
    @Before
    public void init() {
        try {
            Operations operationsInit = new Operations();
            Statement stmt = ConnectionConfig.conn.createStatement();
            ResultSet rs1 = stmt.executeQuery("show tables");
            while (rs1.next()) {
                String str = rs1.getString(1);
                if (str.equals("testtable")) {
                    return;
                }
            }
            operationsInit.createTable();
            System.out.println("创建示例表");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行完毕后关闭与集群的连接
     */
    @After
    public void closeConnect() {
        try {
            ConnectionConfig.conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 该样例展示了使用批量插入与不使用批量插入所带来的效率差异：
     * operations1对象创建在调用Mode.enableBatchMode()之前，只有在调用Mode.enableBatchMode()之后为
     * URL设置了rewriteBatchedStatements=true参数，才开启批量插入。operations2为开启了批量插入的连接对象
     *
     * @throws SQLException
     */
    @Test
    public void insertBatchExample() throws SQLException {
        Operations operations1 = new Operations();
        operations1.insertBatch(10);
        Mode.enableBatchMode();
        Operations operations2 = new Operations();
        operations2.insertBatch(100);
    }

    /**
     * 该样例为流式读取数据
     *
     * @throws SQLException
     */
    @Test
    public void streamReadExample() throws SQLException {
        Operations operations = new Operations();
        operations.streamRead();
    }

    /**
     * 在调用Mode.enableHighAvailability()修改URL开启高可用后，通常伴随着调用Mode.setHostList()设置远程集群的地址，
     *
     * @throws SQLException
     */
    @Test
    public void loadBalancingExample() throws SQLException {
        Mode.enableHighAvailability();
        Mode.setHostList("172.16.34.202,172.16.34.203");
        Operations operations = new Operations();
        operations.loadBalancing();
    }

    /**
     * SSL连接样例
     *
     * @throws SQLException
     */
    @Test
    public void SSLExample() throws SQLException {
        Mode.enableSSL();
        Operations operations = new Operations();
        operations.insertBatch(20);
    }

    /**
     * 开启驱动日志样例
     *
     * @throws SQLException
     */
    @Test
    public void profileExample() throws SQLException {
        Mode.enableProfile();
        Operations operations = new Operations();
        operations.streamRead();
    }

    /**
     * 获取加载任务信息样例
     *
     * @throws SQLException
     */
    @Test
    public void loadSqlExample() throws SQLException {
        Operations operations = new Operations();
        operations.loadTaskInfo();
    }

    /**
     * SSH隧道连接样例
     *
     * @throws SQLException
     */
    @Test
    public void SSHTunnelExample() {
        Mode.enableSSHTunneling();
        Operations operations = new Operations();
        try {
            operations.streamRead();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Mode.stopSSHTunneling();
        }
    }

    /**
     * 设置字符集样例
     */
    @Test
    public void characterSetExample() {
        Mode.setCharacterSet("utf8", "utf8");
        System.out.println(ConnectionConfig.URL);
    }

    @Test
    public void connectionTest(){
        ConnectionConfig connectionConfig = new ConnectionConfig();
        assert ConnectionConfig.conn!=null;
    }
}
