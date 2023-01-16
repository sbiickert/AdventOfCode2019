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

    IntCodeComputer computer;

    public Day19() {
	super(19, "Tractor Beam", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	computer = new IntCodeComputer(input.get(0));

	var part1Solution = solvePartOne();
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne() {
	var monitor = new Grid2D(".", AdjacencyRule.QUEEN);

	for (int x = 0; x < 50; x++) {
	    for (int y = 0; y < 50; y++) {
		if (isInTractorBeam(x, y)) {
		    monitor.set(new Coord2D(x, y), "#");
		}
	    }
	}

	var coords = monitor.getCoordsWithValue("#");

	//monitor.print();
	return String.valueOf(coords.size());
    }

    private String solvePartTwo() {
	// 0,0 is at the ship.
	// Worked out the beam bounds at y = 100 to find slopes of the sides
	// But then realized it would be easier just to brute-force
	// Start at y = 1000
	int x = 0;
	int y = 1000;
	boolean bFoundBeam = false;
	int minX = 0;
	var monitor = new Grid2D(".", AdjacencyRule.ROOK);
	int result = -1;

	while (true) {
	    boolean inBeam = isInTractorBeam(x, y);
	    if (inBeam) {
		monitor.set(new Coord2D(x, y), "#");
	    }
	    if (!bFoundBeam && inBeam) {
		minX = x;
		var c = new Coord2D(x + 99, y - 99);
		if (monitor.get(c).equals("#")) {
		    // We have found a 100x100 space.
		    result = 10000 * x + (y - 99);
		    break;
		}
	    }
	    bFoundBeam = bFoundBeam || inBeam;
	    if (bFoundBeam && !inBeam) {
		bFoundBeam = false;
		if (y % 100 == 0) {
		    System.out.println(y);
		}
		y++;
		x = minX;
	    } else {
		x++;
	    }
	}

	//monitor.print();

	return String.valueOf(result);
    }

    private boolean isInTractorBeam(int x, int y) {
	var state = computer.saveState();
	computer.input.add((long) x);
	computer.input.add((long) y);
	computer.run(true, false); // Not resetting the pointer
	var output = computer.output;
	computer.restoreState(state);
	return output == 1L;
    }
}
