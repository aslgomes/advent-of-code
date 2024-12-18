package com.project.adventofcode.day10;

import com.project.adventofcode.Common;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    // Key aspects of the solution:
    //
    // Part 1 - Perform a simple DFS to count each occurrence of 9 the first time a node with number 9 is visited.
    //
    // Part 2 - Use DFS again to explore all unique paths from any source node 0 to all target reachable 9's, allowing
    // nodes to be revisited. Sum up the scores for these paths at the end.

    private static final String INPUT_FILE_PATH = "src/main/resources/day10/input.txt";

    private record CellPath(int x, int y, List<String> path) {}

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 10");

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
        final int[][] grid = Common.loadIntGrid(INPUT_FILE_PATH);
        int score = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    score += dfs(grid, i, j);
                }
            }
        }

        System.out.println("score=" + score);
    }

    private static void partTwo() throws IOException {
        final int[][] grid = Common.loadIntGrid(INPUT_FILE_PATH);
        final Map<String, Set<List<String>>> pathsByZero = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    final String key = i + "," + j;
                    pathsByZero.put(key, new HashSet<>());
                    dfsWithPaths(pathsByZero, grid, i, j);
                }
            }
        }

        int totalRatings = 0;
        for (String key : pathsByZero.keySet()) {
            totalRatings += pathsByZero.get(key).size();
        }

        System.out.println("totalRatings=" + totalRatings);
    }

    private static int dfs(final int[][] grid, final int x, final int y) {
        int localScore = 0;
        final Set<Integer> seen = new HashSet<>();

        final Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {x, y});

        while (!stack.isEmpty()) {
            final int[] coordinates = stack.pop();
            final int coordinateX = coordinates[0];
            final int coordinateY = coordinates[1];
            final int curr = grid[coordinateX][coordinateY];

            // convert 2D coordinates to 1D coordinate
            final int index = coordinateX * grid[0].length + coordinateY;

            // check if this 9 (the end of the trail) has been reached already
            if (seen.contains(index)) {
                continue;
            }

            seen.add(index);

            if (curr == 9) {
                localScore++;
            }

            final List<int[]> neighbours = List.of(
                    new int[] { coordinateX + 1, coordinateY },
                    new int[] { coordinateX - 1, coordinateY },
                    new int[] { coordinateX, coordinateY + 1 },
                    new int[] { coordinateX, coordinateY - 1 }
            );

            for (int[] neighbour: neighbours) {
                if (Common.withinBounds(grid, neighbour[0], neighbour[1]) &&
                        grid[neighbour[0]][neighbour[1]] == (curr + 1)) {
                    stack.push(new int[] { neighbour[0], neighbour[1] });
                }
            }
        }

        return localScore;
    }

    private static void dfsWithPaths(
            final Map<String, Set<List<String>>> allPaths,
            final int[][] grid,
            final int x,
            final int y) {

        final Deque<CellPath> stack = new ArrayDeque<>();
        final List<String> path = new ArrayList<>();

        final String key = x + "," + y;
        path.add(key);
        stack.push(new CellPath(x, y, path));

        while (!stack.isEmpty()) {
            final CellPath cellPath = stack.pop();
            final int coordinateX = cellPath.x;
            final int coordinateY = cellPath.y;

            if (grid[coordinateX][coordinateY] == 9) {
                allPaths.get(key).add(cellPath.path);
            }

            final List<int[]> neighbours = List.of(
                    new int[] { coordinateX + 1, coordinateY },
                    new int[] { coordinateX - 1, coordinateY },
                    new int[] { coordinateX, coordinateY + 1 },
                    new int[] { coordinateX, coordinateY - 1 }
            );

            for (int[] neighbour: neighbours) {
                if (Common.withinBounds(grid, neighbour[0], neighbour[1]) &&
                        grid[neighbour[0]][neighbour[1]] == (grid[coordinateX][coordinateY] + 1)) {

                    final List<String> newPath = new ArrayList<>(cellPath.path);
                    newPath.add(neighbour[0] + "," + neighbour[1]);
                    stack.push(new CellPath(neighbour[0], neighbour[1], newPath));
                }
            }
        }
    }
}
