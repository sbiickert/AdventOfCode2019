package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sjb
 */
public class Day03 extends Solution {

    public Day03() {
	super(3, "Crossed Wires", true);
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
	var wire1 = new Wire(input.get(0));
	var wire2 = new Wire(input.get(1));

	Set<Coord2D> common = new HashSet<>(wire1.getAllCoords());
	Set<Coord2D> s2 = new HashSet<>(wire2.getAllCoords());
	common.retainAll(s2);

	int lowestManhattan = Integer.MAX_VALUE;
	Coord2D closest;
	Coord2D origin = new Coord2D(0, 0);

	for (var coord : common) {
	    int md = origin.manhattanDistanceTo(coord);
	    if (md < lowestManhattan) {
		closest = coord;
		lowestManhattan = md;
	    }
	}

	return lowestManhattan;
    }

    private int solvePartTwo(List<String> input) {
	return -1;
    }
}

class Wire {

    private static final Map<String, Coord2D> offsets;

    static {
	offsets = new HashMap<>();
	offsets.put("U", new Coord2D(0, -1));
	offsets.put("D", new Coord2D(0, 1));
	offsets.put("L", new Coord2D(-1, 0));
	offsets.put("R", new Coord2D(1, 0));
    }
    private List<Coord2D> coords = new ArrayList<>();

    public Wire(String defn) {
	Coord2D c = new Coord2D(0, 0);
	String[] runs = defn.split(",");
	for (var run : runs) {
	    String dir = run.substring(0, 1);
	    int dist = Integer.valueOf(run.substring(1));
	    for (int step = 1; step <= dist; step++) {
		c = c.add(offsets.get(dir));
		coords.add(c);
	    }
	}
    }

    public List<Coord2D> getAllCoords() {
	return new ArrayList<Coord2D>(coords);
    }
}
