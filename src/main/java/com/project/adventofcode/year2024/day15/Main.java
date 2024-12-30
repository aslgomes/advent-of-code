package com.project.adventofcode.year2024.day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day01/input.txt";

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
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));

        final List<Integer> list1 = new ArrayList<>();
        final List<Integer> list2 = new ArrayList<>();

        for (String line : lines) {
            final String[] numbers = line.split("\\s+");
            list1.add(Integer.parseInt(numbers[0]));
            list2.add(Integer.parseInt(numbers[1]));
        }

        Collections.sort(list1);
        Collections.sort(list2);

        int totalDistance = 0;
        for (int i = 0; i < list1.size(); i++) {
            totalDistance += Math.abs((list1.get(i) - list2.get(i)));
        }

        System.out.println("totalDistance=" + totalDistance);
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));

        final List<Integer> list = new ArrayList<>();
        final Map<Integer, Integer> map = new HashMap<>();

        for (String line : lines) {
            final String[] numbers = line.split("\\s+");
            list.add(Integer.parseInt(numbers[0]));

            final Integer i = Integer.parseInt(numbers[1]);
            map.put(i, map.getOrDefault(i, 0) + 1);
        }

        int similarity = 0;
        for (Integer integer : list) {
            similarity += integer * map.getOrDefault(integer, 0);
        }

        System.out.println("similarity=" + similarity);
    }
}
