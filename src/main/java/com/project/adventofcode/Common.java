package com.project.adventofcode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Common {

    public static void fillGrid(final char[][] grid, final char c) {
        for (char[] chars : grid) {
            Arrays.fill(chars, c);
        }
    }

    public static char[][] loadCharGrid(final String inputFilePath) throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(inputFilePath));
        final int rows = lines.size();
        final int cols = lines.getFirst().length();

        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }

    public static int[][] loadIntGrid(final String inputFilePath) throws IOException {
        final List<String> lines = Files.readAllLines(Path.of(inputFilePath));
        final int rows = lines.size();
        final int cols = lines.getFirst().length();

        int[][] grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                grid[i][j] = Integer.parseInt(String.valueOf(lines.get(i).charAt(j)));
            }
        }

        return grid;
    }

    public static boolean withinBounds(final char[][] grid, final int x, final int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public static boolean withinBounds(final int[][] grid, final int x, final int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public static void printCharGrid(final char[][] grid) {
        for (char[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(chars[j] + " ");
            }
            System.out.println();
        }
    }

    public static void printBooleanGrid(final boolean[][] grid) {
        for (boolean[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(chars[j] + " ");
            }
            System.out.println();
        }
    }

    public static void printIntGrid(final int[][] grid) {
        for (int[] nums : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(nums[j] + " ");
            }
            System.out.println();
        }
    }

    public static void outputImage(
            final boolean shouldProceed,
            final String path,
            final int fileIterator,
            final char[][] grid,
            final Map<Character, Color> colorMap) throws IOException {

        if (shouldProceed) {
            Files.createDirectories(Path.of(path));
            final String fullPath = path + String.format("output_%05d.png", fileIterator);
            System.out.println("Printing " + fullPath);
            Common.saveGridAsImage(grid, fullPath, colorMap);
        }
    }

    // Method to save the grid as a PNG
    private static void saveGridAsImage(
            final char[][] grid,
            final String fullPath,
            final Map<Character, Color> colorMap) throws IOException {

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
                g2d.setColor(colorMap.getOrDefault(c, Color.BLACK));

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
        ImageIO.write(image, "png", new File(fullPath));
    }
}
