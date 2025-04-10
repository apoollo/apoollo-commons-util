/**
 * 
 */
package com.apoollo.commons.util.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@AllArgsConstructor
public class Crypto {

    private String provider;
    private String algorithm;
    private String transformation;

}
