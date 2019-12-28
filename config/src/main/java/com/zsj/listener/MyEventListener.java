package com.zsj.listener;

import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;

public class MyEventListener implements FlowableEventListener {

    // onEvent方法中的逻辑并不重要，可以忽略日志失败异常……
    @Override
    public void onEvent(FlowableEvent flowableEvent) {

    }

    /**
     * 决定了当事件分发后，onEvent方法抛出异常时的行为。
     *  如果返回false,忽略异常
     *  如果返回true,异常不会被忽略,而会向上抛,使当前执行的命令失败
     *  如果事件是API调用的一部分，事务将被回滚。
     * @return
     */
    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
