/**
 * 
 */
package com.apoollo.commons.util.poi.excel.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class NestedProperties {

    private List<String> properties;

    private Map<String, NestedProperties> nestedProperties;

    public NestedProperties(List<String> properties, Map<String, NestedProperties> nestedProperties) {
        super();
        this.properties = properties;
        this.nestedProperties = nestedProperties;
    }

    public NestedProperties(List<String> properties) {
        this(properties, null);
    }

}
