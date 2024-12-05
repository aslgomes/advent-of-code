package com.project.adventofcode.day05;

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

    private static final String INPUT_FILE_PATH = "src/main/resources/day05/input.txt";

    private static final Map<Integer, List<Integer>> graph = new HashMap<>();

    private static Comparator<Integer> customComparator;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 5");
        partOne();
        partTwo();
    }

    // Correct answer: 5732
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

    // Correct answer: 4716
    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        int totalSum = 0;

        boolean lineBreakSeen = false;
        for (String line: lines) {

            if (line.isEmpty()) {
                lineBreakSeen = true;

                // Build the comparator after the graph is built
                customComparator = (o1, o2) -> {
                    if (graph.containsKey(o1) && graph.get(o1).contains(o2)) {
                        return -1;
                    }
                    return 0;
                };

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
