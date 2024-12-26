package com.project.adventofcode.year2024.day18;

import com.project.adventofcode.Common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day18/input.txt";

    // fixed
    private static final int ROWS = 71;

    // fixed
    private static final int COLS = 71;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 18");

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
        final char[][] grid = new char[ROWS][COLS];
        final String regex = "(\\d+),(\\d+)";

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = '.';
            }
        }

        int lineCount = 0;
        int target = 1024;

        for (String line : lines) {
            lineCount++;
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                grid[y][x] = '#';
            }

            if (lineCount == target) {
                int numSteps = bfs(grid);
                System.out.println("numSteps=" + numSteps);
            }
        }
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        final char[][] grid = new char[ROWS][COLS];
        final String regex = "(\\d+),(\\d+)";

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = '.';
            }
        }

        for (String line : lines) {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                grid[y][x] = '#';

                if (bfs(grid) == -1) {
                    System.out.println("bytes=" + x + "," + y);
                    return;
                }
            }
        }
    }

    private static int bfs(final char[][] grid) {
        final Queue<int[]> queue = new ArrayDeque<>();
        final Set<String> visited = new HashSet<>();

        // [0] - coordinate in x
        // [1] - coordinate in y
        // [2] - current level
        queue.add(new int[] { 0, 0, 0 });

        while (!queue.isEmpty()) {
            final int[] current = queue.remove();
            final int x = current[0];
            final int y = current[1];
            final int currentLevel = current[2];
            final String key = x + "," + y;

            if (visited.contains(key)) {
                continue;
            }

            visited.add(key);

            if (x == (ROWS - 1) && y == (COLS - 1)) {
                return currentLevel;
            }

            final int[][] neighbours = {{x, y + 1}, {x, y - 1}, {x - 1, y}, {x + 1, y}};

            for (int[] neighbour: neighbours) {
                if (Common.withinBounds(grid, neighbour[0], neighbour[1]) && grid[neighbour[0]][neighbour[1]] != '#') {
                    queue.add(new int[] { neighbour[0], neighbour[1], currentLevel + 1 });
                }
            }
        }

        return -1;
    }
}
