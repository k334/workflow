package com.zsj.query;

import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableRule;
import org.flowable.task.api.Task;
import org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntity;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class testQuery {

    //默认使用flowable.cfg.xml的配置
    @Rule
    public FlowableRule flowableRule = new FlowableRule();

    @Test
    @Deployment(resources = "holiday-request.bpmn20.xml")
    public void testAND(){
        ProcessEngine processEngine = flowableRule.getProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //查询Bob的任务
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateGroup("managers")
                .processVariableValueEquals("employee","Bob")
                .orderByTaskDueDate().asc()
                .list();

        System.out.println("你有"+list.size()+"个任务:");
        for (int i = 0;i<list.size();i++) {
            System.out.println((i+1)+") "+list.get(i).getName());
        }

        processEngine.close();
    }

    @Test
    @Deployment(resources = "holiday-request.bpmn20.xml")
    public void testOR(){
        ProcessEngine processEngine = flowableRule.getProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        ManagementService managementService = processEngine.getManagementService();
        List<Task> list = taskService.createNativeTaskQuery()
                //managementService.getTableName(Task.class) == ACT_RU_TASK
                .sql("select * from "+ managementService.getTableName(Task.class) +
                        " t where t.NAME_ = #{name}")
                .parameter("name","Approve or reject request")
                .list();

        System.out.println("你有"+list.size()+"个任务:");
        System.out.println(list);

        processEngine.close();
    }
}
