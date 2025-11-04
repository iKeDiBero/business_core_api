package com.kedibero.business_core_api.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public class SpecsDTO {
    private Map<String, Object> specs = new HashMap<>();

    @JsonAnySetter
    public void setDynamicProperty(String name, Object value) {
        specs.put(name, value);
    }

    public Object get(String key) {
        return specs.get(key);
    }

    public void put(String key, Object value) {
        specs.put(key, value);
    }

    public Map<String, Object> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, Object> specs) {
        this.specs = specs;
    }

    @JsonValue
    public Map<String, Object> asJson() {
        return specs;
    }

    @Override
    public String toString() {
        return specs.toString();
    }
}
