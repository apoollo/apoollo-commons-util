package com.apoollo.commons.util;

public class CodePointAndCodeUnitExample {
    public static void main(String[] args) {
        String text = "A你";

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int codePoint = (int) c;

            System.out.println("Character: " + c);
            System.out.println("Code Point (Decimal): " + codePoint);
            System.out.println("Code Point (Hex): U+" + Integer.toHexString(codePoint).toUpperCase());

            // UTF-8
            byte[] utf8Bytes = text.substring(i, i + 1).getBytes(java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("UTF-8 Code Units: " + bytesToHex(utf8Bytes));

            // UTF-16
            char[] utf16Chars = text.substring(i, i + 1).toCharArray();
            System.out.println("UTF-16 Code Units: " + charsToHex(utf16Chars));

            // UTF-32
            int[] utf32Ints = new int[] { codePoint };
            System.out.println("UTF-32 Code Units: " + intsToHex(utf32Ints));

            System.out.println();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    private static String charsToHex(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(String.format("%04X ", (int) c));
        }
        return sb.toString().trim();
    }

    private static String intsToHex(int[] ints) {
        StringBuilder sb = new StringBuilder();
        for (int i : ints) {
            sb.append(String.format("%08X ", i));
        }
        return sb.toString().trim();
    }
}