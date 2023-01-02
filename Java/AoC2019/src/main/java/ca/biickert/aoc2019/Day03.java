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

	var wire1 = new Wire(input.get(0));
	var wire2 = new Wire(input.get(1));

	var part1Solution = solvePartOne(wire1, wire2);
	var part2Solution = solvePartTwo(wire1, wire2);

	result = new Result(Integer.toString(part1Solution), Integer.toString(part2Solution));

	return result;
    }

    private int solvePartOne(Wire wire1, Wire wire2) {
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

    private int solvePartTwo(Wire wire1, Wire wire2) {
	Set<Coord2D> common = new HashSet<>(wire1.getAllCoords());
	Set<Coord2D> s2 = new HashSet<>(wire2.getAllCoords());
	common.retainAll(s2);

	int lowestCombined = Integer.MAX_VALUE;
	Coord2D closest;

	for (var coord : common) {
	    int dist1 = wire1.getFirstIndexOf(coord);
	    int dist2 = wire2.getFirstIndexOf(coord);
	    if (dist1 + dist2 < lowestCombined) {
		closest = coord;
		lowestCombined = dist1 + dist2;
	    }
	}

	return lowestCombined + 2; // indexes start at 0, count starts at 1 * 2 wires
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

    public int getFirstIndexOf(Coord2D coord) {
	return coords.indexOf(coord);
    }
}
