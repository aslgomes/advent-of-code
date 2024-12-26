package com.project.adventofcode.year2024.day04;

import com.project.adventofcode.Common;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day04/input.txt";

    private static final String KEYWORD_XMAS = "XMAS";

    private static final String KEYWORD_MAS = "MAS";

    private static char[][] grid;

    private enum Direction {
        RIGHT(0, 1),
        LEFT(0, -1),
        UP(-1, 0),
        DOWN(1, 0),
        UPPER_RIGHT(-1, 1),
        UPPER_LEFT(-1, -1),
        DOWN_RIGHT(1, 1),
        DOWN_LEFT(1, -1);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Coordinates move(Coordinates c) {
            return new Coordinates(c.x() + dx, c.y() + dy);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 4");

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
        grid = Common.loadCharGrid(INPUT_FILE_PATH);
        int totalCount = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'X') {
                    for (Direction direction: Direction.values()) {
                        if (dfs(new Coordinates(i, j), KEYWORD_XMAS, 0, direction)) {
                            totalCount++;
                        }
                    }
                }
            }
        }

        System.out.println("totalCountPartOne=" + totalCount);
    }

    private static void partTwo() throws IOException {
        grid = Common.loadCharGrid(INPUT_FILE_PATH);
        int totalCount = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'A') {
                    int localMatch = 0;

                    for (SearchPath c : List.of(
                            new SearchPath(i + 1, j + 1, Direction.UPPER_LEFT),
                            new SearchPath(i - 1, j + 1, Direction.DOWN_LEFT),
                            new SearchPath(i + 1, j - 1, Direction.UPPER_RIGHT),
                            new SearchPath(i - 1, j - 1, Direction.DOWN_RIGHT))) {

                        if (dfs(new Coordinates(c.x, c.y), KEYWORD_MAS, 0, c.direction)) {
                            localMatch++;
                        }
                    }

                    if (localMatch == 2) {
                        totalCount++;
                    }
                }
            }
        }

        System.out.println("totalCountPartTwo=" + totalCount);
    }

    private static boolean dfs(
            final Coordinates c,
            final String targetWord,
            final int currentIndex,
            final Direction direction) {

        if (!Common.withinBounds(grid, c.x(), c.y()) || grid[c.x()][c.y()] != targetWord.charAt(currentIndex)) {
            return false;
        }

        if (currentIndex == targetWord.length() - 1) {
            return true;
        }

        final Coordinates next = direction.move(c);
        return dfs(next, targetWord, currentIndex + 1, direction);
    }

    private record Coordinates(int x, int y) {}

    private record SearchPath(int x, int y, Direction direction) {}
}
