package com.project.adventofcode.year2024.day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day14/input.txt";

    // fixed
    private static final int ROWS = 103;

    // fixed
    private static final int COLS = 101;

    private static final int NUMBER_OF_TIMES_PART_ONE = 100;

    private static final int NUMBER_OF_TIMES_PART_TWO = 10000;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 14");

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
        final String regex = "p=(-?\\d+),(-?\\d+)\\sv=(-?\\d+),(-?\\d+)";

        final Map<String, int[]> positions = new HashMap<>();
        final Map<String, int[]> speeds = new HashMap<>();

        for (String line: lines) {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                final String id = UUID.randomUUID().toString();
                positions.put(id, new int[] { Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1)) });
                speeds.put(id, new int[] { Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(3)) });
            }
        }

        final int[][] grid = new int[ROWS][COLS];
        move(grid, positions, speeds);
        System.out.println("safetyScore=" + safetyScore(grid));
    }

    private static void partTwo() throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(INPUT_FILE_PATH));
        final String regex = "p=(-?\\d+),(-?\\d+)\\sv=(-?\\d+),(-?\\d+)";

        final Map<String, int[]> positions = new HashMap<>();
        final Map<String, int[]> speeds = new HashMap<>();

        for (String line: lines) {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                final String id = UUID.randomUUID().toString();
                positions.put(id, new int[] { Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1)) });
                speeds.put(id, new int[] { Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(3)) });
            }
        }

        moveAndPrint(positions, speeds);
    }

    private static void move(
            final int[][] grid,
            final Map<String, int[]> positions,
            final Map<String, int[]> speeds) {

        for (String key : positions.keySet()) {
            for (int i = 0; i < NUMBER_OF_TIMES_PART_ONE; i++) {
                int newX = ((positions.get(key)[0] + speeds.get(key)[0]) % ROWS + ROWS) % ROWS;
                int newY = ((positions.get(key)[1] + speeds.get(key)[1]) % COLS + COLS) % COLS;
                positions.put(key, new int[] { newX, newY });
            }
            grid[positions.get(key)[0]][positions.get(key)[1]]++;
        }
    }

    private static void moveAndPrint(
            final Map<String, int[]> positions,
            final Map<String, int[]> speeds) throws IOException {

        for (int i = 0; i < NUMBER_OF_TIMES_PART_TWO; i++) {
            int[][] grid = new int[ROWS][COLS];

            for (String key : positions.keySet()) {
                int newX = ((positions.get(key)[0] + speeds.get(key)[0]) % ROWS + ROWS) % ROWS;
                int newY = ((positions.get(key)[1] + speeds.get(key)[1]) % COLS + COLS) % COLS;
                positions.put(key, new int[] { newX, newY });
                grid[positions.get(key)[0]][positions.get(key)[1]]++;
            }

            //
            // Performed the safetyScore calculation to identify if the number of robots in a specific quadrant exceeds
            // 200. The threshold of 200 was chosen based on observations that, up until 5000 seconds, most quadrants
            // typically have around 100 robots.
            //
            // At i = 7137 (for the provided input.txt file), quadrant 1 contains 243 robots. The Xmas tree pattern is
            // identified by printing the grid at this point in time. The final answer is calculated at i + 1 seconds.
            //
            if (i == 7137) {
                final String outputFileName = "src/main/resources/year2024/day14/output_" + i + ".txt";
                System.out.println("Printing " + outputFileName);
                Files.writeString(
                        Path.of(outputFileName),
                        printIntGridWithoutZeros(grid),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE);
            }
        }
    }

    private static long safetyScore(final int[][] grid) {
        final int[][] quadrantBoundaries = new int[][] {
                { 0, ROWS / 2, 0 , COLS / 2 },
                { 0, ROWS / 2, (COLS / 2 + 1), COLS },
                { (ROWS / 2 + 1), ROWS, 0, COLS / 2 },
                { (ROWS / 2 + 1), ROWS, (COLS / 2 + 1), COLS }
        };

        long qSum = 1;
        for (int[] q : quadrantBoundaries) {
            int localQ = 0;
            for (int i = q[0]; i < q[1]; i++) {
                for (int j = q[2]; j < q[3]; j++) {
                    localQ += grid[i][j];
                }
            }
            qSum *= localQ;
        }

        return qSum;
    }

    private static String printIntGridWithoutZeros(final int[][] grid) {
        final StringBuilder sb = new StringBuilder();
        for (int[] nums : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                if (nums[j] == 0) {
                    sb.append(". ");
                    continue;
                }
                sb.append(nums[j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
