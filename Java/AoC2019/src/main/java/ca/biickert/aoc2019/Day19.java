package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.IntCodeComputer;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day19 extends Solution {

    public Day19() {
	super(19, "Tractor Beam", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var part1Solution = solvePartOne(input.get(0));
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(String program) {
	var computer = new IntCodeComputer(program);
	var monitor = new Grid2D(".", AdjacencyRule.QUEEN);
	
	for (int x = 0; x < 50; x++) {
	    for (int y = 0; y < 50; y++) {
		var state = computer.saveState();
		computer.input.add((long)x);
		computer.input.add((long)y);
		computer.run(true, false); // Not resetting the pointer
		var output = computer.output;
		monitor.set(new Coord2D(x, y), output == 1L ? "#" : ".");
		computer.restoreState(state);
	    }
	}
	
	var coords = monitor.getCoordsWithValue("#");
	
	monitor.print();
	return String.valueOf(coords.size());
    }

    private String solvePartTwo() {
	return "";
    }

}
