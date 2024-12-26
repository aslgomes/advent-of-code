package com.project.adventofcode.year2024.day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/day09/year2024/input.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 9");

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
        final String line = Files.readAllLines(Path.of(INPUT_FILE_PATH)).getFirst();
        System.out.println("checksum=" + checksum(compact(convertToFileBlock(line))));
    }

    private static void partTwo() throws IOException {
        final String line = Files.readAllLines(Path.of(INPUT_FILE_PATH)).getFirst();
        System.out.println("checksum=" + checksum(compactWholeFiles(convertToFileBlock(line))));
    }

    private static List<Integer> convertToFileBlock(final String line) {
        final List<Integer> fileBlock = new ArrayList<>();

        int index = 0;
        boolean readingFreeSpace = false;

        for (int i = 0; i < line.length(); i++) {
            final int currentDigit = Integer.parseInt(String.valueOf(line.charAt(i)));
            for (int j = 0; j < currentDigit; j++) {
                if (readingFreeSpace) {
                    fileBlock.add(-1);

                } else {
                    fileBlock.add(index);
                }
            }
            if (!readingFreeSpace) {
                index++;
            }
            readingFreeSpace = !readingFreeSpace;
        }

        return fileBlock;
    }

    private static List<Integer> compact(final List<Integer> fileBlock) {
        final List<Integer> compacted = new ArrayList<>();
        int i = 0;
        int j = fileBlock.size() - 1;

        while (i <= j) {
            while (i <= j && fileBlock.get(i) != -1) {
                compacted.add(fileBlock.get(i++));
            }

            while (i <= j && fileBlock.get(j) == -1) {
                j--;
            }

            if (i < j) {
                compacted.add(fileBlock.get(j--));
                i++;
            }
        }

        return compacted;
    }

    private static List<Integer> compactWholeFiles(final List<Integer> fileBlock) {

        final int[] fileBlockArray = fileBlock.stream().mapToInt(Integer::intValue).toArray();

        int j = fileBlock.size() - 1;
        while (j >= 0) {

            // Skip trailing `-1` values
            if (fileBlockArray[j] == -1) {
                j--;
                continue;
            }

            // Identify the file and its size
            int currFile = fileBlockArray[j];
            int currFileSize = 0;
            while (j >= 0 && fileBlockArray[j] == currFile) {
                currFileSize++;
                j--;
            }

            int left = 0;
            int spaceSize = 0;

            while (left < (j + 1) && spaceSize < currFileSize) {
                // Finding the start of an empty space
                while (left < (j + 1) && fileBlockArray[left] != -1) {
                    left++;
                }

                spaceSize = 0;

                // Determining the amount of space available
                while (left < (j + 1) && fileBlockArray[left] == -1) {
                    spaceSize++;
                    left++;
                }
            }

            // Where the space starts on the left
            int startLeft = left - spaceSize;

            // Check if the space is large enough
            if (spaceSize >= currFileSize) {
                // Move the file to the identified space
                for (int i = startLeft; i < (startLeft + currFileSize); i++) {
                    fileBlockArray[i] = currFile;
                }
                // Clear the file from its original position
                for (int i = (j + 1); i < (j + 1) + currFileSize; i++) {
                    fileBlockArray[i] = -1;
                }
            }
        }

        // Convert the compacted array back to a list
        final List<Integer> compacted = new ArrayList<>();
        for (int i = 0; i < fileBlockArray.length; i++) {
            compacted.add(fileBlockArray[i]);
        }

        return compacted;
    }

    private static long checksum(final List<Integer> compactedBlock) {
        long checksum = 0;
        for (int i = 0; i < compactedBlock.size(); i++) {
            if (compactedBlock.get(i) != -1) {
                checksum += (long) i * compactedBlock.get(i);
            }
        }
        return checksum;
    }
}
