package com.s11web.util;

public class DecimalConversion {

    public static StringBuffer addOne(StringBuffer input) {
        if (input.length() != 5) {
            return null;
        }
        for (int i = input.length() - 1; i >= 0; --i) {
            if (input.charAt(i) == '9') {
                input.setCharAt(i, 'a');
                return input;
            } else if (input.charAt(i) == 'z') {
                input.setCharAt(i, 'A');
                return input;
            } else if (input.charAt(i) == 'Z') {
                input.setCharAt(i, '0');
            } else {
                input.setCharAt(i, (char) (input.charAt(i) + 1));
                return input;
            }
        }
        return input;
    }
}
