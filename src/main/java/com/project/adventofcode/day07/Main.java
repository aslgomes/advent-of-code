package com.project.adventofcode.day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day07/input.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 7");

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
        long totalPartOne = 0;

        for (String line: lines) {
            final String regex = "(\\d+):(( \\d+)+)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                final long target = Long.parseLong(matcher.group(1));
                final List<Long> numbers = Arrays.stream(matcher.group(2).split(" "))
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .toList();

                if (findTargetRecursive(target, numbers, false)) {
                    totalPartOne += target;
                }
            }
        }

        System.out.println("totalPartOne=" + totalPartOne);
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        long totalPartTwo = 0;

        for (String line: lines) {
            final String regex = "(\\d+):(( \\d+)+)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                final long target = Long.parseLong(matcher.group(1));
                final List<Long> numbers = Arrays.stream(matcher.group(2).split(" "))
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .toList();

                if (findTargetRecursive(target, numbers, true)) {
                    totalPartTwo += target;
                }
            }
        }

        System.out.println("totalPartTwo=" + totalPartTwo);
    }

    private static boolean findTargetRecursive(final long target, final List<Long> numbers, final boolean concat) {
        return findTargetRecursive(target, numbers, numbers.getFirst(), 0, concat);
    }

    private static boolean findTargetRecursive(
            final long target,
            final List<Long> numbers,
            final long currentSum,
            final int index,
            final boolean concat) {

        if (currentSum > target) {
            return false;
        }

        if (index == numbers.size() - 1) {
            return currentSum == target;
        }

        long nextNumber = numbers.get(index + 1);
        long addition = currentSum + nextNumber;
        long multiplication = currentSum * nextNumber;

        if (concat) {
            long concatenation = Long.parseLong(currentSum + "" + nextNumber);

            return findTargetRecursive(target, numbers, addition, index + 1, true)
                    || findTargetRecursive(target, numbers, multiplication, index + 1, true)
                    || findTargetRecursive(target, numbers, concatenation, index + 1, true);
        }

        return findTargetRecursive(target, numbers, addition, index + 1, false)
                || findTargetRecursive(target, numbers, multiplication, index + 1, false);
    }
}
