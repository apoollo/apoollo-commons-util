/**
 * 
 */
package com.apoollo.commons.util.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@AllArgsConstructor
public class HttpClientResponse<T> {

    private int httpCode;
    private T body;
}
