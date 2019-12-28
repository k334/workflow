package com.zsj.config;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class testConfig {

    private static final Logger logger = LoggerFactory.getLogger(testConfig.class);

    //如果不是使用jdbc连接下面信息则不匹配
    private static void printInfo(ProcessEngineConfiguration processEngineConfiguration){
        System.out.println("数据库连接驱动:"+processEngineConfiguration.getJdbcDriver());
        System.out.println("数据库地址:"+processEngineConfiguration.getJdbcUrl());
        System.out.println("数据库密码:"+processEngineConfiguration.getJdbcPassword());
    }

    @Test
    public void test01(){
        // 1.1 使用默认的xml文件来创建流程引擎配置对象
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        printInfo(processEngineConfiguration);
    }

    @Test
    public void test02(){
        // 1.2 使用新定义的xml文件,创建流程引擎配置对象
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("flowable_durid.cfg.xml");
        logger.info("configuration = {}",processEngineConfiguration);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        logger.info("流程引擎名称 {}",processEngine.getName()); //默认为default

        processEngine.close();
    }

    @Test
    public void test03(){
        // 1.3 使用新定义的xml文件并且指定哪个bean,创建流程引擎配置对象
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("flowable_dbcp.cfg.xml","processEngineConfiguration");
        logger.info("configuration = {}",processEngineConfiguration);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        logger.info("流程引擎名称 {}",processEngine.getName()); //默认为default

        processEngine.close();
    }

    @Test
    public void test04(){
        // 2.1 不使用xml,直接创建一个对象
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?useSSL=false")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcMaxActiveConnections(10) //连接池能够容纳的最大活动连接数量
                .setDatabaseSchemaUpdate("true")
                .buildProcessEngine();
        logger.info("流程引擎名称 {}",processEngine.getName()); //默认为default
    }

    @Test
    public void test05(){
        //2.2 通过new创建一个流程配置对象，默认使用的数据库为h2，数据库表结构策略为create_drop
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?useSSL=false")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setDatabaseSchemaUpdate("true")
                .buildProcessEngine();

        logger.info("流程引擎名称 {}",processEngine.getName()); //默认为default
    }
}
