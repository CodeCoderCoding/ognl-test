package ognl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonTemplate {

    public static void main(String[] args) throws IOException, OgnlException {
        // 加载JSON文件
        ObjectMapper objectMapper = new ObjectMapper();
//        File jsonFile = new File("template.json");
        File jsonFile = new File(JsonTemplate.class.getClassLoader().getResource("template.json").getFile());
        ObjectNode templateNode = (ObjectNode) objectMapper.readTree(jsonFile);

        // 创建OGNL上下文
        OgnlContext context = new OgnlContext();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Alice");
        user.put("age", 18);
        user.put("address", "Beijing");
        user.put("phone", null);
        user.put("school", "MIT");
        context.setRoot(user);

        // 解析JSON模板并构造JSON对象
        ObjectNode rootNode = objectMapper.createObjectNode();
        templateNode.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            String template = entry.getValue().asText();
            if (template.startsWith("${") && template.endsWith("}")) {
                String expression = template.substring(2, template.length() - 1).trim();
                try {
                    Object value = Ognl.getValue(expression, context, context.getRoot());
                    rootNode.putPOJO(key, value);
                } catch (OgnlException e) {
                    e.printStackTrace();
                }
            } else {
                rootNode.put(key, template);
            }
        });

        // 输出结果
        objectMapper.writeValue(new File("output.json"), rootNode);
    }

}