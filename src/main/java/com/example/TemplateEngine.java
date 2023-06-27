package com.example;

import ognl.OgnlException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {
    public static String processIf(String template, Map<String, Object> context) {
        String result = template;

        Pattern pattern = Pattern.compile("\\$\\{if (.+?)\\}(.+?)\\$\\{endif\\}");
        Matcher matcher = pattern.matcher(result);

        while (matcher.find()) {
            String condition = matcher.group(1);
            String content = matcher.group(2);

            boolean flag = false;

            try {
                flag = (Boolean) OgnlUtils.getValue(context, condition);
            } catch (OgnlException e) {
                e.printStackTrace();
            }

            if (flag) {
                result = StringUtils.replace(result, matcher.group(), content);
            } else {
                result = StringUtils.replace(result, matcher.group(), "");
            }
        }

        return result;
    }

    public String render(String template, Map<String, Object> context){
        String string="";
        return string;
    }
}
