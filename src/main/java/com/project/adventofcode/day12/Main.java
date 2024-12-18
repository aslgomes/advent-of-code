package com.project.adventofcode.day12;

import com.project.adventofcode.Common;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day12/smaller-input-2.txt";

    private static boolean[][] processed;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 1");

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
//        final char[][] grid = Common.loadCharGrid(INPUT_FILE_PATH);
//        int fencePrice = 0;
//        processed = new boolean[grid.length][grid[0].length];
//
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid[0].length; j++) {
//                if (!processed[i][j]) {
//                    System.out.println("processing char-" + grid[i][j]);
//                    fencePrice += dfs(grid, i, j);
//                }
//            }
//        }
//
//        System.out.println("fencePrice=" + fencePrice);
    }

//    private static int dfs(final char[][] grid, int x, int y) {
//        int area = 0;
//        int perimeter = 0;
//
//        final Deque<int[]> stack = new ArrayDeque<>();
//        stack.push(new int[] {x, y});
//
//        while (!stack.isEmpty()) {
//            final int[] coordinates = stack.pop();
//            final int coordinateX = coordinates[0];
//            final int coordinateY = coordinates[1];
//
//            if (processed[coordinateX][coordinateY]) {
//                continue;
//            }
//
//            processed[coordinateX][coordinateY] = true;
//            area++;
//            perimeter += 4;
//            System.out.println("processed grid[" + coordinateX + "][" + coordinateY + "]");
//            System.out.println("area = " + area);
//            System.out.println("perimeter = " + perimeter);
//            final char curr = grid[coordinateX][coordinateY];
//
//            final List<int[]> neighbours = List.of(
//                    new int[] { coordinateX + 1, coordinateY },
//                    new int[] { coordinateX - 1, coordinateY },
//                    new int[] { coordinateX, coordinateY + 1 },
//                    new int[] { coordinateX, coordinateY - 1 }
//            );
//
//            for (int[] neighbour: neighbours) {
//                if (Common.withinBounds(grid, neighbour[0], neighbour[1])
//                        && grid[neighbour[0]][neighbour[1]] == curr) {
//
//                    if (!processed[neighbour[0]][neighbour[1]]) {
//                        perimeter
//                        stack.push(new int[] { neighbour[0], neighbour[1] });
//                    }
//                }
//            }
//        }
//
//        System.out.println(" area=" + area);
//        System.out.println(" perimeter=" + perimeter);
//        System.out.println();
//        System.out.println();
//
//        return area * perimeter;
//    }

    private static void partTwo() throws IOException {
    }
}
