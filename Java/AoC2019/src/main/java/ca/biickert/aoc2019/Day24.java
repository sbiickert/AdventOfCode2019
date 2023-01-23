package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author sjb
 */
public class Day24 extends Solution {

    private Grid2D initialMap;

    public Day24() {
	super(24, "Planet of Discord", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	initialMap = parseMap(input);
	Grid2D map = null;
	try {
	    map = (Grid2D) initialMap.clone();
	} catch (CloneNotSupportedException e) {
	    System.exit(1);
	}
	
	boolean test = initialMap.getCoords().equals(map.getCoords());

	var part1Solution = solvePartOne(map);
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(Grid2D map) {
	int i = 0;
	Set<List<Coord2D>> history = new HashSet<>();
	var ext = map.getExtent();
	var coords = ext.getAllCoords();
	
	while (!history.contains(map.getCoords())) {
	    history.add(map.getCoords())
;
	    var nextMap = new Grid2D(map.getDefaultValue(), map.getAdjacencyRule());
	    
	    for (var coord : coords) {
		boolean isBug = map.get(coord).equals("#");
		var adjacentWithBugs = map.getAdjacentWithValue(coord, "#");
		if (isBug && adjacentWithBugs.size() == 1) {
		    // lives
		    nextMap.set(coord, "#");
		} else if (isBug) {
		    // dies
		} else if (!isBug && (adjacentWithBugs.size() == 1 || adjacentWithBugs.size() == 2)) {
		    // infests
		    nextMap.set(coord, "#");
		}
	    }

	    map = nextMap;
	    i++;
	    System.out.println(i);
	}

	long biodiversity = 0;
	for (var coord : map.getCoordsWithValue("#")) {
	    int power = coord.getCol() + (coord.getRow() * ext.getWidth());
	    biodiversity += Math.pow(2, power);
	}
	
	map.print();
	return String.valueOf(biodiversity);
    }

    private String solvePartTwo() {
	return "";
    }

    private Grid2D parseMap(List<String> input) {
	var map = new Grid2D(".", AdjacencyRule.ROOK);

	for (int r = 0; r < input.size(); r++) {
	    var line = input.get(r);
	    var chars = line.split("");
	    for (int c = 0; c < chars.length; c++) {
		if (chars[c].equals("#")) {
		    map.set(new Coord2D(c, r), chars[c]);
		}
	    }
	}

	return map;
    }
}
