package com.example;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class OgnlUtils {
    public static Object getValue(Object object, String expression) throws OgnlException {
        OgnlContext context = new OgnlContext();
        return Ognl.getValue(expression, context, object);
    }

    public static void setValue(Object object, String expression, Object value) throws OgnlException {
        OgnlContext context = new OgnlContext();
        Ognl.setValue(expression, context, object, value);
    }
}
