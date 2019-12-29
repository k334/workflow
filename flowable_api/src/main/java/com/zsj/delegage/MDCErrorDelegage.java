package com.zsj.delegage;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MDCErrorDelegage implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(MDCErrorDelegage.class);

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("run MDCErrorDelegage");
        //throw new RuntimeException("only test");
    }
}
