package ognl;

import ognl.Ognl;
import ognl.OgnlException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    public static void main(String[] args) throws OgnlException, IOException {
        String templateContent = "{ \"queryType\": \"myQuery\", \"filter\": { "
                + "<#if name??> \"name\": \"${name}\", </#if>"
                + "<#if age??> \"age\": ${age}, </#if>"
                + "<#if status??> \"status\": \"${status}\", </#if>"
                + "<#if roles??> \"roles\": [ <#list roles as role> \"${role}\"<#if role_has_next>,</#if> </#list> ], </#if>"
                + "<#if choose == \"A\"> \"choose\": \"A\" <#elseif choose == \"B\"> \"choose\": \"B\" <#else> \"choose\": \"C\" </#if>"
                + "<#if users??> \"users\": [ <#foreach user in users> { \"name\": \"${user.name}\", \"age\": ${user.age} }<#if user_has_next>,</#if> </#foreach> ] </#if>"
                + "} }";

        Map<String, Object> root = new HashMap<>();
        root.put("name", "John");
        root.put("age", 30);
        root.put("status", "active");
        root.put("roles", new String[]{"admin", "user"});
        root.put("choose", "B");

        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");
        user1.put("age", 25);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");
        user2.put("age", 35);

        root.put("users", new Map[]{user1, user2});

        // 使用OGNL解析模板文件中的表达式
        Object parsedTemplate = Ognl.parseExpression(templateContent);
        Object parsedRoot = Ognl.parseExpression(root.toString());
        Object dataModel = Ognl.getValue(parsedTemplate, parsedRoot);

//        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
//        cfg.setNumberFormat("computer");
//        cfg.setDefaultEncoding("UTF-8");
//        cfg.setClassForTemplateLoading(ognl.JsonParser.class, "/");
//        Template template = new Template("myTemplate", templateContent, cfg);
//
//        StringWriter out = new StringWriter();
//        template.process(dataModel, out);

//        String json = out.toString();
//        System.out.println(json);
    }
}