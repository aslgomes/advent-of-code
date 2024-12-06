package com.project.adventofcode.day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day06/input.txt";

    private static final List<int[]> GUARD_POSITIONS = new ArrayList<>();

    private enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT;
    }

    private static final Map<Direction, int[]> nextCoordinateByDirection = Map.of(
            Direction.UP,    new int[] { -1,  0 },
            Direction.DOWN,  new int[] {  1,  0 },
            Direction.RIGHT, new int[] {  0,  1 },
            Direction.LEFT,  new int[] {  0, -1 }
    );

    private static final Map<Direction, Direction> rotate = Map.of(
            Direction.UP, Direction.RIGHT,
            Direction.DOWN,  Direction.LEFT,
            Direction.RIGHT, Direction.DOWN,
            Direction.LEFT,  Direction.UP
    );

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 6");

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

        final int rows = lines.size();
        final int cols = lines.getFirst().length();

        final char[][] grid = new char[rows][cols];

        int r = 0;
        for (String line: lines) {
            char[] chars = line.toCharArray();
            System.arraycopy(chars, 0, grid[r++], 0, chars.length);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '^') {
                    grid[i][j] = 'X';
                    GUARD_POSITIONS.add(new int[] {i, j});
                    move(grid, i, j, Direction.UP);
                }
            }
        }

        System.out.println("totalPositionsPartOne=" + GUARD_POSITIONS.size());
    }

    private static void move(final char[][] grid, final int x, final int y, final Direction direction) {

        final int newX = x + nextCoordinateByDirection.get(direction)[0];
        final int newY = y + nextCoordinateByDirection.get(direction)[1];

        if (withinBounds(grid, newX, newY)) {
            if (grid[newX][newY] == '#') {
                // If obstacle, change direction
                move(grid, x, y, rotate.get(direction));

            } else {
                // Mark position if not visited
                if (grid[newX][newY] != 'X') {
                    GUARD_POSITIONS.add(new int[] {newX, newY});
                    grid[newX][newY] = 'X';
                }
                // Continue in the same direction
                move(grid, newX, newY, direction);
            }
        }
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        int totalPositionsPartTwo = 0;

        final int rows = lines.size();
        final int cols = lines.getFirst().length();

        final char[][] grid = new char[rows][cols];

        int r = 0;
        for (String line: lines) {
            char[] chars = line.toCharArray();
            System.arraycopy(chars, 0, grid[r++], 0, chars.length);
        }

        for(int[] pos: GUARD_POSITIONS) { // Assumes partOne() has ran first
            char tmp = grid[pos[0]][pos[1]];
            grid[pos[0]][pos[1]] = 'O';

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid[i][j] == '^') {
                        if (hasLoop(grid, i, j, Direction.UP, new HashMap<>())) {
                            totalPositionsPartTwo++;
                        }
                    }
                }
            }

            grid[pos[0]][pos[1]] = tmp;
        }

        System.out.println("totalPositionsPartTwo=" + totalPositionsPartTwo);
    }

    private static boolean hasLoop(
            final char[][] grid,
            final int x,
            final int y,
            final Direction direction,
            final Map<Integer, Direction> seen) {

        final int newX = x + nextCoordinateByDirection.get(direction)[0];
        final int newY = y + nextCoordinateByDirection.get(direction)[1];

        if (withinBounds(grid, newX, newY)) {
            if (grid[newX][newY] == '#' || grid[newX][newY] == 'O') {
                // If obstacle, change direction
                return hasLoop(grid, x, y, rotate.get(direction), seen);
            }

            final int index = newX * grid[0].length + newY;

            // If the same position has been visited twice following the same direction, there's a loop.
            if (seen.containsKey(index) && seen.get(index) == direction) {
                return true;
            }

            seen.put(index, direction);
            // Continue in the same direction
            return hasLoop(grid, newX, newY, direction, seen);
        }

        return false;
    }

    private static boolean withinBounds(final char[][] grid, final int x, final int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
}
