package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendRejectionMail implements JavaDelegate {
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("xxx发送一份了拒绝申请的邮件");
    }
}
