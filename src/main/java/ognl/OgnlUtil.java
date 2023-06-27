package ognl;

import com.alibaba.fastjson.JSONObject;

/**
 * @program: java-deep
 * @description 工具类：使用Ognl对象图导航语言,获取JSON多层嵌套数据
 * @author: DONGSHILEI
 * @create: 2020/5/20 16:26
 **/
public class OgnlUtil {
    private OgnlContext context;

    public OgnlUtil context(OgnlContext context) {
        this.context = context;
        return this;
    }

    public static OgnlUtil build() {
        return new OgnlUtil();
    }

    public OgnlUtil add(JSONObject source) {
        return this.add(null, source);
    }

    public OgnlUtil add(String key, JSONObject source) {
        if (this.context == null) {
            this.context = new OgnlContext();
        }
        if (key == null) {
            this.context.setRoot(source);
        } else {
            this.context.put(key, source);
        }
        return this;
    }

    public static Object getValue(OgnlContext context, String dsl) throws OgnlException {
        Object mapValue = Ognl.getValue(dsl, context, context.getRoot());
        return mapValue;
    }

    public Object getValue(String dsl) throws OgnlException {
        Object mapValue = Ognl.getValue(dsl, this.context, this.context.getRoot());
        return mapValue;
    }
}