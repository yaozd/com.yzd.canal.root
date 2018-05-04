package com.yzd.example2.h5.utils.timeVersionIdExt;

public class TimeVersionUtil2 {
    static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
    public static void checkState(boolean b, String errorMessageTemplate, long p1, long p2) {
        if (!b) {
            throw new IllegalStateException(String.format(errorMessageTemplate, p1, p2));
        }
    }
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }
}
