package com.project.adventofcode.year2024.day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day05/input.txt";

    private static final Map<Integer, List<Integer>> graph = new HashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 5");

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
        int totalSum = 0;

        boolean lineBreakSeen = false;
        for (String line: lines) {

            if (line.isEmpty()) {
                lineBreakSeen = true;
                continue;
            }

            if (!lineBreakSeen) {
                buildGraph(line);

            } else {
                final List<Integer> nodes = Arrays.stream(line.split(","))
                        .map(Integer::parseInt)
                        .toList();

                for (int i = 0; i < nodes.size(); i++) {
                    if (i == nodes.size() - 1) {
                        int middleElem = nodes.size() / 2;
                        totalSum += nodes.get(middleElem);
                        continue;
                    }

                    if (!graph.containsKey(nodes.get(i)) || !graph.get(nodes.get(i)).contains(nodes.get(i + 1))) {
                        break;
                    }
                }
            }
        }

        System.out.println("totalSumPartOne=" + totalSum);
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));

        final Comparator<Integer> customComparator = (o1, o2) -> {
            if (graph.containsKey(o1) && graph.get(o1).contains(o2)) {
                return -1;
            }
            return 0;
        };

        int totalSum = 0;

        boolean lineBreakSeen = false;
        for (String line: lines) {

            if (line.isEmpty()) {
                lineBreakSeen = true;
                continue;
            }

            if (!lineBreakSeen) {
                buildGraph(line);

            } else {

                final List<Integer> nodes = new ArrayList<>(
                        Arrays.stream(line.split(","))
                                .map(Integer::parseInt)
                                .toList());

                for (int i = 0; i < nodes.size(); i++) {
                    if (i == nodes.size() - 1) {
                        continue;
                    }

                    if (!graph.containsKey(nodes.get(i)) || !graph.get(nodes.get(i)).contains(nodes.get(i + 1))) {
                        nodes.sort(customComparator);
                        int middleElem = nodes.size() / 2;
                        totalSum += nodes.get(middleElem);
                        break;
                    }
                }
            }
        }

        System.out.println("totalSumPartTwo=" + totalSum);
    }

    private static void buildGraph(final String line) {
        final String regex = "(\\d{2})\\|(\\d{2})";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(line);

        if (!matcher.find()) {
            return; // Invalid format, skip line
        }

        final int from = Integer.parseInt(matcher.group(1));
        final int to = Integer.parseInt(matcher.group(2));

        graph.computeIfAbsent(from, _ -> new ArrayList<>()).add(to);
        graph.computeIfAbsent(to, _ -> new ArrayList<>());
    }
}
