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
	    boolean hasAdjacentPair = false;
	    boolean doesNeverDecrease = true;
	    for (int j = 1; j < digits.size(); j++) {
		if (digits.get(j-1).equals(digits.get(j))) {
		    hasAdjacentPair = true;
		}
		if (digits.get(j-1).intValue() > digits.get(j)) {
		    doesNeverDecrease = false;
		    break;
		}
	    }
	    if (hasAdjacentPair && doesNeverDecrease) {
		validCount++;
	    }
	}
	
	return validCount;
    }

    private int solvePartTwo() {
	return 0;
    }
}
