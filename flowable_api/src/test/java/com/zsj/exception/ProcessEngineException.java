package com.zsj.exception;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessEngineException {

    @Rule
    public FlowableRule flowableRule = new FlowableRule();

    @Test
    @Deployment(resources = "testJunit4ProcessTest.testRuleUsageExample.bpmn20.xml")
    public void testObjectNotFound(){
        TaskService taskService = flowableRule.getTaskService();
        taskService.complete(null);

        RuntimeService runtimeService = flowableRule.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ruleUsage");
    }

    @Test
    @Deployment(resources = "testJunit4ProcessTest.testRuleUsageExample.bpmn20.xml")
    public void testFlowableException(){
        RuntimeService runtimeService = flowableRule.getRuntimeService();
        //流程定义文件中的一个类不能实例化
        runtimeService.startProcessInstanceByKey("ruleUsage");
    }

}
