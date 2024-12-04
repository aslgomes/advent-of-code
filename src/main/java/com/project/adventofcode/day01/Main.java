package com.project.adventofcode.day01;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day01/input.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 1");
        partOne();
        partTwo();
    }

    private static void partOne() throws IOException {
        final Path filePath = new File(INPUT_FILE_PATH).toPath();

        final List<Integer> list1 = new ArrayList<>();
        final List<Integer> list2 = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] numbers = line.split("\\s+");
                list1.add(Integer.parseInt(numbers[0]));
                list2.add(Integer.parseInt(numbers[1]));
            }
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
        final Path filePath = new File(INPUT_FILE_PATH).toPath();

        final List<Integer> list = new ArrayList<>();
        final Map<Integer, Integer> map = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] numbers = line.split("\\s+");
                list.add(Integer.parseInt(numbers[0]));

                final Integer i = Integer.parseInt(numbers[1]);
                map.put(i, map.getOrDefault(i, 0) + 1);
            }
        }

        int similarity = 0;
        for (int i = 0; i < list.size(); i++) {
            similarity += list.get(i) * map.getOrDefault(list.get(i), 0);
        }

        System.out.println("similarity=" + similarity);
    }
}
