package com.zsj.config.junit;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableRule;
import org.flowable.task.api.Task;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Junit4ProcessTest {

    @Rule
    public FlowableRule flowableRule = new FlowableRule();

    @Test
    @Deployment(resources = "testJunit4ProcessTest.testRuleUsageExample.bpmn20.xml")
    public void ruleUsageExample(){

        RuntimeService runtimeService = flowableRule.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ruleUsage");

        TaskService taskService = flowableRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("approveTask", task.getName());

        taskService.complete(task.getId());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());

        flowableRule.getProcessEngine().close();
    }
}
