package com.project.adventofcode.year2024.day08;

import com.project.adventofcode.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day08/input.txt";

    private record Coordinates(int x, int y) {}

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 8");

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
        final Map<Character, List<Coordinates>> LOCATIONS = new HashMap<>();
        final Set<Integer> antinodes = new HashSet<>();
        final char[][] grid = Common.loadCharGrid(INPUT_FILE_PATH);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (Character.isAlphabetic(grid[i][j]) || Character.isDigit(grid[i][j])) {
                    LOCATIONS.computeIfAbsent(grid[i][j], _ -> new ArrayList<>()).add(new Coordinates(i, j));
                }
            }
        }

        for (Character antenna : LOCATIONS.keySet()) {
            final List<Coordinates> locations = LOCATIONS.get(antenna);
            for (int i = 0; i < locations.size(); i++) {
                final Coordinates fixed = locations.get(i);

                for (int j = 0; j < locations.size(); j++) {
                    if (i != j) {
                        final Coordinates curr = locations.get(j);

                        int diffX = fixed.x() - curr.x();
                        int diffY = fixed.y() - curr.y();

                        final int newX = curr.x() - diffX;
                        final int newY = curr.y() - diffY;

                        if (Common.withinBounds(grid, newX, newY)) {
                            // From 2D -> 1D coordinates
                            final Integer index = newX * grid[0].length + newY;
                            antinodes.add(index);
                        }
                    }
                }
            }
        }

        System.out.println("totalLocationsPartOne=" + antinodes.size());
    }

    private static void partTwo() throws IOException {
        final Map<Character, List<Coordinates>> LOCATIONS = new HashMap<>();
        final Set<Integer> antinodes = new HashSet<>();
        final char[][] grid = Common.loadCharGrid(INPUT_FILE_PATH);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (Character.isAlphabetic(grid[i][j]) || Character.isDigit(grid[i][j])) {
                    LOCATIONS.computeIfAbsent(grid[i][j], _ -> new ArrayList<>()).add(new Coordinates(i, j));
                }
            }
        }

        for (Character antenna : LOCATIONS.keySet()) {
            final List<Coordinates> locations = LOCATIONS.get(antenna);
            for (int i = 0; i < locations.size(); i++) {

                final Coordinates fixed = locations.get(i);

                for (int j = 0; j < locations.size(); j++) {
                    if (i != j) {
                        final Coordinates curr = locations.get(j);

                        int diffX = fixed.x() - curr.x();
                        int diffY = fixed.y() - curr.y();

                        // one direction
                        int newX = fixed.x() - diffX;
                        int newY = fixed.y() - diffY;

                        while (Common.withinBounds(grid, newX, newY)) {
                            // From 2D -> 1D coordinates
                            final Integer index = newX * grid[0].length + newY;
                            antinodes.add(index);

                            newX = newX - diffX;
                            newY = newY - diffY;
                        }

                        // opposite direction
                        int otherNewX = fixed.x() + diffX;
                        int otherNewY = fixed.y() + diffY;

                        while (Common.withinBounds(grid, otherNewX, otherNewY)) {
                            // From 2D -> 1D coordinates
                            final Integer index = otherNewX * grid[0].length + otherNewY;
                            antinodes.add(index);

                            otherNewX = otherNewX + diffX;
                            otherNewY = otherNewY + diffY;
                        }
                    }
                }
            }
        }

        System.out.println("totalLocationsPartTwo=" + antinodes.size());
    }
}
