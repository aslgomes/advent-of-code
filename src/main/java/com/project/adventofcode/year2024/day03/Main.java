package com.project.adventofcode.year2024.day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day03/input.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 3");

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
        final String regex = "mul\\((\\d+),(\\d+)\\)";

        int total = 0;
        for (String line : lines) {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                total += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
            }
        }

        System.out.println("total=" + total);
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        final String regex = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don\'t\\(\\)";

        int total = 0;

        // At the start of the program, multiplication (mul) is allowed. However, line breaks in the input text should
        // not reset the shouldMultiply instruction.
        boolean shouldMultiply = true;

        for (String line : lines) {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                final String match = matcher.group();

                if (match.startsWith("m") && shouldMultiply) {
                    total += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
                    continue;
                }

                if (match.equals("don't()")) {
                    shouldMultiply = false;
                    continue;
                }

                if (match.equals("do()")) {
                    shouldMultiply = true;
                }
            }
        }

        System.out.println("total=" + total);
    }
}
