package org.example.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CountLetters {

    private CountLetters() {
    }

    public static Map<Character, Long> countWithForLoops(String letters) {
        var result = new HashMap<Character, Long>();
        var chars = letters.toCharArray();
        for (var c : chars) {
            var count = result.getOrDefault(c, 0L) + 1;
            result.put(c, count);
        }
        return result;
    }

    public static Map<Character, Long> countWithStreams(String letters) {
        return letters.chars().boxed()
                      .collect(Collectors.groupingBy(Function.identity()))
                      .entrySet().stream()
                      .collect(Collectors.toMap(e -> (char) e.getKey().intValue(), e -> (long) e.getValue().size()));
    }

    public static Map<Character, Long> countWithParallelStreams(String letters) {
        return letters.chars().parallel().boxed()
                      .collect(Collectors.groupingBy(Function.identity()))
                      .entrySet().parallelStream()
                      .collect(Collectors.toMap(e -> (char) e.getKey().intValue(), e -> (long) e.getValue().size()));
    }
}
