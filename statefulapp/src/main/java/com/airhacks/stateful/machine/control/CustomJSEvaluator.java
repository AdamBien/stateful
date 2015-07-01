package com.airhacks.stateful.machine.control;

import java.io.Serializable;
import org.apache.commons.scxml2.Context;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.env.javascript.JSEvaluator;

/**
 * A hook for debugging purposes
 *
 * @author airhacks.com
 */
public class CustomJSEvaluator extends JSEvaluator implements Serializable {

    @Override
    public Object eval(Context context, String expression) throws SCXMLExpressionException {
        return super.eval(context, expression);
    }

}
