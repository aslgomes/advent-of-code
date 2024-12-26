package com.project.adventofcode.year2024.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    // Key aspects of the solution:
    //
    // Part 1 - uses recursion and memoization to determine whether a string (word) can be segmented into substrings
    // from a set of patterns (patterns). Starting from a given index, the function checks if any pattern in patterns
    // matches the substring of word beginning at the current index.
    //
    // If a match is found, it recursively checks if the rest of the string is valid starting from the next position
    // (index + length of the matched pattern).
    //
    // Part 2 - the approach is similar to what has been done in Part 1 but instead of caching whether building the
    // string from the set of patterns is possible, a total count is accumulated that represents the number of times
    // a solution from a different recursion call stack has been found.

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day19/input.txt";

    private static final Map<String, Boolean> validMemo = new HashMap<>();

    private static final Map<String, Long> waysMemo = new HashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 19");

        long start1 = System.currentTimeMillis();
        partOne();
        long end1 = System.currentTimeMillis();
        System.out.println("time: " + (end1 - start1) + " (ms)");

        System.out.println();

        long start2 = System.currentTimeMillis();
        partTwo();
        long end2 = System.currentTimeMillis();
        System.out.println("time: " + (end2 - start2) + " (ms)");
    }

    private static void partOne() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        final Set<String> patterns = new HashSet<>();

        int totalPossible = 0;
        for (String line: lines) {
            if (line.equals(lines.getFirst())) {
                patterns.addAll(Arrays.stream(line.split(",")).map(String::trim).toList());
                continue;
            }

            if (line.isEmpty()) {
                continue;
            }

            if (isValid(patterns, line, 0)) {
                totalPossible++;
            }
        }

        System.out.println("totalPossible=" + totalPossible);
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        final Set<String> patterns = new HashSet<>();

        long totalWays = 0;
        for (String line: lines) {
            if (line.equals(lines.getFirst())) {
                patterns.addAll(Arrays.stream(line.split(",")).map(String::trim).toList());
                continue;
            }

            if (line.isEmpty()) {
                continue;
            }

            totalWays += countWays(patterns, line, 0);
        }

        System.out.println("totalWays=" + totalWays);
    }

    private static boolean isValid(final Set<String> patterns, final String word, final int index) {
        if (index == word.length()) {
            return true;
        }

        final String localKey = word + "_" + index;
        if (validMemo.containsKey(localKey)) {
            return validMemo.get(localKey);
        }

        for (String pattern : patterns) {
            if (word.startsWith(pattern, index)) {
                if (isValid(patterns, word, index + pattern.length())) {
                    validMemo.put(localKey, true);
                    return true;
                }
            }
        }

        validMemo.put(localKey, false);
        return false;
    }

    private static long countWays(final Set<String> patterns, final String word, final int index) {
        if (index == word.length()) {
            return 1;
        }

        final String localKey = word + "_" + index;
        if (waysMemo.containsKey(localKey)) {
            return waysMemo.get(localKey);
        }

        long ways = 0;
        for (String pattern : patterns) {
            if (word.startsWith(pattern, index)) {
                ways += countWays(patterns, word, index + pattern.length());
            }
        }

        waysMemo.put(localKey, ways);
        return ways;
    }
}
