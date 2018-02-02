package com.hospital.util;

public class CalculateUtils {
    public static Integer getMax(Integer[] arr) {
        Integer max = arr[0];
        for (Integer x = 1; x < arr.length; x++) {
            if (arr[x] > max)
                max = arr[x];
        }
        return max;
    }
}
