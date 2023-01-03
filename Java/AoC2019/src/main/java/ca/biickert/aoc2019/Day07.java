package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.Algorithms;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        var part2Solution = solvePartTwo();

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
        List<Integer> phases = new ArrayList<>();
        phases.add(0);
        phases.add(1);
        phases.add(2);
        phases.add(3);
        phases.add(4);
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
                amp.execute();
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

    private int solvePartTwo() {
        return 0;
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

    public void execute() {
        //System.out.println("Starting execute");
        boolean bExit = false;
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
                    //System.out.println("Read from input: " + i);
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
                    //System.out.println("Wrote to output: " + output);
                    ptr += 2;
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
        var digits = intToDigits(value);
        if (digits.size() == 1) {
            // Just a single-digit opcode, no parameter mode info
            result.add(digits.get(0));
            for (int i = 1; i <= 3; i++) {
                result.add(0);
            }
        } else {
            var ones = digits.get(digits.size() - 1);
            var tens = digits.get(digits.size() - 2);
            int opCode = ones + (10 * tens);
            result.add(opCode);
            for (int i = 3; i < 6; i++) {
                result.add((digits.size() - i >= 0) ? digits.get(digits.size() - i) : 0);
            }
        }
        return result;
    }

    public static List<Integer> intToDigits(int i) {
        return intToDigits(i, 0);
    }

    public static List<Integer> intToDigits(int i, int padToLength) {
        var s = String.format("%05d", i);
        List<String> chars = Arrays.asList(s.split(""));
        return chars.stream().map(Integer::valueOf).toList();
    }

    public int getProgramLength() {
        return program.size();
    }

    public void printState() {
        System.out.println(String.join(",", program.stream().map(String::valueOf).toList()));
    }
}
