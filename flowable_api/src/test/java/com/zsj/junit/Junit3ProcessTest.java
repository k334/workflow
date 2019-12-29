package com.zsj.junit;

import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestCase;
import org.flowable.task.api.Task;

public class Junit3ProcessTest extends FlowableTestCase {

    @Deployment(resources = "testJunit3ProcessTest.testSimpleProcess.bpmn20.xml")
    public void testSimpleProcess(){

        runtimeService.startProcessInstanceByKey("holidayRequest");

        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("approveTask",task.getName());
        taskService.complete(task.getId());
        assertEquals(0,runtimeService.createProcessInstanceQuery().count());
        processEngine.close();
    }
}
