package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day02 extends Solution {

    public Day02() {
	super(2, "1202 Program Alarm", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var part1Solution = solvePartOne(input);
	var part2Solution = solvePartTwo(input);

	result = new Result(Integer.toString(part1Solution), Integer.toString(part2Solution));

	return result;
    }

    private int solvePartOne(List<String> input) {
	var computer = new IntCodeComputer2();
	computer.load(input.get(0));
	if (computer.getProgramLength() > 20) {
	    computer.setValue(1, 12);
	    computer.setValue(2, 2);
	}
	computer.execute();
	var result = computer.getValue(0);
	return result;
    }

    private int solvePartTwo(List<String> input) {
	var computer = new IntCodeComputer2();
	computer.load(input.get(0));
	for (int noun = 0; noun < 100; noun++) {
	    for (int verb = 0; verb < 100; verb++) {
		computer.setValue(1, noun);
		computer.setValue(2, verb);
		computer.execute();
		if (computer.getValue(0) == 19690720) {
		    return 100 * noun + verb;
		}
		computer.load(input.get(0));
	    }
	}
	return -1;
    }
}

class IntCodeComputer2 {

    static final int OPCODE_ADD = 1;
    static final int OPCODE_MULTIPLY = 2;
    static final int EXIT = 99;

    private List<Integer> program;
    private int ptr;

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
	boolean bExit = false;
	int inputPos1;
	int inputPos2;
	int outputPos;

	while (!bExit) {
	    switch (getValue(ptr)) {
		case OPCODE_ADD:
		    inputPos1 = getValue(ptr + 1);
		    inputPos2 = getValue(ptr + 2);
		    outputPos = getValue(ptr + 3);
		    program.set(outputPos, getValue(inputPos1) + getValue(inputPos2));
		    ptr += 4;
		    break;
		case OPCODE_MULTIPLY:
		    inputPos1 = getValue(ptr + 1);
		    inputPos2 = getValue(ptr + 2);
		    outputPos = getValue(ptr + 3);
		    program.set(outputPos, getValue(inputPos1) * getValue(inputPos2));
		    ptr += 4;
		    break;
		case EXIT:
		    bExit = true;
		    break;
		default:
		    System.out.println(String.format("Unexpected opcode %d", getValue(ptr)));
		    assert (false);
	    }
	}
    }

    public int getValue(int position) {
	return program.get(position);
    }

    public void setValue(int position, int value) {
	program.set(position, value);
    }

    public int getProgramLength() {
	return program.size();
    }

    public void printState() {
	System.out.println(String.join(",", program.stream().map(String::valueOf).toList()));
    }
}
