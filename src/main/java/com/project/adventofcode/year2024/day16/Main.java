package com.project.adventofcode.year2024.day16;

import com.project.adventofcode.Common;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Main {

    // Key aspects of the solution:
    //
    // Part 1 - Dijkstra's algorithm is used to find the path with the lowest cost to reach the target cell 'E'.
    //
    // Part 2 - Tracking Optimal Paths.
    //
    //  - Maintain a distTo and predecessors map for all nodes visited. These maps are crucial for identifying nodes
    //  that are part of one or more optimal paths, as there may be multiple paths with the same minimum cost.
    //
    //  - Depth-First Search (DFS) for Path Identification - Starting from the target node 'E', a DFS is done in
    // reverse (backwards traversal) to explore all optimal paths. All unique cells that lie on any of
    // these optimal paths are identified and returned as the result.

    // Directions
    //
    // 1 -> RIGHT
    // 2 -> LEFT
    // 3 -> UP
    // 4 -> DOWN

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day16/input.txt";

    private static final Map<Integer, Map<Integer, Integer>> ROTATION_COST = Map.of(
            1, Map.of(1, 1, 2, 2000 + 1, 3, 1000 + 1, 4, 1000 + 1),
            2, Map.of(1, 2000 + 1, 2, 1, 3, 1000 + 1, 4, 1000 + 1),
            3, Map.of(1, 1000 + 1, 2, 1000 + 1, 3, 1, 4, 2000 + 1),
            4, Map.of(1, 1000 + 1, 2, 1000 + 1, 3, 2000 + 1, 4, 1));

    record Edge(int x, int y, int direction) {}

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 16");

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
        final char[][] grid = Common.loadCharGrid(INPUT_FILE_PATH);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'S') {
                    final int minCost = shortestPath(grid, i, j);
                    System.out.println("lowestScore=" + minCost);
                }
            }
        }
    }

    private static void partTwo() throws IOException {
        final char[][] grid = Common.loadCharGrid(INPUT_FILE_PATH);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'S') {
                    shortestAllPaths(grid, i, j);
                }
            }
        }
    }

    private static int shortestPath(final char[][] grid, final int startX, final int startY) {

        final Map<Edge, Integer> distTo = new HashMap<>();
        final PriorityQueue<Edge> minQueue = new PriorityQueue<>(
                Comparator.comparingInt(s -> distTo.getOrDefault(s, Integer.MAX_VALUE)));

        final Edge startEdge = new Edge(startX, startY, 1);
        minQueue.add(startEdge);
        distTo.put(startEdge, 0);

        int minCost = Integer.MAX_VALUE;
        while (!minQueue.isEmpty()) {
            final Edge currentEdge = minQueue.poll();

            if (grid[currentEdge.x][currentEdge.y] == 'E') {
                minCost = Math.min(minCost, distTo.get(currentEdge));
                continue;
            }

            final int[][] neighbours = {
                    { 0, 1, 1 },   // Right
                    { 0, -1, 2 },  // Left
                    { -1, 0, 3 },  // Up
                    { 1, 0, 4 }    // Down
            };

            for (int[] neighbour : neighbours) {
                int newX = currentEdge.x() + neighbour[0];
                int newY = currentEdge.y() + neighbour[1];
                int directionTo = neighbour[2];

                if (Common.withinBounds(grid, newX, newY) && grid[newX][newY] != '#') {
                    final int rotationCost = ROTATION_COST.get(currentEdge.direction()).get(directionTo);
                    final int newCost = distTo.getOrDefault(currentEdge, Integer.MAX_VALUE) + rotationCost;
                    final Edge newEdge = new Edge(newX, newY, directionTo);

                    if (newCost < distTo.getOrDefault(newEdge, Integer.MAX_VALUE)) {
                        distTo.put(newEdge, newCost);
                        minQueue.add(newEdge);
                    }
                }
            }
        }

        return minCost;
    }

    private static void shortestAllPaths(final char[][] grid, final int startX, final int startY) {

        Edge targetEdge = null;
        final Map<Edge, Integer> distTo = new HashMap<>();
        final Map<Edge, Set<Edge>> predecessors = new HashMap<>();

        final PriorityQueue<Edge> minQueue = new PriorityQueue<>(
                Comparator.comparingInt(s -> distTo.getOrDefault(s, Integer.MAX_VALUE)));

        final Edge startEdge = new Edge(startX, startY, 1);
        minQueue.add(startEdge);
        distTo.put(startEdge, 0);

        int minCost = Integer.MAX_VALUE;
        while (!minQueue.isEmpty()) {
            final Edge currentEdge = minQueue.poll();

            if (grid[currentEdge.x][currentEdge.y] == 'E') {
                if (distTo.get(currentEdge) < minCost) {
                    minCost = distTo.get(currentEdge);
                    targetEdge = currentEdge;
                }
                continue;
            }

            final int[][] neighbours = {
                    {0, 1, 1},   // Right
                    {0, -1, 2},  // Left
                    {-1, 0, 3},  // Up
                    {1, 0, 4}    // Down
            };

            for (int[] neighbour : neighbours) {
                int newX = currentEdge.x() + neighbour[0];
                int newY = currentEdge.y() + neighbour[1];
                int directionTo = neighbour[2];

                if (Common.withinBounds(grid, newX, newY) && grid[newX][newY] != '#') {
                    final int rotationCost = ROTATION_COST.get(currentEdge.direction()).get(directionTo);
                    final int newCost = distTo.getOrDefault(currentEdge, Integer.MAX_VALUE) + rotationCost;
                    final Edge newEdge = new Edge(newX, newY, directionTo);

                    if (newCost <= distTo.getOrDefault(newEdge, Integer.MAX_VALUE)) {
                        distTo.put(newEdge, newCost);
                        minQueue.add(newEdge);
                        predecessors.computeIfAbsent(newEdge, _ -> new HashSet<>()).add(currentEdge);
                    }
                }
            }
        }

        int totalUniqueTiles = markUniqueCells(grid, predecessors, targetEdge);
        System.out.println("totalUniqueTiles=" + totalUniqueTiles);
    }

    // DFS
    private static int markUniqueCells(
            final char[][] grid,
            final Map<Edge, Set<Edge>> predecessors,
            final Edge startingEdge) {

        int totalUniqueTiles = 0;

        final Set<Edge> visitedCells = new HashSet<>();
        final Deque<Edge> stack = new ArrayDeque<>();
        stack.push(startingEdge);

        while (!stack.isEmpty()) {
            final Edge currentEdge = stack.pop();

            if (visitedCells.contains(currentEdge)) {
                continue;
            }

            visitedCells.add(currentEdge);

            if (grid[currentEdge.x][currentEdge.y] != 'O') {
                grid[currentEdge.x][currentEdge.y] = 'O';
                totalUniqueTiles++;
            }

            for (Edge neighbour : predecessors.getOrDefault(currentEdge, new HashSet<>())) {
                stack.push(neighbour);
            }
        }

        return totalUniqueTiles;
    }
}
