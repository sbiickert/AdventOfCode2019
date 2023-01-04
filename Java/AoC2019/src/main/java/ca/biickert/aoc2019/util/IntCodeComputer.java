/*
This is the IntCodeComputer that was "finished" on Day 9.
*/
package ca.biickert.aoc2019.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

final public class IntCodeComputer {

    static final int OPCODE_ADD = 1;
    static final int OPCODE_MULTIPLY = 2;
    static final int OPCODE_INPUT = 3;
    static final int OPCODE_OUTPUT = 4;
    static final int OPCODE_JUMP_IF_TRUE = 5;
    static final int OPCODE_JUMP_IF_FALSE = 6;
    static final int OPCODE_LESS_THAN = 7;
    static final int OPCODE_EQUALS = 8;
    static final int OPCODE_RELATIVE_BASE = 9;
    static final int EXIT = 99;

    private List<Long> program;
    private int ptr;
    private int relativeBase;

    public Queue<Long> input = new LinkedList<>();
    public Long output;
    public boolean isHalted = false;

    public IntCodeComputer(String programDefn) {
	this.load(programDefn);
    }

    public void load(String programDefn) {
	var list = Arrays.asList(programDefn.split(","));
	List<Long> pgm = list.stream().map(Long::valueOf).toList();
	load(pgm);
    }

    private void load(List<Long> program) {
	this.program = new ArrayList(program);
	this.ptr = 0;
	this.relativeBase = 0;
    }

    public void run(boolean toOutput, boolean resetPtr) {
	//System.out.println("Starting execute");
	boolean bExit = false;
	if (resetPtr) {
	    ptr = 0;
	    relativeBase = 0;
	}
	long input1;
	long input2;
	int outputPos;

	while (!bExit) {
	    var opCode = parseOpCode((int) getImmediateValue(ptr));

	    switch (opCode.get(0)) {
		case OPCODE_ADD:
		    input1 = getValue(ptr + 1, opCode.get(1));
		    input2 = getValue(ptr + 2, opCode.get(2));
		    outputPos = (int) getImmediateValue(ptr + 3);
		    setValue(outputPos, input1 + input2, opCode.get(3));
		    ptr += 4;
		    break;
		case OPCODE_MULTIPLY:
		    input1 = getValue(ptr + 1, opCode.get(1));
		    input2 = getValue(ptr + 2, opCode.get(2));
		    outputPos = (int) getImmediateValue(ptr + 3);
		    setValue(outputPos, input1 * input2, opCode.get(3));
		    ptr += 4;
		    break;
		case OPCODE_INPUT:
		    outputPos = (int) getImmediateValue(ptr + 1);
		    Long i = input.poll();
		    try {
			setValue(outputPos, i, opCode.get(1));
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
			ptr = (int) getValue(ptr + 2, opCode.get(2));
		    } else {
			ptr += 3;
		    }
		    break;
		case OPCODE_JUMP_IF_FALSE:
		    input1 = getValue(ptr + 1, opCode.get(1));
		    if (input1 == 0) {
			ptr = (int) getValue(ptr + 2, opCode.get(2));
		    } else {
			ptr += 3;
		    }
		    break;
		case OPCODE_LESS_THAN:
		    input1 = getValue(ptr + 1, opCode.get(1));
		    input2 = getValue(ptr + 2, opCode.get(2));
		    outputPos = (int) getImmediateValue(ptr + 3);
		    setValue(outputPos, (input1 < input2) ? 1 : 0, opCode.get(3));
		    ptr += 4;
		    break;
		case OPCODE_EQUALS:
		    input1 = getValue(ptr + 1, opCode.get(1));
		    input2 = getValue(ptr + 2, opCode.get(2));
		    outputPos = (int) getImmediateValue(ptr + 3);
		    setValue(outputPos, (input1 == input2) ? 1 : 0, opCode.get(3));
		    ptr += 4;
		    break;
		case OPCODE_RELATIVE_BASE:
		    input1 = getValue(ptr + 1, opCode.get(1));
		    relativeBase += input1;
		    ptr += 2;
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

    public long getValue(int position, int flag) {
	try {
	    if (flag == 1) {
		return getImmediateValue(position);
	    } else if (flag == 2) {
		return getRelativeValue(position);
	    }
	    return getPositionValue(position);
	} catch (NullPointerException npe) {
	    System.out.println("Null when getting value. Position: " + position + ", is immediate: " + flag);
	    System.exit(1);
	}
	return -9999;
    }

    public long getPositionValue(int position) {
	return getImmediateValue(program.get(position).intValue());
    }

    public long getRelativeValue(int position) {
	return getImmediateValue(program.get(position).intValue() + relativeBase);
    }

    public long getImmediateValue(int position) {
	//System.out.println(String.format("Read %d from position %d", program.get(position), position));
	try {
	    return program.get(position);
	} catch (IndexOutOfBoundsException e) {
	    return 0;
	}
    }

    public void setValue(int position, long value, int flag) {
	if (flag == 2) {
	    setRelativeValue(position, value);
	} else {
	    setImmediateValue(position, value);
	}
    }

    public void setRelativeValue(int position, long value) {
	setImmediateValue(position + relativeBase, value);
    }

    private void setImmediateValue(int position, long value) {
	//System.out.println(String.format("Wrote %d to position %d", value, position));
	while (position >= program.size()) {
	    program.add(0L);
	}
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
	var s = String.format("%0" + padToLength + "d", i);
	List<String> chars = Arrays.asList(s.split(""));
	return new ArrayList<>(chars.stream().map(Integer::valueOf).toList()); // Mutable
    }

    public int getProgramLength() {
	return program.size();
    }

    public void printState() {
	System.out.println(String.join(",", program.stream().map(String::valueOf).toList()));
    }
}
