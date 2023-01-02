package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day04 extends Solution {

    static final int INPUT_MIN = 359282;
    static final int INPUT_MAX = 820401;

    public Day04() {
	super(4, "Secure Container", false);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	var part1Solution = solvePartOne();
	var part2Solution = solvePartTwo();

	result = new Result(Integer.toString(part1Solution), Integer.toString(part2Solution));

	return result;
    }

    private int solvePartOne() {
	int validCount = 0;

	for (int i = INPUT_MIN; i <= INPUT_MAX; i++) {
	    List<String> chars = Arrays.asList(String.valueOf(i).split(""));
	    List<Integer> digits = chars.stream().map(Integer::valueOf).toList();
	    if (hasAdjacent(digits) && doesNeverDecrease(digits)) {
		validCount++;
	    }
	}

	return validCount;
    }

    private int solvePartTwo() {
	int validCount = 0;

	for (int i = INPUT_MIN; i <= INPUT_MAX; i++) {
	    List<String> chars = Arrays.asList(String.valueOf(i).split(""));
	    List<Integer> digits = chars.stream().map(Integer::valueOf).toList();
	    if (hasAdjacentPair(digits) && doesNeverDecrease(digits)) {
		validCount++;
	    }
	}

	return validCount;
    }

    private boolean hasAdjacent(List<Integer> digits) {
	for (int j = 1; j < digits.size(); j++) {
	    if (digits.get(j - 1).equals(digits.get(j))) {
		return true;
	    }
	}
	return false;
    }

    private boolean hasAdjacentPair(List<Integer> digits) {
	for (int i = -1; i <= digits.size()-3; i++) {
	    int a = (i < 0) ? -1 : digits.get(i);
	    int b = digits.get(i+1);
	    int c = digits.get(i+2);
	    int d = (i + 4 > digits.size()) ? -1 : digits.get(i+3);
	    if (a != b && b == c && c != d) {
		return true;
	    }
	}
	return false;
    }

    private boolean doesNeverDecrease(List<Integer> digits) {
	for (int j = 1; j < digits.size(); j++) {
	    if (digits.get(j - 1) > digits.get(j)) {
		return false;
	    }
	}
	return true;
    }
}
