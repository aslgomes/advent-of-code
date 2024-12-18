package com.project.adventofcode.day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day14/input.txt";

    // fixed
    private static final int ROWS = 103;

    // fixed
    private static final int COLS = 101;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 14");

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
        final int[][] grid = new int[ROWS][COLS];
        final String regex = "p=(-?\\d+),(-?\\d+)\\sv=(-?\\d+),(-?\\d+)";

        final Map<String, int[]> positions = new HashMap<>();
        final Map<String, int[]> speeds = new HashMap<>();

        for (String line: lines) {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                final String id = UUID.randomUUID().toString();
                positions.put(id, new int[] { Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1)) });
                speeds.put(id, new int[] { Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(3)) });
            }
        }

        move(grid, positions, speeds, 100);
        System.out.println("safetyScore=" + safetyScore(grid));
    }

    private static void move(
            final int[][] grid,
            final Map<String, int[]> positions,
            final Map<String, int[]> speeds,
            int times) {

        for (String key : positions.keySet()) {
            for (int i = 0; i < times; i++) {
                int newX = ((positions.get(key)[0] + speeds.get(key)[0]) % ROWS + ROWS) % ROWS;
                int newY = ((positions.get(key)[1] + speeds.get(key)[1]) % COLS + COLS) % COLS;
                positions.put(key, new int[] { newX, newY });
            }
            grid[positions.get(key)[0]][positions.get(key)[1]]++;
        }
    }

    private static long safetyScore(final int[][] grid) {
        int q1Sum = 0;
        for (int i = 0; i < ROWS / 2; i++) {
            for (int j = 0; j < COLS / 2; j++) {
                q1Sum += grid[i][j];
            }
        }

        int q2Sum = 0;
        for (int i = 0; i < ROWS / 2; i++) {
            for (int j = (COLS / 2 + 1); j < COLS; j++) {
                q2Sum += grid[i][j];
            }
        }

        int q3Sum = 0;
        for (int i = (ROWS / 2 + 1); i < ROWS; i++) {
            for (int j = 0; j < COLS / 2; j++) {
                q3Sum += grid[i][j];
            }
        }

        int q4Sum = 0;
        for (int i = (ROWS / 2 + 1); i < ROWS; i++) {
            for (int j = (COLS / 2 + 1); j < COLS; j++) {
                q4Sum += grid[i][j];
            }
        }

        return ((long) q1Sum * q2Sum * q3Sum * q4Sum);
    }

    private static void partTwo() throws IOException {
    }
}
