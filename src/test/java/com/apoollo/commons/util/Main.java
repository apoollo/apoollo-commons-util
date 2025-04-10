/**
 * 
 */
package com.apoollo.commons.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author liuyulong
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        pfBytesLength("1", StandardCharsets.UTF_16LE);
        pfBytesLength("12", StandardCharsets.UTF_16LE);
        pfBytesLength("a", StandardCharsets.UTF_16LE);
        pfBytesLength("ab", StandardCharsets.UTF_16LE);
        pfBytesLength("12ab", StandardCharsets.UTF_16LE);
        pfBytesLength("中", StandardCharsets.UTF_16LE);
        pfBytesLength("中文", StandardCharsets.UTF_16LE);
        pfBytesLength("中12ab文", StandardCharsets.UTF_16LE);

    }

    public static void pfBytesLength(String input, Charset charset) {

        System.out.println(input + " - " + input.getBytes(charset).length);
    }
}
