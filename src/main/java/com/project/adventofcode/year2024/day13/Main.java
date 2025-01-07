package com.project.adventofcode.year2024.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day13/input.txt";

    private static Map<String, Integer> memo = new HashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 13");

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

        final String buttonAPattern = "Button A:\\sX\\+(\\d+),\\sY\\+(\\d+)";
        final String buttonBPattern = "Button B:\\sX\\+(\\d+),\\sY\\+(\\d+)";
        final String prizePattern = "Prize:\\sX=(\\d+),\\sY=(\\d+)";

        final Pattern buttonARegex = Pattern.compile(buttonAPattern);
        final Pattern buttonBRegex = Pattern.compile(buttonBPattern);
        final Pattern prizeRegex = Pattern.compile(prizePattern);

        int aX = 0, aY = 0, bX = 0, bY = 0, prizeX, prizeY;

        int total = 0;
        for (String line: lines) {

            if (line.isEmpty()) {
                continue;
            }

            final Matcher buttonAMatcher = buttonARegex.matcher(line);
            if (buttonAMatcher.find()) {
                aX = Integer.parseInt(buttonAMatcher.group(1));
                aY = Integer.parseInt(buttonAMatcher.group(2));
            }

            final Matcher buttonBMatcher = buttonBRegex.matcher(line);
            if (buttonBMatcher.find()) {
                bX = Integer.parseInt(buttonBMatcher.group(1));
                bY = Integer.parseInt(buttonBMatcher.group(2));
            }

            final Matcher prizeMatcher = prizeRegex.matcher(line);
            if (prizeMatcher.find()) {
                prizeX = Integer.parseInt(prizeMatcher.group(1));
                prizeY = Integer.parseInt(prizeMatcher.group(2));
                memo = new HashMap<>();
                int tokens = computeTokens(aX, aY, bX, bY, prizeX, prizeY, 0, 0);
                if (tokens != Integer.MAX_VALUE) {
                    total += tokens;
                }
            }
        }

        System.out.println("total=" + total);
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));

        final String buttonAPattern = "Button A:\\sX\\+(\\d+),\\sY\\+(\\d+)";
        final String buttonBPattern = "Button B:\\sX\\+(\\d+),\\sY\\+(\\d+)";
        final String prizePattern = "Prize:\\sX=(\\d+),\\sY=(\\d+)";

        final Pattern buttonARegex = Pattern.compile(buttonAPattern);
        final Pattern buttonBRegex = Pattern.compile(buttonBPattern);
        final Pattern prizeRegex = Pattern.compile(prizePattern);

        double aX = 0, aY = 0, bX = 0, bY = 0, prizeX, prizeY;
        long total = 0;

        for (String line: lines) {

            if (line.isEmpty()) {
                continue;
            }

            final Matcher buttonAMatcher = buttonARegex.matcher(line);
            if (buttonAMatcher.find()) {
                aX = Integer.parseInt(buttonAMatcher.group(1));
                aY = Integer.parseInt(buttonAMatcher.group(2));
            }

            final Matcher buttonBMatcher = buttonBRegex.matcher(line);
            if (buttonBMatcher.find()) {
                bX = Integer.parseInt(buttonBMatcher.group(1));
                bY = Integer.parseInt(buttonBMatcher.group(2));
            }

            final Matcher prizeMatcher = prizeRegex.matcher(line);
            if (prizeMatcher.find()) {
                prizeX = Integer.parseInt(prizeMatcher.group(1)) + 10000000000000L;
                prizeY = Integer.parseInt(prizeMatcher.group(2)) + 10000000000000L;

                // Solve the equations:
                // aX * A + bX * B = prizeX
                // aY * A + bY * B = prizeY

                // linear algebra
                final long b = (long) ((prizeY * aX - prizeX * aY) / (bY * aX - bX * aY));
                final long a = (long) ((prizeX - b * bX) / aX);

                if (a >= 0 && b >= 0 && (a * aX + b * bX == prizeX) && (a * aY + b * bY == prizeY)) {
                    total += a * 3 + b;
                }
            }
        }

        System.out.println("total=" + total);
    }

    private static int computeTokens(
            final int aX, final int aY,
            final int bX, final int bY,
            final int targetX, final int targetY,
            final int numberOfPressesA, final int numberOfPressesB) {

        final String key = numberOfPressesA + "." + numberOfPressesB;

        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        if (numberOfPressesA > 100 || numberOfPressesB > 100) {
            return Integer.MAX_VALUE;
        }

        if ((numberOfPressesA * aX + numberOfPressesB * bX > targetX)
                || (numberOfPressesA * aY + numberOfPressesB * bY > targetY)) {
            return Integer.MAX_VALUE;
        }

        if ((numberOfPressesA * aX + numberOfPressesB * bX == targetX)
                && (numberOfPressesA * aY + numberOfPressesB * bY == targetY)) {
            return numberOfPressesA * 3 + numberOfPressesB;
        }

        int result = Math.min(
                computeTokens(aX, aY, bX, bY, targetX, targetY, numberOfPressesA + 1, numberOfPressesB),
                computeTokens(aX, aY, bX, bY, targetX, targetY, numberOfPressesA, numberOfPressesB + 1)
        );

        memo.put(key, result);
        return result;
    }
}
