package com.project.adventofcode.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day13/small-input.txt";

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

                System.out.println("aX = " + aX);
                System.out.println("aY = " + aY);
                System.out.println("bX = " + bX);
                System.out.println("bY = " + bY);
                System.out.println("targetX = " + prizeX);
                System.out.println("targetY = " + prizeY);
                System.out.println();

                int minTokensToWin = minTokensToWin(aX, aY, bX, bY, prizeX, prizeY, 0, 0);
                if (minTokensToWin != Integer.MAX_VALUE) {
                    System.out.println("minTokensToWin=" + minTokensToWin);
                }
            }
        }
    }

    private static int minTokensToWin(
            final int aX, final int aY,
            final int bX, final int bY,
            final int targetX, final int targetY,
            final int numberOfPressesA, final int numberOfPressesB) {

//        System.out.println("Here");
//        System.out.println("aX = " + aX);
//        System.out.println("aY = " + aY);
//        System.out.println("bX = " + bX);
//        System.out.println("bY = " + bY);
//        System.out.println("targetX = " + targetX);
//        System.out.println("targetY = " + targetY);
//        System.out.println("numberOfPressesA = " + numberOfPressesA);
//        System.out.println("numberOfPressesB = " + numberOfPressesB);
//        System.out.println();

        if ((numberOfPressesA * aX + numberOfPressesB * bX == targetX)
                && (numberOfPressesA * aY + numberOfPressesB * bY == targetY)) {
            return numberOfPressesA * 3 + numberOfPressesB;
        }

        if ((numberOfPressesA * aX + numberOfPressesB * bX > targetX)
                || (numberOfPressesA * aY + numberOfPressesB * bY > targetY)) {
            return Integer.MAX_VALUE;
        }

        return Math.min(
                minTokensToWin(aX, aY, bX, bY, targetX, targetY, numberOfPressesA + 1, numberOfPressesB),
                minTokensToWin(aX, aY, bX, bY, targetX, targetY, numberOfPressesA, numberOfPressesB + 1)
        );
    }

    private static void partTwo() throws IOException {
    }
}
