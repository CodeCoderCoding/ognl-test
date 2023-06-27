package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class AbInitioCommandAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Before("execution(* com.example.AbInitioService.execute(..)) && args(command,..)")
    public void beforeExecute(AbInitioCommand command) throws Exception {
        String template = command.getTemplate();
        Map<String, Object> parameters = command.getParameters();

        // 解析模板语法
        template = processTemplate(template, parameters);

        // 根据模板和参数生成 Ab Initio 查询命令
        String commandString = generateCommand(template, parameters);

        command.setTemplate(commandString);
    }

    private String processTemplate(String template, Map<String, Object> context) {
        template = processIf(template, context);
        template = processChoose(template, context);
        template = processForeach(template, context);
        return template;
    }

    private String processIf(String template, Map<String, Object> context) {
        Pattern pattern = Pattern.compile("\\$\\{if (.+?)\\}(.+?)\\$\\{endif\\}");
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String condition = matcher.group(1);
            String content = matcher.group(2);

            boolean flag = false;

            try {
                flag = (Boolean) OgnlUtils.getValue(context, condition);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (flag) {
                template = StringUtils.replace(template, matcher.group(), content);
            } else {
                template = StringUtils.replace(template, matcher.group(), "");
            }
        }

        return template;
    }

    private String processChoose(String template, Map<String, Object> context) {
        Pattern pattern = Pattern.compile("\\$\\{choose\\}(.+?)\\$\\{endchoose\\}");
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String content = matcher.group(1);

            int index = content.indexOf("${when");
            if (index == -1) {
                template = StringUtils.replace(template, matcher.group(), "");
                continue;
            }

            String defaultContent = "";

            int endIndex = content.lastIndexOf("${otherwise}");
            if (endIndex != -1) {
                defaultContent = content.substring(endIndex + 13);
                content = content.substring(0, endIndex);
            }

            boolean match = false;

            while (index != -1) {
                int end = content.indexOf("}", index) + 1;
                String condition = content.substring(index + 2, end - 1);
                String whenContent = "";

                int nextIndex = content.indexOf("${when", end);
                if (nextIndex == -1) {
                    whenContent = content.substring(end);
                    index = -1;
                } else {
                    whenContent = content.substring(end, nextIndex);
                    index = nextIndex;
                }

                try {
                    match = (Boolean) OgnlUtils.getValue(context, condition);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (match) {
                    template = StringUtils.replace(template, matcher.group(), whenContent);
                    break;
                }
            }

            if (!match) {
                template = StringUtils.replace(template, matcher.group(), defaultContent);
            }
        }

        return template;
    }

    private String processForeach(String template, Map<String, Object> context) {
        Pattern pattern = Pattern.compile("\\$\\{foreach (\\w+?) in (\\w+?)\\}(.+?)\\$\\{endfor\\}");
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String variable = matcher.group(1);
            String listName = matcher.group(2);
            String content = matcher.group(3);

            List<Object> list = new ArrayList<>();

            try {
                list = (List<Object>) OgnlUtils.getValue(context, listName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            StringBuilder builder = new StringBuilder();

            for (Object item : list) {
                Map<String, Object> tempContext = new HashMap<>();
                tempContext.put(variable, item);

                String result = processTemplate(content, tempContext);
                builder.append(result);
            }

            template = StringUtils.replace(template, matcher.group(), builder.toString());
        }

        return template;
    }

    private String generateCommand(String template, Map<String, Object> context) throws Exception {
        String commandString = "";

        // 将模板转换为 Ab Initio 命令
        TemplateEngine engine = new TemplateEngine();
        commandString = engine.render(template, context);

        return commandString;
    }
}
