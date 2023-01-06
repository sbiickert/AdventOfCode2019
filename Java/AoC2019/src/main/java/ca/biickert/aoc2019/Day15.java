package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.IntCodeComputer;
import ca.biickert.aoc2019.util.IntCodeComputerState;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day15 extends Solution {

    static final String UNKNOWN = "~";
    static final String EMPTY = " ";
    static final String WALL = "#";
    static final String OXYGEN = "O";
    static final String DROID = "D";
    static final String PATH = ".";

    public Day15() {
	super(15, "Oxygen System", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var droid = new RepairDroid(input.get(0));
	map = new Grid2D(Day15.UNKNOWN, AdjacencyRule.ROOK);
	var loc = new Coord2D(0, 0);
	map.set(loc, DROID);
	mapSpace(droid, loc);
	//map.print();

	Coord2D oxygenLocation = map.getCoordsWithValue(OXYGEN).get(0);

	var part1Solution = solvePartOne(loc, oxygenLocation);
	var part2Solution = solvePartTwo(oxygenLocation);

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private Grid2D map;

    private String solvePartOne(Coord2D droidLocation, Coord2D oxygenLocation) {
	// BFS
	int step = 0;
	List<Coord2D> toVisit = new ArrayList<>();
	toVisit.add(droidLocation);
	boolean bReachedOxygen = false;
	while (!bReachedOxygen) {
	    step++;
	    List<Coord2D> nextStep = new ArrayList();
	    for (var loc : toVisit) {
		for (var n : map.getAdjacent(loc)) {
		    if (map.get(n).equals(EMPTY)) {
			nextStep.add(n);
			map.set(n, PATH);
		    } else if (n.equals(oxygenLocation)) {
			bReachedOxygen = true;
		    }
		}
	    }
	    toVisit = nextStep;
	}

	return String.valueOf(step);
    }

    private String solvePartTwo(Coord2D oxygenLocation) {
	int step = 0;
	List<Coord2D> toVisit = new ArrayList<>();
	toVisit.add(oxygenLocation);
	while (toVisit.size() > 0) {
	    step++;
	    List<Coord2D> nextStep = new ArrayList<>();
	    for (var loc : toVisit) {
		for (var n : map.getAdjacent(loc)) {
		    if (map.get(n).equals(EMPTY) || map.get(n).equals(PATH)) {
			nextStep.add(n);
			map.set(n, step % 10);
		    }
		}
	    }
	    toVisit = nextStep;
	}
	
	//map.print();
	return String.valueOf(step-1); // While loop does one more iteration after oxygen reaches last space
   }

    private void mapSpace(RepairDroid droid, Coord2D location) {
	for (var dir : RepairDroid.allDirections) {
	    var n = getNeighbor(location, dir);
	    if (map.get(n).equals(UNKNOWN)) {
		var state = droid.saveState();
		var result = droid.move(dir);

		if (result == RepairDroid.HIT_WALL) {
		    map.set(n, WALL);
		} else {
		    if (result == RepairDroid.MOVED) {
			map.set(n, EMPTY);
			mapSpace(droid, n);
		    } else {
			map.set(n, OXYGEN);
			mapSpace(droid, n);
		    }
		    droid.restoreState(state);
		}
	    }
	}
    }

    private Coord2D getNeighbor(Coord2D loc, long direction) {
	if (direction == RepairDroid.NORTH) {
	    return loc.add(new Coord2D(0, -1));
	}
	if (direction == RepairDroid.SOUTH) {
	    return loc.add(new Coord2D(0, 1));
	}
	if (direction == RepairDroid.WEST) {
	    return loc.add(new Coord2D(-1, 0));
	}
	if (direction == RepairDroid.EAST) {
	    return loc.add(new Coord2D(1, 0));
	}
	System.out.println("Unknown direction " + direction);
	return loc;
    }
}

class RepairDroid {

    static final long NORTH = 1L;
    static final long SOUTH = 2L;
    static final long WEST = 3L;
    static final long EAST = 4L;
    static final long[] allDirections = new long[]{RepairDroid.NORTH, RepairDroid.SOUTH, RepairDroid.WEST, RepairDroid.EAST};

    static final long HIT_WALL = 0L;
    static final long MOVED = 1L;
    static final long MOVED_AND_FOUND_OXYGEN_SYSTEM = 2L;

    private IntCodeComputer computer;
    private String programDefn;

    public RepairDroid(String programDefn) {
	this.programDefn = programDefn;
	this.computer = new IntCodeComputer(programDefn);
    }

    public long move(long direction) {
	computer.input.add(direction);
	computer.run(true, false);
	return computer.output;
    }

    public IntCodeComputerState saveState() {
	return computer.saveState();
    }

    public void restoreState(IntCodeComputerState state) {
	computer.restoreState(state);
    }
}
