package com.project.adventofcode.day03;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day03/input.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 3");
        partOne();
        partTwo();
    }

    private static void partOne() throws IOException {
        final Path filePath = new File(INPUT_FILE_PATH).toPath();
        final String regex = "mul\\((\\d+),(\\d+)\\)";
        int total = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    total += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
                }
            }
        }

        System.out.println("total=" + total);
    }

    private static void partTwo() throws IOException {
        final Path filePath = new File(INPUT_FILE_PATH).toPath();
        final String regex = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don\'t\\(\\)";

        int total = 0;

        // At the start of the program, multiplication (mul) is allowed. However, line breaks in the input text should
        // not reset the shouldMultiply instruction.
        boolean shouldMultiply = true;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
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
        }

        System.out.println("total=" + total);
    }
}
