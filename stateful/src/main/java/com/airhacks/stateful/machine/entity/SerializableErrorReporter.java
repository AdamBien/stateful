package com.airhacks.stateful.machine.entity;

import java.io.Serializable;
import org.apache.commons.scxml2.ErrorReporter;

/**
 * TODO: workaround for SCXML bug
 *
 * @author airhacks.com
 */
public class SerializableErrorReporter implements ErrorReporter, Serializable {

    @Override
    public void onError(String errCode, String errDetail, Object errCtx) {
        System.err.println(errCode + "-" + errDetail + "-" + errCtx);
    }

}
