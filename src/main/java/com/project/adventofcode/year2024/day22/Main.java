package com.project.adventofcode.year2024.day22;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    // Key aspects of the solution:
    //
    // tags - #math #sliding-window
    //
    // Part 1 - Translates the problem statement into several mathematical steps. After the 2000th cycle
    // for each buyer, the secret number gets added to a running total.
    //
    // Part 2 - Builds of the diffs[] and digits[] arrays based on the problem description. Performs a sliding window
    // over these structures to build local sequences per buyer. Computes the global maximum by comparing all the local
    // sequences.

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day22/input.txt";

    private static final BigInteger MODULO = BigInteger.valueOf(16777216);

    private static final int CYCLES = 2000;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 22");

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
        BigInteger total = BigInteger.ZERO;

        for (String line : lines) {
            BigInteger secretNumber = new BigInteger(line);

            for (int i = 0; i < CYCLES; i++) {
                secretNumber = prune(mix(secretNumber, secretNumber.multiply(BigInteger.valueOf(64))));
                secretNumber = prune(mix(secretNumber, secretNumber.divide(BigInteger.valueOf(32))));
                secretNumber = prune(mix(secretNumber, secretNumber.multiply(BigInteger.valueOf(2048))));
                if (i == CYCLES - 1) {
                    total = total.add(secretNumber);
                }
            }
        }

        System.out.println("total=" + total);
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        final Map<String, Integer> intervals = new HashMap<>();

        for (String line : lines) {
            int[] diffs = new int[CYCLES];
            int[] digits = new int[CYCLES];

            BigInteger secretNumber = new BigInteger(line);
            int lastDigit = secretNumber.mod(BigInteger.TEN).intValue();

            for (int i = 0; i < CYCLES; i++) {
                secretNumber = prune(mix(secretNumber, secretNumber.multiply(BigInteger.valueOf(64))));
                secretNumber = prune(mix(secretNumber, secretNumber.divide(BigInteger.valueOf(32))));
                secretNumber = prune(mix(secretNumber, secretNumber.multiply(BigInteger.valueOf(2048))));
                diffs[i] = (secretNumber.mod(BigInteger.TEN).intValue()) - lastDigit;
                lastDigit = secretNumber.mod(BigInteger.TEN).intValue();
                digits[i] = lastDigit;
            }

            final Map<String, Integer> localIntervals = gatherIntervals(diffs, digits);
            for (String key : localIntervals.keySet()) {
                // when the same sequence appears between different buyers, sum the values
                intervals.put(key, intervals.getOrDefault(key, 0) + localIntervals.get(key));
            }
        }

        int max = Integer.MIN_VALUE;
        for (String key : intervals.keySet()) {
            max = Math.max(max, intervals.get(key));
        }

        System.out.println("max=" + max);
    }

    private static BigInteger mix(final BigInteger secret, final BigInteger n) {
        return secret.xor(n);
    }

    private static BigInteger prune(final BigInteger secret) {
        return secret.mod(MODULO);
    }

    private static Map<String, Integer> gatherIntervals(final int[] diffs, final int[] digits) {
        final Map<String, Integer> localIntervals = new HashMap<>();

        int i = 0;
        int j = i + 3;

        while (j < diffs.length) {
            final StringBuilder key = new StringBuilder();
            for (int p = i; p <= j; p++) {
                if (p == j) {
                    key.append(diffs[p]);
                    continue;
                }
                key.append(diffs[p]).append(",");
            }

            final String completeKey = key.toString();

            // if the same sequence exists more than once within the same buyer, keep the first value seen
            localIntervals.merge(completeKey, digits[j], (i1, _) -> i1);
            i++;
            j++;
        }

        return localIntervals;
    }
}
