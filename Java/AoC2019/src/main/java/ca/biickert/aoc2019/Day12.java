package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.Coord3D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author sjb
 */
public class Day12 extends Solution {

    public Day12() {
	super(12, "The N-Body Problem", true);
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
	var moons = parseMoons(input);
	for (int i = 1; i <= 1000; i++) {
	    simulateStep(moons);
	}

	int totalEnergy = 0;
	for (var moon : moons) {
	    totalEnergy += moon.getKineticEnergy() * moon.getPotentialEnergy();
	}

	return String.valueOf(totalEnergy);
    }

    private String solvePartTwo(List<String> input) {
	var repeat = calculateToRepeat(input);

	return String.valueOf(repeat);
    }

    private long calculateToRepeat(List<String> input) {
	var moons = parseMoons(input);
	var cycles = analyzeCycles(moons);
	System.out.println(cycles);
	Set<Integer> allCycles = new HashSet<>();
	for (var moon : cycles) {
	    for (var cycle : moon) {
		allCycles.add(cycle);
	    }
	}
	List<Integer> repeatCycles = allCycles.stream().sorted(Comparator.reverseOrder()).toList();
	System.out.println(repeatCycles);
	
	int maxSync = 0;
	int jump = repeatCycles.get(maxSync);
	long ts = jump;
	long temp = ts; // To avoid "effectively final" lambda capture error
	List<Long> remainders = repeatCycles.stream().map(rc -> temp % rc).toList();
	
	while (true) {
	    boolean remaindersAreZero = true;
	    for (int r = 0; r <= maxSync + 1; r++) {
		remaindersAreZero = remaindersAreZero && remainders.get(r) == 0;
	    }
	    if (remaindersAreZero) {
		maxSync++;
		if (remainders.stream().distinct().count() == 1) {
		    // ALL remainders zero.
		    System.out.println("found future state at time " + ts);
		    break;
		}
		if (maxSync < repeatCycles.size() - 2) {
		    // Don't change the jump for the last remainder digit
		    jump *= repeatCycles.get(maxSync);
		    System.out.println("Jump changed to " + jump);
		}
	    }
	    
	    ts += jump;
	    long temp1 = ts; // To avoid "effectively final" lambda capture error
	    remainders = repeatCycles.stream().map(rc -> temp1 % rc).toList();
	}
	return ts;
    }

    private List<List<Integer>> analyzeCycles(List<Moon> moons) {
	List<List<Integer>> cycles = new ArrayList<>();
	List<List<List<Integer>>> tracking = new ArrayList<>();
	for (Moon moon : moons) {
	    cycles.add(Arrays.asList(new Integer[]{0, 0, 0, 0, 0, 0}));
	    List<List<Integer>> track = new ArrayList();
	    for (int i = 0; i < 6; i++) {
		track.add(new ArrayList<>());
	    }
	    tracking.add(track);
	}

	final int analyzeAfter = 10000;
	boolean allCyclesMeasured = false;
	int cycle = 0;

	while (allCyclesMeasured == false) {
	    for (int m = 0; m < moons.size(); m++) {
		var moon = moons.get(m);
		for (int axis = 0; axis < 6; axis++) {
		    tracking.get(m).get(axis).add(moon.getAxis(axis));
		}
	    }
	    cycle++;
	    if (cycle % analyzeAfter == 0) {
		// Analyze for cycling. cycle must be even.
		for (int m = 0; m < moons.size(); m++) {
		    for (int axis = 0; axis < 6; axis++) {
			if (cycles.get(m).get(axis) == 0) {
			    // Returns 0 if not found
			    var c = findCycle(tracking.get(m).get(axis), cycle - analyzeAfter + 2);
			    if (c > 0) {
				cycles.get(m).set(axis, c);
			    }
			}
		    }
		}
		allCyclesMeasured = true;
		for (int m = 0; m < moons.size(); m++) {
		    if (cycles.get(m).contains(0)) {
			allCyclesMeasured = false;
			break;
		    }
		}
	    }
	    simulateStep(moons);
	}
	return cycles;
    }

    private int findCycle(List<Integer> track, int start) {
	for (int i = start; i < track.size(); i += 2) {
	    int half = i / 2;
	    List<Integer> front = track.subList(0, half);
	    List<Integer> back = track.subList(half, half + front.size());
	    if (front.equals(back)) {
		return half;
	    }
	}
	return 0;
    }

    private long simulateToRepeat(List<String> input) {
	// Too slow for anything except the first test case
	var unchanged = parseMoons(input);
	var moons = parseMoons(input);

	long steps = 0;
	boolean finished = false;
	while (!finished) {
	    simulateStep(moons);
	    steps++;
	    finished = true;
	    for (var m = 0; m < moons.size(); m++) {
		finished = finished && moons.get(m).equals(unchanged.get(m));
	    }
	    if (steps % 100000 == 0) {
		System.out.println(steps);
	    }
	}
	return steps;
    }

    private void simulateStep(List<Moon> moons) {
	for (int a = 0; a < moons.size() - 1; a++) {
	    for (int b = a + 1; b < moons.size(); b++) {
		moons.get(a).interactWith(moons.get(b));
	    }
	}

	for (var moon : moons) {
	    moon.applyVelocity();
	}
    }

    private List<Moon> parseMoons(List<String> input) {
	List<Moon> moons = new ArrayList<>();
	for (var line : input) {
	    moons.add(new Moon(line));
	}
	return moons;
    }
}

class Moon {

    static final Pattern REGEX = Pattern.compile("x=([\\-\\d]+), y=([\\-\\d]+), z=([\\-\\d]+)");
    Coord3D pos;
    Coord3D vel;

    public Moon(String defn) {
	vel = new Coord3D(0, 0, 0);
	var matcher = REGEX.matcher(defn);
	if (matcher.find()) {
	    int x = Integer.parseInt(matcher.group(1));
	    int y = Integer.parseInt(matcher.group(2));
	    int z = Integer.parseInt(matcher.group(3));
	    pos = new Coord3D(x, y, z);
	} else {
	    System.out.println("regex didn't work on " + defn);
	}
    }

    public void interactWith(Moon other) {
	int dx = compare(pos.getX(), other.pos.getX());
	int dy = compare(pos.getY(), other.pos.getY());
	int dz = compare(pos.getZ(), other.pos.getZ());
	vel.setX(vel.getX() + dx);
	vel.setY(vel.getY() + dy);
	vel.setZ(vel.getZ() + dz);
	other.vel.setX(other.vel.getX() - dx);
	other.vel.setY(other.vel.getY() - dy);
	other.vel.setZ(other.vel.getZ() - dz);
    }

    public void applyVelocity() {
	pos = pos.add(vel);
    }

    public int getKineticEnergy() {
	return vel.manhattanDistanceTo(new Coord3D(0, 0, 0));
    }

    public int getPotentialEnergy() {
	return pos.manhattanDistanceTo(new Coord3D(0, 0, 0));
    }

    public int getAxis(int a) {
	switch (a) {
	    case 0:
		return pos.getX();
	    case 1:
		return pos.getY();
	    case 2:
		return pos.getZ();
	    case 3:
		return vel.getX();
	    case 4:
		return vel.getY();
	    case 5:
		return vel.getZ();
	}
	return 0;
    }

    private int compare(int a, int b) {
	if (a < b) {
	    return 1;
	}
	if (a == b) {
	    return 0;
	}
	return -1; // if (b < a)
    }

    @Override
    public String toString() {
	return "pos: " + pos + " vel: " + vel;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || this.getClass() != o.getClass()) {
	    return false;
	}
	Moon that = (Moon) o;

	return this.pos.equals(that.pos) && this.vel.equals(that.vel);
    }
}
