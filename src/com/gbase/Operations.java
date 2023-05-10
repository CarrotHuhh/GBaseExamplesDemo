package com.gbase;

import com.gbase.jdbc.ConnectionImpl;
import com.gbase.jdbc.StatementImpl;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 本类封装了若干种JDBC特性的操作方法，继承自ConnectionConfig类
 */
public class Operations extends ConnectionConfig {

    /**
     * 创建一张50个字段的表，字段均为varchar(20)类型
     *
     * @throws SQLException
     */
    public void createTable() throws SQLException {
        String sql = "create table testtable(v0 varchar(20),v1 varchar(20),v2 varchar(20),v3 varchar(20),v4 varchar(20),v5 varchar(20),v6 varchar(20),v7 varchar(20),v8 varchar(20),v9 varchar(20),v10 varchar(20),v11 varchar(20),v12 varchar(20),v13 varchar(20),v14 varchar(20),v15 varchar(20),v16 varchar(20),v17 varchar(20),v18 varchar(20),v19 varchar(20),v20 varchar(20),v21 varchar(20),v22 varchar(20),v23 varchar(20),v24 varchar(20),v25 varchar(20),v26 varchar(20),v27 varchar(20),v28 varchar(20),v29 varchar(20),v30 varchar(20),v31 varchar(20),v32 varchar(20),v33 varchar(20),v34 varchar(20),v35 varchar(20),v36 varchar(20),v37 varchar(20),v38 varchar(20),v39 varchar(20),v40 varchar(20),v41 varchar(20),v42 varchar(20),v43 varchar(20),v44 varchar(20),v45 varchar(20),v46 varchar(20),v47 varchar(20),v48 varchar(20),v49 varchar(20))";
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        System.out.println("表格创建成功，共50个字段");
    }

    /**
     * 采用批量插入方法进行数据插入，大幅提高插入效率
     *
     * @param n：插入数据数量
     * @throws SQLException
     */
    public void insertBatch(int n) throws SQLException {
        String sql = "insert into testtable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        // 计时开始
        Long beginTime = System.currentTimeMillis();
        System.out.println("数据插入开始");
        try {
            //进行预处理
            PreparedStatement stm = conn.prepareStatement(sql);
            for (int j = 0; j < n; j++) {
                for (int i = 1; i <= 50; i++) {
                    stm.setString(i, String.valueOf(j));
                }
                // 添加批量插入内容
                stm.addBatch();
            }
            // 执行批量插入
            stm.executeBatch();
        } catch (Exception e) {
            System.out.println("数据插入失败");
        } finally {
            conn.close();
        }
        // 计时结束
        Long endTime = System.currentTimeMillis();
        System.out.println("数据处理以及插入共耗时：" + (endTime - beginTime) + "ms");
    }

    /**
     * 流式读取数据，逐条从集群获取数据，减小大结果集对应用内存的影响
     *
     * @throws SQLException
     */
    public void streamRead() throws SQLException {
        String sql = "select * from testtable";
        System.out.println("流式读取开始：");
        try {
            Statement streamStmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            //Integer.MIN_VALUE=-2147483648,该参数必须是固定值，
            //设置为Integer.MIN_VALUE时，JDBC将尝试使用最佳的流式读取行数
            streamStmt.setFetchSize(Integer.MIN_VALUE);
            ResultSet rs = streamStmt.executeQuery(sql);
            while (rs.next()) {
                // 必须遍历完所有结果集
                System.out.println(rs.getString("v0"));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    /**
     * 验证负载均衡，集群在开启轮询策略负载均衡后，会将连接请求逐个分配给集群中的主机，这里采用反射的方法获取连接的
     * 服务器ip地址，对负载均衡的开启情况进行验证。
     *
     * @throws SQLException
     */
    public void loadBalancing() throws SQLException {
        System.out.println("输出每次连接的服务器ip地址：");
        for (int i = 1; i <= 6; i++) {
            try {
                ConnectionConfig.conn = DriverManager.getConnection(ConnectionConfig.URL);
                //反射拆解对象
                Class<ConnectionImpl> clazz = ConnectionImpl.class;
                Field host = clazz.getDeclaredField("host");
                host.setAccessible(true);
                Object serverIP = host.get(ConnectionConfig.conn);
                System.out.println("serverIP:" + serverIP);
                ConnectionConfig.conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * JDBC支持用于获取加载任务ID号，加载数据跳过行数的功能。因为JDBC标准接口并不包含该方法定义，故用
     * 户在使用时需要将标准的Statement转化为com.gbase.jdbc.StatementImpl类型方可使用。
     * 在86版本，是不支持本地文件加载的，必须是ftp，sftp，http,hadoop等数据源的形式，
     *
     * @throws SQLException
     */
    public void loadTaskInfo() throws SQLException {
        String loadSql = "load data infile 'file://root@172.16.34.201/tmp/1.txt' into table testtable fields terminated by ','";
        try {
            Connection conn = DriverManager.getConnection(ConnectionConfig.URL);
            StatementImpl stmt = (StatementImpl) conn.createStatement();
            stmt.executeUpdate(loadSql);
            long skippedLines = stmt.getSkippedLines();
            long taskID = stmt.getLoadTaskID();
            System.out.println("skippedLines: " + skippedLines);
            System.out.println("taskID: " + taskID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

}