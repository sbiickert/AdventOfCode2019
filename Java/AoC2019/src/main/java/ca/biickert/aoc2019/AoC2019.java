package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.Input;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;

/**
 *
 * @author sjb
 */
public class AoC2019 {

    public static void main(String[] args) {
	System.out.println("Advent of Code 2019");
	System.out.println("Working Directory = " + System.getProperty("user.dir"));

	Input input;
	Result result;
	
	var day = new Day18();
	
//	input = Solution.tests(day).get(2);
	input = Solution.challenge(day);
	
	result = day.solve(input.filename(), input.index());
	//result = day.solve("", 0);
	
	System.out.println(result);
    }
}
