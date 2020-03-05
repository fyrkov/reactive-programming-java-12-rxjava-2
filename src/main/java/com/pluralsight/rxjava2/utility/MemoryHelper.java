package com.pluralsight.rxjava2.utility;

public class MemoryHelper {

    public static long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static String freeMemory(long divisor, int decimalPlaces) {
        float decimalAmount = ((float)freeMemory() / (float)divisor);
        return String.format( "%1$,." + decimalPlaces + "f", decimalAmount);
    }
}
