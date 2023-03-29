package com.cqupt.electroniccase.domain.model;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.Map;

/**
 * @auther DyingZhang
 * @Create 2023-03-29 下午 8:40
 * @Discriptioon
 */
public class ESResult {
    private Map<String, Object> map;

    public ESResult() {
    }

    public ESResult data(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Map<String, Object> getData() {
        return map;
    }
}
