/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.biickert.aoc2019.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sjb
 */
public abstract class Solution {
    static final String RELATIVE_INPUT_PATH = "../../Input";
    
    public static List<Input> inputsForSolution(Solution s) {
	List<Input> result = new ArrayList<>();
	
	// Challenge input N=1
	result.add(new Input(s, filename(s.day, false), 0));
	
	// Test input. N = 0..N
	if (s.emptyLinesIndicateMultipleInputs) {
	    var testGroups = InputReader.readGroupedInputFile(filename(s.day, true));
	    for (int i = 0; i < testGroups.size(); i++) {
		result.add(new Input(s, filename(s.day, true), i));
	    }
	}
	else {
	    result.add(new Input(s, filename(s.day, true), 0));
	}

	return result;
    }
    
    public static String filename(int day, boolean isTest) {
	return String.format("%s/day%02d_", Solution.RELATIVE_INPUT_PATH, day) + (isTest ? "test" : "challenge") + ".txt";
    }
    
    int day = 0;
    String name = "";
    boolean emptyLinesIndicateMultipleInputs = true;
    
    public Solution(int day, String name) {
	this(day, name, false);
    }
    
    public Solution(int day, String name, boolean emptyLinesIndicateMultipleInputs) {
	this.day = day;
	this.name = name;
	this.emptyLinesIndicateMultipleInputs = emptyLinesIndicateMultipleInputs;
    }
    
    public Result solve(String filename, int index) {
	System.out.println(String.format("Day %02d, %s. input: %s [%d]", day, name, filename, index));
	return new Result("", "");
    }
}

