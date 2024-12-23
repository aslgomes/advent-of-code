package com.project.adventofcode.day23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Main {

    // Key aspects of the solution:
    //
    // tags - #graph #subsets
    //
    // Part 1 - Counts unique triplets of elements in a graph where at least one node starts with "t".
    //
    // Part 2 - Finds the largest subset of nodes in the graph where every node is directly connected to every other
    // node in the subset (a fully connected subset). Returns the sorted elements of this subset.

    private static final String INPUT_FILE_PATH = "src/main/resources/day23/input.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 23");

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
        final Map<String, List<String>> graph = new HashMap<>();

        for (String line : lines) {
            final String[] split = line.split("-");
            graph.computeIfAbsent(split[0], _ -> new ArrayList<>()).add(split[1]);
            graph.computeIfAbsent(split[1], _ -> new ArrayList<>()).add(split[0]);
        }

        final Set<String> tNodes = graph.keySet().stream()
                .filter(node -> node.startsWith("t"))
                .collect(Collectors.toSet());

        final Set<Set<String>> sets = new HashSet<>();

        for (String c1 : tNodes) {
            for (String c2 : graph.get(c1)) {
                for (String c3 : graph.get(c2)) {
                    if (graph.get(c3).contains(c1)) {
                        sets.add(new HashSet<>(Set.of(c1, c2, c3)));
                    }
                }
            }
        }

        System.out.println("total=" + sets.size());
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        final Map<String, List<String>> graph = new HashMap<>();

        for (String line : lines) {
            final String[] split = line.split("-");
            graph.computeIfAbsent(split[0], _ -> new ArrayList<>()).add(split[1]);
            graph.computeIfAbsent(split[1], _ -> new ArrayList<>()).add(split[0]);
        }

        Set<String> largestSet = Collections.emptySet();
        for (String c : graph.keySet()) {
            final Set<Set<String>> allConnected = buildSets(graph, new HashSet<>(Set.of(Set.of(c))), c, largestSet.size());
            for (Set<String> s : allConnected) {
                largestSet = s.size() > largestSet.size() ? s : largestSet;
            }
        }

        final Set<String> sorted = new TreeSet<>(largestSet);
        System.out.println("largestSet=" + String.join(",", sorted));
    }

    private static Set<Set<String>> buildSets(
            final Map<String, List<String>> graph,
            final Set<Set<String>> allConnected,
            final String computer,
            final int largestSetSize) {

        final List<String> neighbours = graph.get(computer);

        // If the number of neighbours of the computer being visited now is smaller than the largest set seen so far,
        // no point in searching here.
        if (neighbours.size() < largestSetSize) {
            return Collections.emptySet();
        }

        // loop through all the neighbours of a computer
        for (String neighbour : neighbours) {
            final Set<Set<String>> sTemp = new HashSet<>();

            // loop through all the existent all-connected sets at this point of time
            for (Set<String> set : allConnected) {

                boolean shouldAdd = true;

                // for each element of the current set, if the current neighbour is directly connected to each one of
                // them then a new subset that matches the criteria of the problem is formed
                for (String c : set) {
                    if (!graph.get(neighbour).contains(c)) {
                        shouldAdd = false;
                        break;
                    }
                }

                if (shouldAdd) {
                    final Set<String> newSet = new HashSet<>(set);
                    newSet.add(neighbour);
                    sTemp.add(newSet);
                }
            }

            allConnected.addAll(sTemp);
        }

        return allConnected;
    }
}
