package com.project.adventofcode.year2024.day12;

import com.project.adventofcode.Common;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day12/input.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 12");

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

    private enum Direction {
        VERTICAL,
        HORIZONTAL
    }

    private enum Side {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    private record Corner(int x, int y) {
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    private record EdgeKey(Corner from, Corner to) { }

    private record Edge(Corner from, Corner to, Direction direction, Side side) {
        @Override
        public String toString() {
            return "[" + from + " -> " + to + "], direction=" + direction + ", side=" + side;
        }
    }

    private record Price(int area, List<Edge> edges) { }

    private static void partOne() throws IOException {
        final char[][] grid = Common.loadCharGrid(INPUT_FILE_PATH);
        final boolean[][] visited = new boolean[grid.length][grid[0].length];

        int totalFence = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!visited[i][j]) {
                    final Price price = dfs(grid, visited, i, j);

                    // Group edges by count (how many times each edge occurs)
                    final Map<EdgeKey, Long> edgeCount = price.edges.stream()
                            .collect(Collectors.groupingBy(
                                    edge -> new EdgeKey(edge.from(), edge.to()),
                                    Collectors.counting()
                            ));

                    // Filter out edges where the count is greater than 1
                    final List<Edge> filteredEdges = price.edges.stream()
                            .filter(edge -> edgeCount.get(new EdgeKey(edge.from(), edge.to())) <= 1)
                            .toList();

                    totalFence += (price.area * filteredEdges.size());
                }
            }
        }

        System.out.println("totalFence=" + totalFence);
    }

    private static void partTwo() throws IOException {
        final char[][] grid = Common.loadCharGrid(INPUT_FILE_PATH);
        final boolean[][] visited = new boolean[grid.length][grid[0].length];

        int totalFence = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!visited[i][j]) {
                    final Price price = dfs(grid, visited, i, j);

                    final Map<EdgeKey, Long> edgeCount = price.edges.stream()
                            .collect(Collectors.groupingBy(
                                    edge -> new EdgeKey(edge.from(), edge.to()),
                                    Collectors.counting()
                            ));

                    final List<Edge> filteredEdges = price.edges.stream()
                            .filter(edge -> edgeCount.get(new EdgeKey(edge.from(), edge.to())) <= 1)
                            .toList();

                    final List<Edge> horizontalEdges = filteredEdges.stream()
                            .filter(e -> e.direction.equals(Direction.HORIZONTAL))
                            .toList();

                    final List<Edge> verticalEdges = filteredEdges.stream()
                            .filter(e -> e.direction.equals(Direction.VERTICAL))
                            .toList();

                    final List<Edge> rowsMerged = mergeRows(horizontalEdges);
                    final List<Edge> colsMerged = mergeCols(verticalEdges);
                    totalFence += (price.area * (rowsMerged.size() + colsMerged.size()));
                }
            }
        }

        System.out.println("totalFence=" + totalFence);
    }

    private static Price dfs(final char[][] grid, final boolean[][] visited, final int x, final int y) {

        final List<Edge> allEdges = new ArrayList<>();
        int area = 0;

        final char currentChar = grid[x][y];

        final Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {x, y});

        while (!stack.isEmpty()) {
            final int[] curr = stack.pop();

            // do not re-visit nodes
            if (visited[curr[0]][curr[1]]) {
                continue;
            }

            // everytime a new cell is visited, the total area is incremented by 1
            area++;
            visited[curr[0]][curr[1]] = true;

            allEdges.addAll(List.of(
                    new Edge(new Corner(curr[0], curr[1]), new Corner(curr[0], curr[1] + 1),
                            Direction.HORIZONTAL, Side.TOP),
                    new Edge(new Corner(curr[0], curr[1] + 1), new Corner(curr[0] + 1, curr[1] + 1),
                            Direction.VERTICAL, Side.RIGHT),
                    new Edge(new Corner(curr[0] + 1, curr[1]), new Corner(curr[0] + 1, curr[1] + 1),
                            Direction.HORIZONTAL, Side.BOTTOM),
                    new Edge(new Corner(curr[0], curr[1]), new Corner(curr[0] + 1, curr[1]),
                            Direction.VERTICAL, Side.LEFT))
            );

            final List<int[]> neighbours = List.of(
                    new int[] { curr[0], curr[1] + 1 },
                    new int[] { curr[0], curr[1] - 1 },
                    new int[] { curr[0] + 1, curr[1] },
                    new int[] { curr[0] - 1, curr[1] }
            );

            for (int[] neighbour : neighbours) {
                if (Common.withinBounds(grid, neighbour[0], neighbour[1])
                        && grid[neighbour[0]][neighbour[1]] == currentChar) {

                    if (!visited[neighbour[0]][neighbour[1]]) {
                        stack.push(neighbour);
                    }
                }
            }
        }

        return new Price(area, allEdges);
    }

    private static List<Edge> mergeRows(final List<Edge> inputEdges) {
        return mergeEdges(
                inputEdges,
                Direction.HORIZONTAL,
                edge -> edge.from.x,
                edge -> edge.from.y
        );
    }

    private static List<Edge> mergeCols(final List<Edge> inputEdges) {
        return mergeEdges(
                inputEdges,
                Direction.VERTICAL,
                edge -> edge.from.y,
                edge -> edge.from.x
        );
    }

    private static List<Edge> mergeEdges(
            final List<Edge> inputEdges,
            final Direction direction,
            final Function<Edge, Integer> primaryKeyExtractor,
            final Function<Edge, Integer> secondaryKeyExtractor) {

        final List<Edge> results = new ArrayList<>();
        final List<Edge> edges = new ArrayList<>(inputEdges);

        edges.sort(
                Comparator.comparing(primaryKeyExtractor)
                        .thenComparing(secondaryKeyExtractor));

        results.add(edges.getFirst());
        for (int i = 1; i < edges.size(); i++) {
            final Edge currentEdge = edges.get(i);
            final Edge lastEdge = results.getLast();

            if (lastEdge.to.equals(currentEdge.from) && lastEdge.side == currentEdge.side) {
                results.removeLast();
                results.add(new Edge(lastEdge.from, currentEdge.to, direction, lastEdge.side));

            } else {
                results.add(new Edge(currentEdge.from, currentEdge.to, direction, currentEdge.side));
            }
        }

        return results;
    }
}
