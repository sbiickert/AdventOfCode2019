package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sjb
 */
public class Day10 extends Solution {
    private Coord2D monitoringStationLocation;

    public Day10() {
	super(10, "Monitoring Station", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var part1Solution = solvePartOne(input);
	var part2Solution = solvePartTwo(input);

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(List<String> input) {
	var grid = parseGrid(input);
	grid.print();

	int maxVisible = 0;
	Coord2D best = new Coord2D(-1,-1);
	List<Coord2D> asteroids = grid.getCoords();

	for (var location : asteroids) {
	    var lineOfSightCoords = this.getLineOfSightCoords(location, asteroids);
	    if (lineOfSightCoords.size() > maxVisible) {
		maxVisible = lineOfSightCoords.size();
		best = location;
	    }
	}
	System.out.println("Best location is at " + best);
	monitoringStationLocation = best;

	return String.valueOf(maxVisible);
    }

    private String solvePartTwo(List<String> input) {
	var grid = parseGrid(input);
	return "";
    }
    
    private Collection<Coord2D> getLineOfSightCoords(Coord2D origin, List<Coord2D> coords) {
	Map<Double, Coord2D> los = new HashMap<>();
	
	for (var c : coords) {
	    if (origin.equals(c)) { continue; }
	    var delta = origin.delta(c);
	    double angle = Math.atan2(delta.getY(), delta.getX());
	    if (los.containsKey(angle)) {
		var dist = origin.manhattanDistanceTo(los.get(angle));
		if (origin.manhattanDistanceTo(c) < dist) {
		    los.replace(angle, c);
		}
	    }
	    else {
		los.put(angle, c);
	    }
	}
	
	return los.values();
    }

    private Grid2D parseGrid(List<String> input) {
	Grid2D g = new Grid2D(".", AdjacencyRule.QUEEN);
	for (int r = 0; r < input.size(); r++) {
	    var chars = input.get(r).split("");
	    for (int c = 0; c < chars.length; c++) {
		if (chars[c].equals("#")) {
		    g.set(new Coord2D(c, r), chars[c]);
		}
	    }
	}
	return g;
    }
}
