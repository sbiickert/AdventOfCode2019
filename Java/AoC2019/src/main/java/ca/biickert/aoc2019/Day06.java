package ca.biickert.aoc2019;

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
public class Day06 extends Solution {

    public Day06() {
	super(6, "Universal Orbit Map", true);
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
	var map = buildMap(input);
	var result = 0;

	for (String key : map.keySet()) {
	    var body = map.get(key);
	    int orbits = 0;
	    while (body.inOrbitAround != null) {
		orbits++;
		body = body.inOrbitAround;
	    }
	    //System.out.println(String.format("body %s has orbit count %d", key, orbits));
	    result += orbits;
	}
	return result;
    }

    private int solvePartTwo(List<String> input) {
	var map = buildMap(input);
	Set<String> myOrbits = new HashSet<>();
	var body = map.get("YOU");
	while (body != null) {
	    myOrbits.add(body.name);
	    body = body.inOrbitAround;
	}

	Body common = null;
	int transferCount = -1; // Will count SAN to body, otherwise
	body = map.get("SAN");
	while (body != null) {
	    if (myOrbits.contains(body.name)) {
		common = body;
		break;
	    }
	    body = body.inOrbitAround;
	    transferCount++;
	}

	body = map.get("YOU");
	transferCount--;  // Will count YOU to body, otherwise
	while (body != null) {
	    if (body.name.equals(common.name)) {
		break;
	    }
	    body = body.inOrbitAround;
	    transferCount++;
	}

	return transferCount;
    }

    private Map<String, Body> buildMap(List<String> input) {
	Map<String, Body> bodies = new HashMap<>();

	for (var line : input) {
	    // THE BODY BEING ORBITED ) THE BODY IN ORBIT
	    var names = line.split("\\)");
	    for (int i = 0; i < 2; i++) {
		if (bodies.containsKey(names[i]) == false) {
		    bodies.put(names[i], new Body(names[i]));
		}
	    }
	    bodies.get(names[1]).inOrbitAround = bodies.get(names[0]);
	    bodies.get(names[0]).orbitingBodies.add(bodies.get(names[1]));
	}

	return bodies;
    }
}

class Body {

    String name;
    Body inOrbitAround = null;
    List<Body> orbitingBodies = new ArrayList<>();

    public Body(String name) {
	this.name = name;
    }
}
