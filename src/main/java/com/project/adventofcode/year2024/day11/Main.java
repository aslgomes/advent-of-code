package com.project.adventofcode.year2024.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/year2024/day11/input.txt";

    private static final int NUMBER_OF_BLINKS_PART_ONE = 25;

    private static final int NUMBER_OF_BLINKS_PART_TWO = 75;

    private static final ListNode DUMMY_NODE = new ListNode(-1, null);

    private static final ListNode HEAD = DUMMY_NODE;

    private static class ListNode {
        long number;
        ListNode next;

        public ListNode(long number, ListNode next) {
            this.number = number;
            this.next = next;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Day 11");

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
        final String[] numbers = line.split(" ");

        for (String number : numbers) {
            final long n = Long.parseLong(number);
            final ListNode newNode = new ListNode(n, null);
            insertAfter(findLast(), newNode);
        }

        for (int i = 0; i < NUMBER_OF_BLINKS_PART_ONE; i++) {

            // skipping dummy node
            ListNode curr = HEAD.next;
            while (curr != null) {

                if (curr.number == 0) {
                    curr.number = 1;
                    curr = curr.next;
                    continue;
                }

                if (String.valueOf(curr.number).length() % 2 == 0) {
                    final String stringValue = String.valueOf(curr.number);

                    // left number
                    curr.number = Long.parseLong(stringValue.substring(0, stringValue.length() / 2));

                    // right number
                    final long newNumber = Long.parseLong(stringValue.substring(stringValue.length() / 2));
                    insertAfter(curr, new ListNode(newNumber, null));
                    curr = curr.next.next;

                } else {
                    curr.number = curr.number * 2024;
                    curr = curr.next;
                }
            }
        }

        int numberOfStones = 0;
        ListNode curr = HEAD.next;

        while (curr != null) {
            numberOfStones++;
            curr = curr.next;
        }

        System.out.println("numberOfStones=" + numberOfStones);
    }

    private static void partTwo() throws IOException {
        final String line = Files.readAllLines(Path.of(INPUT_FILE_PATH)).getFirst();
        final String[] numbers = line.split(" ");

        Map<Long, Long> frequencies = new HashMap<>();
        for (String number : numbers) {
            final long n = Long.parseLong(number);
            frequencies.compute(n, (_, value) -> (value == null) ? 1 : value + 1);
        }

        for (int i = 0; i < NUMBER_OF_BLINKS_PART_TWO; i++) {
            final Map<Long, Long> localFrequencies = new HashMap<>();
            for (long key : frequencies.keySet()) {
                final long frequency = frequencies.get(key);

                if (key == 0) {
                    localFrequencies.compute(1L, (_, value) -> (value == null) ? frequency : value + 1);
                    continue;
                }

                if (String.valueOf(key).length() % 2 == 0) {
                    final String stringValue = String.valueOf(key);
                    final long leftNumber = Long.parseLong(stringValue.substring(0, stringValue.length() / 2));
                    final long rightNumber = Long.parseLong(stringValue.substring(stringValue.length() / 2));

                    localFrequencies.compute(leftNumber,
                            (_, value) -> (value == null) ? frequency : value + frequency);
                    localFrequencies.compute(rightNumber,
                            (_, value) -> (value == null) ? frequency : value + frequency);

                } else {
                    localFrequencies.compute(key * 2024,
                            (_, value) -> (value == null) ? frequency : value + frequency);
                }
            }

            frequencies = localFrequencies;
        }

        long numberOfStones = frequencies.values().stream().mapToLong(Long::longValue).sum();
        System.out.println("numberOfStones=" + numberOfStones);
    }

    private static ListNode findLast() {
        ListNode curr = HEAD;
        while (curr.next != null) {
            curr = curr.next;
        }
        return curr;
    }

    private static void insertAfter(final ListNode targetNode, final ListNode newNode) {
        newNode.next = targetNode.next;
        targetNode.next = newNode;
    }

    private static void printLinkedList() {
        // skipping dummy node
        ListNode curr = HEAD.next;
        while (curr != null) {
            System.out.print(curr.number + " ");
            curr = curr.next;
        }
        System.out.println();
    }
}
