package com.project.adventofcode.day02;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day02/input.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 2");

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
        final Path filePath = new File(INPUT_FILE_PATH).toPath();
        int totalSafe = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] levels = line.split("\\s+");
                if (isSafe(levels) == -1) {
                    totalSafe++;
                }
            }
        }

        System.out.println("totalSafe=" + totalSafe);
    }

    private static void partTwo() throws IOException {
        final Path filePath = new File(INPUT_FILE_PATH).toPath();
        int totalSafe = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] levels = line.split("\\s+");

                if (isSafe(levels) == -1) {
                    totalSafe++;

                } else {

                    //
                    // Why do we need to iterate over the whole list?
                    //
                    // Given the example: 1 4 3 2 1
                    //
                    // Identifying the exact number to remove can be challenging, as local relationships between numbers
                    // might seem valid initially. For instance, the pair 1-4 appears to be increasing and within the
                    // allowed difference. However, the subsequent numbers reveal that removing 1 is necessary to make
                    // the entire sequence safe.
                    //
                    // Given this complexity, a brute-force approach of checking all possible removals is a practical
                    // solution. As soon as we find a removal that results in a safe sequence, we can confidently
                    // conclude that the original sequence is also safe after removing that element.
                    //
                    for (int i = 0; i < levels.length; i++) {
                        if (isSafe(levels, i) == -1) {
                            totalSafe++;
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("totalSafeDampener=" + totalSafe);
    }

    //
    // A negative 'skipIndex' input parameter will ensure no element from 'levels' is skipped
    // A return value of -1 means this set of values are deemed safe in the context of the problem statement
    // A return value different from -1 represents the index at which the first violation was seen
    //
    private static int isSafe(final String[] levels) {
        return isSafe(levels, -1);
    }

    private static int isSafe(final String[] levels, int skipIndex) {
        int prev = -1;
        boolean increasing = false;
        boolean directionSet = false;

        for (int i = 0; i < levels.length; i++) {

            int curr = Integer.parseInt(levels[i]);

            if (i == skipIndex) {
                continue;
            }

            if (prev == -1) {
                prev = curr;
                continue;
            }

            // Determine the direction on the first comparison
            if (!directionSet) {
                increasing = prev < curr;
                directionSet = true;
            }

            // Check for violations of the rules
            int diff = Math.abs(prev - curr);
            if ((increasing && prev > curr) || (!increasing && prev < curr) || diff < 1 || diff > 3) {
                return i;
            }

            prev = curr;
        }

        return -1;
    }
}
