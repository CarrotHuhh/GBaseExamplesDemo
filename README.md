# 项目工程结构
```xml
.
├── GbaseDemo.iml
├── README.md
├── lib
│   ├── gbase-connector-java-9.5.0.1-build1-bin.jar
│   ├── hamcrest-core-1.3.jar
│   ├── jsch-0.1.55.jar
│   └── junit-4.12.jar
├── out
│   └── production
│       └── GbaseDemo
│           └── com
│               └── gbase
│                   ├── ConnectionConfig.class
│                   ├── Mode.class
│                   ├── Operations.class
│                   └── OperationsTest.class
└── src
    └── com
        └── gbase
            ├── ConnectionConfig.java
            ├── Mode.java
            ├── Operations.java
            └── OperationsTest.java
```

# 结构说明
项目源代码位于src/com/gbase路径下，其中ConnectionConfig类为连接前预配置，定义了与集群连接的相关参数。
Operations类继承自ConnectionConfig类，在创建Operations对象时将根据父类中构建方法完成Connection对象的构建；Operations类中封装了有关JDBC相关特性操作的方法。
Mode类中封装了有关设定连接模式的相关方法。__这些方法的调用需要发生在Operations对象的创立（也就是Connection对象生成）之前，方可生效。__
OperationsTest类采用Junit测试的形式对JDBC各特性的使用进行演示。
lib中共有四个jar包，其中gbase-connector-java-9.5.0.1-build1-bin.jar为gbase集群驱动；jsch-0.1.55.jar用于SSH隧道连接；junit-4.12.jar和hamcrest-core-1.3.jar为本工程采用Junit进行演示所使用，并非与集群连接所必需。
