package com.project.adventofcode.year2024.day15;

import com.project.adventofcode.Common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    // Key aspects of the solution:
    //
    // Part 1 - Basic grid manipulation. The key challenge is implementing the move() function which may need to
    // perform multiple swaps if there are boxes blocking the path.

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day15/input.txt";

    private static final Map<Character, int[]> DIRECTIONS =
            Map.of(
                    '>', new int[] { 0 , 1 },
                    '<', new int[] { 0, -1 },
                    '^', new int[] { -1, 0 },
                    'v', new int[] { 1, 0 }
            );

    private static final Color DARK_GREEN = new Color(0, 100, 0);

    private static final boolean WITH_OUTPUT_PART_ONE = true;

    private static final boolean WITH_OUTPUT_PART_TWO = false;

    private record RobotPos(int x, int y) {}

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 15");

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
        final List<String> moves = new ArrayList<>();

        int rows = 0;
        int cols = lines.getFirst().length();
        boolean seenEmpty = false;

        // Process the input file
        for (String line : lines) {
            if (line.isEmpty()) {
                seenEmpty = true;
                continue;
            }

            if (!seenEmpty) {
                rows++;

            } else {
                moves.add(line);
            }
        }

        // Building the grid
        RobotPos robotPos = new RobotPos(0,0);
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = lines.get(i).charAt(j);
                if (grid[i][j] == '@') {
                    robotPos = new RobotPos(i, j);
                }
            }
        }

        int fileIterator = 1;

        // Compute all moves
        for (String move : moves) {
            for (int i = 0; i < move.length(); i++) {
                final char direction = move.charAt(i);
                robotPos = movePartOne(direction, robotPos, grid);

                if (WITH_OUTPUT_PART_ONE) {
                    final String fileName = String.format("output_%05d.png", fileIterator);
                    final String directoryPath = "src/main/resources/year2024/day15/output-part-one/";
                    final String fullPath = directoryPath + fileName;

                    Files.createDirectories(Path.of(directoryPath));
                    System.out.println("Printing " + fullPath);
                    saveGridAsImage(grid, fullPath);
                }

                fileIterator++;
            }
        }

        // Calculate the final result
        long total = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'O') {
                    total += (100L * i + j);
                }
            }
        }

        System.out.println("total=" + total);
    }

    private static void partTwo() throws IOException {
    }

    private static RobotPos movePartOne(final char direction, final RobotPos robotPos, final char[][] grid) {

        // Compute new coordinates
        final int[] coordinates = DIRECTIONS.get(direction);
        int currX = robotPos.x + coordinates[0];
        int currY = robotPos.y + coordinates[1];

        while (Common.withinBounds(grid, currX, currY) && grid[currX][currY] == 'O') {
            currX = currX + coordinates[0];
            currY = currY + coordinates[1];
        }

        if (Common.withinBounds(grid, currX, currY) && grid[currX][currY] == '#') {
            return robotPos;
        }

        if (Common.withinBounds(grid, currX, currY) && grid[currX][currY] == '.') {

            // while we are not at the coordinates where '@' is
            while (currX != robotPos.x || currY != robotPos.y) {

                // find previous
                int prevX = currX - coordinates[0];
                int prevY = currY - coordinates[1];

                // swap current with previous
                swap(grid, currX, currY, prevX, prevY);

                // if the element that was just swapped is the robot, end the iteration
                if (grid[currX][currY] == '@') {
                    return new RobotPos(currX, currY);
                }

                // new current
                currX = prevX;
                currY = prevY;
            }
        }

        // this should not happen
        return new RobotPos(-1, -1);
    }

    private static void swap(final char[][] grid, int currX, int currY, int prevX, int prevY) {
        char tmp = grid[currX][currY];
        grid[currX][currY] = grid[prevX][prevY];
        grid[prevX][prevY] = tmp;
    }

    // Method to save the grid as a PNG
    private static void saveGridAsImage(final char[][] grid, final String outputFileName) throws IOException {
        // Calculate dimensions
        final int cellSize = 20; // Size of each cell in pixels
        final int padding = 10;  // Padding around the grid
        final int width = grid[0].length * cellSize + padding * 2;
        final int height = grid.length * cellSize + padding * 2;

        // Create an image
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = image.createGraphics();

        // Set background color
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Set text color and font
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 16));

        // Draw the grid
        final FontMetrics metrics = g2d.getFontMetrics();
        int cellY = padding + metrics.getAscent();

        for (char[] row : grid) {
            int cellX = padding;
            for (char c : row) {

                // Set specific colors for different characters
                if (c == '@') {
                    g2d.setColor(Color.RED);

                } else if (c == 'O') {
                    g2d.setColor(Color.BLUE);

                } else if (c == '#') {
                    g2d.setColor(DARK_GREEN);

                } else {
                    g2d.setColor(Color.BLACK);
                }

                // Draw the character
                g2d.drawString(String.valueOf(c), cellX, cellY);

                // Move to the next cell
                cellX += cellSize;
            }
            cellY += cellSize; // Move to the next row
        }

        // Release resources
        g2d.dispose();

        // Write the image to file
        ImageIO.write(image, "png", new File(outputFileName));
    }
}
