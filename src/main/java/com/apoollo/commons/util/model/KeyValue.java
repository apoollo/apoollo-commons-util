/**
 * 
 */
package com.apoollo.commons.util.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeyValue<K, V> {

    private K key;
    private V value;
}
