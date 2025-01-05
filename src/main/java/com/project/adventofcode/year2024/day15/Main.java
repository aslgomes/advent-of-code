package com.project.adventofcode.year2024.day15;

import com.project.adventofcode.Common;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    // Key aspects of the solution:
    //
    // Part 1 - Basic grid manipulation. The key challenge is implementing the move() function which may need to
    // perform multiple swaps if there are boxes blocking the path.
    //
    // Part 2 - Similar to Part 1 with the added difficulty that now each box is represented by 2 cells []. In addition
    // to this, multiple boxes adjacent to each other in triangles can be pushed together too.

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day15/input.txt";

    private static final String OUTPUT_FILE_PATH_PART_ONE = "src/main/resources/year2024/day15/output-part-one/";

    private static final String OUTPUT_FILE_PATH_PART_TWO = "src/main/resources/year2024/day15/output-part-two/";

    private static final Map<Character, int[]> DIRECTIONS =
            Map.of(
                    '>', new int[] { 0, 1 },
                    '<', new int[] { 0, -1 },
                    '^', new int[] { -1, 0 },
                    'v', new int[] { 1, 0 }
            );

    private static final Map<Character, Color> COLOR_MAP =
            Map.of(
                    '@', Color.RED,
                    'O', Color.BLUE,
                    '[', Color.BLUE,
                    ']', Color.BLUE,
                    '#', new Color(0, 100, 0) // Dark Green
            );

    private static final boolean WITH_OUTPUT_PART_ONE = false;

    private static final boolean WITH_OUTPUT_PART_TWO = false;

    private record Cell(int x, int y) { }

    private record RowsAndMoves(int rows, List<String> moves) { }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 15");

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

        final RowsAndMoves rowsAndMoves = processInput(lines);
        final List<String> moves = rowsAndMoves.moves();

        int rows = rowsAndMoves.rows();
        int cols = lines.getFirst().length();

        char[][] grid = new char[rows][cols];
        Cell robotPos = populateGrid(grid, lines);

        int fileIterator = 1;
        Common.outputImage(WITH_OUTPUT_PART_ONE, OUTPUT_FILE_PATH_PART_ONE, fileIterator++, grid, COLOR_MAP);

        // Compute all moves
        for (String move : moves) {
            for (int i = 0; i < move.length(); i++) {
                final char direction = move.charAt(i);
                robotPos = movePartOne(direction, robotPos, grid);
                Common.outputImage(WITH_OUTPUT_PART_ONE, OUTPUT_FILE_PATH_PART_ONE, fileIterator++, grid, COLOR_MAP);
            }
        }

        System.out.println("total=" + computeResult(grid, 'O'));
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));

        final RowsAndMoves rowsAndMoves = processInput(lines);
        final List<String> moves = rowsAndMoves.moves();

        int rows = rowsAndMoves.rows();
        int cols = lines.getFirst().length();

        char[][] grid = new char[rows][cols * 2];
        Cell robotPos = new Cell(0, 0);

        for (int i = 0; i < rows; i++) {
            int p = 0;
            for (int j = 0; j < lines.get(i).length(); j++) {
                final char currentChar = lines.get(i).charAt(j);

                if (currentChar == '.') {
                    grid[i][p] = '.';
                    grid[i][p + 1] = '.';
                }

                if (currentChar == '#') {
                    grid[i][p] = '#';
                    grid[i][p + 1] = '#';
                }

                if (currentChar == 'O') {
                    grid[i][p] = '[';
                    grid[i][p + 1] = ']';
                }

                if (currentChar == '@') {
                    grid[i][p] = '@';
                    grid[i][p + 1] = '.';
                    robotPos = new Cell(i, p);
                }

                p = p + 2;
            }
        }

        int fileIterator = 1;
        Common.outputImage(WITH_OUTPUT_PART_TWO, OUTPUT_FILE_PATH_PART_TWO, fileIterator++, grid, COLOR_MAP);

        // Compute all moves
        for (String move : moves) {
            for (int i = 0; i < move.length(); i++) {
                final char direction = move.charAt(i);
                robotPos = movePartTwo(direction, robotPos, grid);
                Common.outputImage(WITH_OUTPUT_PART_TWO, OUTPUT_FILE_PATH_PART_TWO, fileIterator++, grid, COLOR_MAP);
            }
        }

        System.out.println("total=" + computeResult(grid, '['));
    }

    private static RowsAndMoves processInput(final List<String> lines) {
        final List<String> moves = new ArrayList<>();

        int rows = 0;
        boolean seenEmpty = false;

        // Process the input file
        for (String line : lines) {
            if (line.isEmpty()) {
                seenEmpty = true;
                continue;
            }

            if (!seenEmpty) {
                rows++;

            } else {
                moves.add(line);
            }
        }

        return new RowsAndMoves(rows, moves);
    }

    private static Cell populateGrid(final char[][] grid, final List<String> lines) {
        Cell robotPos = new Cell(0, 0);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = lines.get(i).charAt(j);
                if (grid[i][j] == '@') {
                    robotPos = new Cell(i, j);
                }
            }
        }

        return robotPos;
    }

    private static long computeResult(final char[][] grid, final char c) {
        long total = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == c) {
                    total += (100L * i + j);
                }
            }
        }
        return total;
    }

    private static Cell movePartOne(final char direction, final Cell robotPos, final char[][] grid) {

        final int[] coordinates = DIRECTIONS.get(direction);
        Cell current = new Cell(robotPos.x() + coordinates[0], robotPos.y() + coordinates[1]);

        while (Common.withinBounds(grid, current.x, current.y)
                && (grid[current.x()][current.y()] == 'O'
                        || grid[current.x()][current.y()] == '['
                        || grid[current.x()][current.y()] == ']')) {

            current = new Cell(current.x() + coordinates[0], current.y() + coordinates[1]);
        }

        if (Common.withinBounds(grid, current.x, current.y) && grid[current.x()][current.y()] == '#') {
            return robotPos;
        }

        if (Common.withinBounds(grid, current.x, current.y) && grid[current.x()][current.y()] == '.') {

            // while we are not at the coordinates where '@' is
            while (current.x() != robotPos.x() || current.y() != robotPos.y()) {

                // find previous
                final Cell previous = new Cell(current.x() - coordinates[0], current.y() - coordinates[1]);

                // swap current with previous
                swap(grid, current, previous);

                // if the element that was just swapped is the robot, end the iteration
                if (grid[current.x()][current.y()] == '@') {
                    return new Cell(current.x(), current.y());
                }

                // new current
                current = new Cell(previous.x(), previous.y());
            }
        }

        // this code should not be reached
        return new Cell(-1, -1);
    }

    private static Cell movePartTwo(final char direction, final Cell robotPos, final char[][] grid) {

        if (direction == '>' || direction == '<') {
            return movePartOne(direction, robotPos, grid);
        }

        if (canMoveFrom(direction, robotPos, grid)) {
            swapFrom(direction, robotPos, grid);
            return getNextCell(direction, robotPos);
        }

        return robotPos;
    }

    private static boolean canMoveFrom(final char direction, final Cell current, final char[][] grid) {
        final Cell next = getNextCell(direction, current);
        final Cell nextNeighbour;

        return switch (grid[current.x()][current.y()]) {
            case '@' -> canMoveFrom(direction, next, grid);
            case '.' -> true;
            case '[' -> {
                nextNeighbour = new Cell(next.x(), next.y() + 1);
                yield canMoveFrom(direction, next, grid) && canMoveFrom(direction, nextNeighbour, grid);
            }
            case ']' -> {
                nextNeighbour = new Cell(next.x(), next.y() - 1);
                yield canMoveFrom(direction, next, grid) && canMoveFrom(direction, nextNeighbour, grid);
            }
            default -> false;
        };
    }

    private static void swapFrom(final char direction, final Cell current, final char[][] grid) {
        final Cell next = getNextCell(direction, current);
        final Cell nextNeighbour;

        switch (grid[current.x()][current.y()]) {
            case '@':
                swapFrom(direction, next, grid);
                swap(grid, current, next);
                break;

            case '.':
                break;

            case '[':
                nextNeighbour = new Cell(next.x(), next.y() + 1);
                swapFrom(direction, next, grid);
                swapFrom(direction, nextNeighbour, grid);
                swap(grid, current, next);
                swap(grid, new Cell(current.x(), current.y() + 1), nextNeighbour);
                break;

            case ']':
                nextNeighbour = new Cell(next.x(), next.y() - 1);
                swapFrom(direction, next, grid);
                swapFrom(direction, nextNeighbour, grid);
                swap(grid, current, next);
                swap(grid, new Cell(current.x(), current.y() - 1), nextNeighbour);
                break;
        }
    }

    private static Cell getNextCell(final char direction, final Cell current) {
        final int[] coordinates = DIRECTIONS.get(direction);
        return new Cell(current.x() + coordinates[0], current.y() + coordinates[1]);
    }

    private static void swap(final char[][] grid, final Cell a, final Cell b) {
        char tmp = grid[a.x][a.y];
        grid[a.x][a.y] = grid[b.x][b.y];
        grid[b.x][b.y] = tmp;
    }
}
