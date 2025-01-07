package com.project.adventofcode.year2024.day20;

import com.project.adventofcode.Common;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day20/input.txt";

    private static char[][] grid;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 20");

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

        int sX = -1;
        int sY = -1;
        int fastestNoCheat = -1;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'S') {
                    sX = i;
                    sY = j;
                    fastestNoCheat = bfs(grid, sX, sY);
                }
            }
        }

        final Map<Integer, Integer> cheats = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i > 0 && j > 0 && i < (grid.length - 1) && j < (grid[i].length - 1) && grid[i][j] == '#') {
                    grid[i][j] = '.';
                    final int timeTaken = bfs(grid, sX, sY);
                    final int key = fastestNoCheat - timeTaken;
                    if (timeTaken < fastestNoCheat && key >= 100) {
                        if (cheats.containsKey(key)) {
                            cheats.put(key, cheats.get(key) + 1);
                        } else {
                            cheats.put(key, 1);
                        }
                    }
                    grid[i][j] = '#';
                }
            }
        }

        int total = 0;
        for (Integer noCheats : cheats.keySet()) {
            total += cheats.get(noCheats);
        }

        System.out.println("total=" + total);
    }

    private static void partTwo() throws IOException {
    }

    private static int bfs(final char[][] grid, final int i, final int j) {
        final Queue<int[]> queue = new ArrayDeque<>();
        final Set<String> visited = new HashSet<>();
        queue.add(new int[] { i, j, 0 });

        while (!queue.isEmpty()) {
            final int[] current = queue.remove();
            final int x = current[0];
            final int y = current[1];
            final int currentLevel = current[2];
            final String key = x + "," + y;

            if (visited.contains(key)) {
                continue;
            }

            if (grid[x][y] == 'E') {
                return currentLevel;
            }

            visited.add(key);

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
