package org.flowable;


import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HolidayRequest {

    private static final Logger logger = LoggerFactory.getLogger(HolidayRequest.class);

    public static void main(String[] args) {

        //1.创建流程引擎
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?useSSL=false&characterEncodind=utf-8")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine engine = cfg.buildProcessEngine();
        //2.部署流程定义

        //3.将流程定义部署到流程引擎中
        RepositoryService repositoryService = engine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("holiday-request.bpmn20.xml");
        Deployment deployment = deploymentBuilder.deploy();

        //4.验证流程定义是否部署在流程引擎中
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()     //创建一个流程定义的查询
                .deploymentId(deployment.getId())   //根据部署的id查询
                .singleResult();        //返回结果

        logger.info("找到流程定义信息：[id:{}, name:{}, key:{}]",processDefinition.getId()
                ,processDefinition.getName(),processDefinition.getKey());

        //5.输入表单数据
        Scanner scanner = new Scanner(System.in);
        System.out.println("你是谁？");
        String employee = scanner.nextLine();
        System.out.println("你想要请几天假？");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());
        System.out.println("请假的原因?");
        String description = scanner.nextLine();

        //6.得到运行时Service，根据这个Service启动流程
        RuntimeService runtimeService = engine.getRuntimeService();

        //7.收集数据并且启动流程实例
        Map<String,Object> variables = new HashMap<>();
        variables.put("employee",employee);
        variables.put("nrOfHolidays",nrOfHolidays);
        variables.put("description",description);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(),variables);


        //8.查看提交的任务列表
        TaskService taskService = engine.getTaskService();
        List<Task> list = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("你有"+list.size()+"个任务:");
        for (int i = 0;i<list.size();i++) {
            System.out.println((i+1)+") "+list.get(i).getName());
        }

        //9.使用任务Id获取特定的流程实例变量
        System.out.println("哪一个任务你想要完成?");
        int taskIndex = Integer.valueOf(scanner.nextLine());
        Task task = list.get(taskIndex-1);
        Map<String,Object> processVariables = taskService.getVariables(task.getId());
        System.out.println(processVariables.get("employee") + "想要请假" +
                processVariables.get("nrOfHolidays") + "天，您是否批准?");

        //10.执行者进行审批,审批完,一个完整流程就结束了
        boolean approved = scanner.nextLine().toLowerCase().equals("y");
        variables = new HashMap<>();
        variables.put("approved",approved);
        taskService.complete(task.getId(),variables);

//        //11.查看历史数据，显示流程实例已经执行的时间
        HistoryService historyService = engine.getHistoryService();
        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();

        for (HistoricActivityInstance h : activities) {
            System.out.println(h.getActivityId() + " 花费了 " + h.getDurationInMillis() + "毫秒");
        }
        scanner.close();
    }
}
