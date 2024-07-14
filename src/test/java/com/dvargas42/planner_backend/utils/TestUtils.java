package com.dvargas42.planner_backend.utils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {
    
    public static String convertDateTime(LocalDateTime date) {
        int nanos = date.getNano();
        String formattedNanos = String.format("%04d", nanos / 100000);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return date.format(formatter) + "." + formattedNanos;
    }

    public static int getDeclaratedFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        return fields.length;
    }
}
