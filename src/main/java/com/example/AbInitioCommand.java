package com.example;

import java.util.Map;

public class AbInitioCommand {
    private String template;
    private Map<String, Object> parameters;

    // getters and setters


    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
