package com.project.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Common {

    public static char[][] loadGrid(final String inputFilePath) throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(inputFilePath));
        final int rows = lines.size();
        final int cols = lines.getFirst().length();

        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }

    public static boolean withinBounds(final char[][] grid, final int x, final int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public static void printGrid(final char[][] grid) {
        for (char[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(chars[j]);
            }
            System.out.println();
        }
    }
}
