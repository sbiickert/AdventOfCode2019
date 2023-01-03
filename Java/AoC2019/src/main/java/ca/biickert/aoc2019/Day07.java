package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.Algorithms;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author sjb
 */
public class Day07 extends Solution {

    public Day07() {
        super(7, "Amplification Circuit", true);
    }

    @Override
    public Result solve(String filename, int index) {
        var result = super.solve(filename, index);

        List<String> input = InputReader.readGroupedInputFile(filename, index);

        var part1Solution = solvePartOne(input.get(0));
        var part2Solution = solvePartTwo(input.get(0));

        result = new Result(Integer.toString(part1Solution), Integer.toString(part2Solution));

        return result;
    }

    private int solvePartOne(String program) {
        List<IntCodeComputer7> amps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            amps.add(new IntCodeComputer7(program));
        }

        int maxSignal = 0;
        List<Integer> maxPhaseSettings;

        // Phase settings are 0 to 4, and each can only be used once
        // i.e. permutations
        List<Integer> phases = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        var perms = Algorithms.getAllPermutations(phases);
        for (var phaseSettings : perms) {
            int inputSignal = 0;
            //System.out.println(phaseSettings);
            //System.out.print(String.format("%d -> ", inputSignal));
            for (int i = 0; i < 5; i++) {
                var amp = amps.get(i);
                amp.load(program);
                amp.input.add(phaseSettings.get(i));
                amp.input.add(inputSignal);
                amp.run(false, true);
                inputSignal = amp.output;
                //System.out.print(String.format("%d -> ", inputSignal));
            }
            //System.out.println();
            if (inputSignal > maxSignal) {
                maxPhaseSettings = phaseSettings;
                maxSignal = inputSignal;
                System.out.println(maxPhaseSettings + " -> " + String.valueOf(maxSignal));
            }
        }

        return maxSignal;
    }

    private int solvePartTwo(String program) {

        int maxSignal = 0;
        List<Integer> maxPhaseSettings;

        // Phase settings are 5 to 9, and each can only be used once
        // i.e. permutations
        List<Integer> phases = new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9));
        var perms = Algorithms.getAllPermutations(phases);
        for (var phaseSettings : perms) {
            List<IntCodeComputer7> amps = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                amps.add(new IntCodeComputer7(program));
                amps.get(i).input.add(phaseSettings.get(i));
            }

            int inputSignal = 0;

            while (amps.get(0).isHalted == false) {
                for (int i = 0; i < 5; i++) {
                    var amp = amps.get(i);
                    amp.input.add(inputSignal);
                    amp.run(true, false);
                    inputSignal = amp.output;
                    //System.out.print(String.format("%d -> ", inputSignal));
                }
            }
            //System.out.println();
            if (inputSignal > maxSignal) {
                maxPhaseSettings = phaseSettings;
                maxSignal = inputSignal;
                System.out.println(maxPhaseSettings + " -> " + String.valueOf(maxSignal));
            }
        }

        return maxSignal;
    }

}

final class IntCodeComputer7 {

    static final int OPCODE_ADD = 1;
    static final int OPCODE_MULTIPLY = 2;
    static final int OPCODE_INPUT = 3;
    static final int OPCODE_OUTPUT = 4;
    static final int OPCODE_JUMP_IF_TRUE = 5;
    static final int OPCODE_JUMP_IF_FALSE = 6;
    static final int OPCODE_LESS_THAN = 7;
    static final int OPCODE_EQUALS = 8;
    static final int EXIT = 99;

    private List<Integer> program;
    private int ptr;

    public Queue<Integer> input = new LinkedList<>();
    public Integer output;
    public boolean isHalted = false;

    public IntCodeComputer7(String programDefn) {
        this.load(programDefn);
    }

    public void load(String programDefn) {
        var list = Arrays.asList(programDefn.split(","));
        List<Integer> pgm = list.stream().map(Integer::valueOf).toList();
        load(pgm);
    }

    private void load(List<Integer> program) {
        this.program = new ArrayList(program);
        this.ptr = 0;
    }

    public void run(boolean toOutput, boolean resetPtr) {
        //System.out.println("Starting execute");
        boolean bExit = false;
        if (resetPtr) {
            ptr = 0;
        }
        int input1;
        int input2;
        int outputPos;

        while (!bExit) {
            var opCode = parseOpCode(getImmediateValue(ptr));

            switch (opCode.get(0)) {
                case OPCODE_ADD:
                    input1 = getValue(ptr + 1, opCode.get(1));
                    input2 = getValue(ptr + 2, opCode.get(2));
                    outputPos = getImmediateValue(ptr + 3);
                    setImmediateValue(outputPos, input1 + input2);
                    ptr += 4;
                    break;
                case OPCODE_MULTIPLY:
                    input1 = getValue(ptr + 1, opCode.get(1));
                    input2 = getValue(ptr + 2, opCode.get(2));
                    outputPos = getImmediateValue(ptr + 3);
                    setImmediateValue(outputPos, input1 * input2);
                    ptr += 4;
                    break;
                case OPCODE_INPUT:
                    outputPos = getImmediateValue(ptr + 1);
                    Integer i = input.poll();
                    try {
                        setImmediateValue(outputPos, i);
                    } catch (NullPointerException npe) {
                        System.out.println(npe);
                        System.exit(1);
                    }
                    ptr += 2;
                    break;
                case OPCODE_OUTPUT:
                    output = getValue(ptr + 1, opCode.get(1));
                    ptr += 2;
                    if (toOutput) {
                        bExit = true;
                    }
                    break;
                case OPCODE_JUMP_IF_TRUE:
                    input1 = getValue(ptr + 1, opCode.get(1));
                    if (input1 != 0) {
                        ptr = getValue(ptr + 2, opCode.get(2));
                    } else {
                        ptr += 3;
                    }
                    break;
                case OPCODE_JUMP_IF_FALSE:
                    input1 = getValue(ptr + 1, opCode.get(1));
                    if (input1 == 0) {
                        ptr = getValue(ptr + 2, opCode.get(2));
                    } else {
                        ptr += 3;
                    }
                    break;
                case OPCODE_LESS_THAN:
                    input1 = getValue(ptr + 1, opCode.get(1));
                    input2 = getValue(ptr + 2, opCode.get(2));
                    outputPos = getImmediateValue(ptr + 3);
                    setImmediateValue(outputPos, (input1 < input2) ? 1 : 0);
                    ptr += 4;
                    break;
                case OPCODE_EQUALS:
                    input1 = getValue(ptr + 1, opCode.get(1));
                    input2 = getValue(ptr + 2, opCode.get(2));
                    outputPos = getImmediateValue(ptr + 3);
                    setImmediateValue(outputPos, (input1 == input2) ? 1 : 0);
                    ptr += 4;
                    break;
                case EXIT:
                    bExit = true;
                    isHalted = true;
                    break;
                default:
                    System.out.println(String.format("Unexpected opcode %d", getPositionValue(ptr)));
                    System.exit(1);
            }
        }
    }

    public int getValue(int position, int flag) {
        try {
            if (flag == 1) {
                return getImmediateValue(position);
            }
            return getPositionValue(position);
        } catch (NullPointerException npe) {
            System.out.println("Null when getting value. Position: " + position + ", is immediate: " + flag);
            System.exit(1);
        }
        return -9999;
    }

    public int getPositionValue(int position) {
        return getImmediateValue(program.get(position));
    }

    public int getImmediateValue(int position) {
        //System.out.println(String.format("Read %d from position %d", program.get(position), position));
        return program.get(position);
    }

    private void setImmediateValue(int position, int value) {
        //System.out.println(String.format("Wrote %d to position %d", value, position));
        program.set(position, value);
    }

    private List<Integer> parseOpCode(int value) {
        List<Integer> result = new ArrayList<>();
        var digits = intToDigits(value, 5);
        var ones = digits.remove(digits.size() - 1);
        var tens = digits.remove(digits.size() - 1);
        int opCode = ones + (10 * tens);
        result.add(opCode);
        while (!digits.isEmpty()) {
            result.add(digits.remove(digits.size() - 1));
        }
        return result;
    }

    private List<Integer> intToDigits(int i, int padToLength) {
        var s = String.format("%05d", i);
        List<String> chars = Arrays.asList(s.split(""));
        return new ArrayList<Integer>(chars.stream().map(Integer::valueOf).toList()); // Mutable
    }

    public int getProgramLength() {
        return program.size();
    }

    public void printState() {
        System.out.println(String.join(",", program.stream().map(String::valueOf).toList()));
    }
}
