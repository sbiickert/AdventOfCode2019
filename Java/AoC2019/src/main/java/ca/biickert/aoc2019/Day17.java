package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.IntCodeComputer;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sjb
 */
public class Day17 extends Solution {

    public Day17() {
	super(17, "Set and Forget", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var part1Solution = solvePartOne(input.get(0));
	var part2Solution = solvePartTwo(input.get(0));

	result = new Result(part1Solution, part2Solution);

	return result;
    }
    
    private Grid2D map;
    private Coord2D start;

    private String solvePartOne(String program) {
	makeMap(program);

	List<Coord2D> intersections = new ArrayList<>();
	var scaffolding = map.getCoordsWithValue("#");
	for (var c : scaffolding) {
	    boolean allNeighborsAreScaffold = true;
	    for (var n : map.getAdjacent(c)) {
		allNeighborsAreScaffold = allNeighborsAreScaffold && map.get(n).equals("#");
	    }
	    if (allNeighborsAreScaffold) {
		intersections.add(c);
	    }
	}
	
	int sum = 0;
	for (var c : intersections) {
	    sum += c.getX() * c.getY();
	}

	return String.valueOf(sum);
    }

    private String solvePartTwo(String program) {
	var route = findRoute();
	var routeStr = String.join(",", route);
	//System.out.println(routeStr);
	
	// Worked this out manually
	final String mainMovementRoutine = "B,A,B,A,C,A,B,C,A,C";
	Map<String,String> routines = new HashMap<>();
	routines.put("A", "L,6,L,4,L,12");
	routines.put("B", "L,12,L,8,R,10,R,10");
	routines.put("C", "R,10,L,8,L,4,R,10");
	
	var computer = new IntCodeComputer(program);
	computer.setImmediateValue(0, 2);
	computer.inputAscii(mainMovementRoutine);
	computer.inputAscii(routines.get("A"));
	computer.inputAscii(routines.get("B"));
	computer.inputAscii(routines.get("C"));
	computer.inputAscii("n");
	
	String line = "";
	while (true) {
	    computer.run(true, false);
	    long asciiVal = computer.output;
	    if (asciiVal > 255) {
		break;
	    }
	    //line += Character.toString((char)asciiVal);
	    //if (asciiVal == 10) {
		//System.out.print(line);
	    //}
	}
		
	return String.valueOf(computer.output);
    }

    private void makeMap(String program) {
	var computer = new IntCodeComputer(program);
	map = new Grid2D(".", AdjacencyRule.ROOK);
	
	int row = 0;
	int col = 0;
	while (computer.isHalted == false) {
	    computer.run(true, false);
	    long asciiVal = computer.output;
	    if (asciiVal == 10) {
		row++;
		col = 0;
	    }
	    else {
		String str = Character.toString((char)asciiVal);
		map.set(new Coord2D(col, row), str);
		col++;
	    }
	}
	start = map.getCoordsWithValue("^").get(0);
    }
    
    private List<String> findRoute() {
	var position = start;
	var directions = map.getOffsets(); // N, E, S, W
	int currentDir = 0; // N
	List<String> route = new ArrayList<>();
	int runCount = 1;
	
	while (true) {
	    // Look ahead
	    Coord2D n = position.add(directions.get(currentDir));
	    if (map.get(n).equals("#")) {
		position = n;
		runCount++;
		continue;
	    }
	    if (runCount > 1) {
		route.add(String.valueOf(runCount));
		runCount = 1;
	    }
	    // Look left
	    int leftDir = currentDir - 1;
	    if (leftDir < 0) { leftDir += 4; }
	    n = position.add(directions.get(leftDir));
	    if (map.get(n).equals("#")) {
		position = n;
		route.add("L");
		currentDir = leftDir;
		continue;
	    }
	    // Look right
	    int rightDir = currentDir + 1;
	    if (rightDir >= 4) { rightDir = 0; }
	    n = position.add(directions.get(rightDir));
	    if (map.get(n).equals("#")) {
		position = n;
		route.add("R");
		currentDir = rightDir;
		continue;
	    }
	    // Dead end, we can end
	    break;
	}
	return route;
    }
}
