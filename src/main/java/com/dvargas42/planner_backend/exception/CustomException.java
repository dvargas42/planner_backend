package com.dvargas42.planner_backend.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class CustomException extends RuntimeException {
    
    public CustomException(String message, String... searchParamsMap) {
        super(CustomException.generateMessage(message,
            toMap(String.class, String.class, searchParamsMap)));
    }
  
    private static String generateMessage(String message, Map<String, String> searchParams) {
        return  message + " " + searchParams;
    }
  
    private static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
    
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
            .collect(HashMap::new,
                (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                Map::putAll);
    }
  }
  