package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author sjb
 */
public class Day18 extends Solution {

    public Day18() {
	super(18, "Many-Worlds Interpretation", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	NeptuneVault vault;// = new NeptuneVault(input, false);
	//vault.print();

	var part1Solution = "";//solvePartOne(vault);

	if (input.size() > 50) {
	    // Challenge input. Need to replace some characters
	    input.set(39, input.get(39).substring(0, 39) + "@#@" + input.get(39).substring(42));
	    input.set(40, input.get(40).substring(0, 39) + "###" + input.get(40).substring(42));
	    input.set(41, input.get(41).substring(0, 39) + "@#@" + input.get(41).substring(42));
	}
	vault = new NeptuneVault(input, true);
	vault.print();

	var part2Solution = solvePartTwo(vault);

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(NeptuneVault vault) {
	Key start = vault.getKeys().get("@");

	List<String> possessedKeys = new ArrayList<>();
	possessedKeys.add("@");

	_shortestPathLength = Key.BIG;
	_memo = new HashMap<>();
	int shortest = findShortestPathLength(vault, start, possessedKeys, 0);

	return String.valueOf(shortest);
    }

    private int _shortestPathLength = Key.BIG;
    private Map<String, Integer> _memo;

    private int findShortestPathLength(NeptuneVault v, Key keyFrom, List<String> possessedKeys, int cost) {
	String memoKey = keyFrom.name + "," + cost + "," + String.join("", possessedKeys.stream().sorted().toList());
	if (_memo.containsKey(memoKey)) {
	    //System.out.println(memoKey);
	    return _memo.get(memoKey);
	}

	List<Path> paths = v.getOpenPathsFrom(keyFrom, possessedKeys);
	if (paths.isEmpty()) {
	    if (cost < _shortestPathLength) {
		System.out.println(cost + ": " + possessedKeys);
		_shortestPathLength = cost;
	    }
	    return cost;
	} else if (cost >= _shortestPathLength) {
	    _memo.put(memoKey, Key.BIG);
	    return Key.BIG;
	}

	int shortest = Key.BIG;
	for (var p : paths) {
	    List<String> newPossessedKeys = new ArrayList<>(possessedKeys);
	    newPossessedKeys.add(p.to.name);
	    int l = findShortestPathLength(v, p.to, newPossessedKeys, cost + p.distance);

	    shortest = Math.min(l, shortest);
	}

	_memo.put(memoKey, shortest);
	return shortest;
    }

    private String solvePartTwo(NeptuneVault vault) {
	List<Key> robots = new ArrayList<>();
	List<String> possessedKeys = new ArrayList<>();
	for (int i = 1; i <= 4; i++) {
	    robots.add(vault.getKeys().get(String.valueOf(i)));
	    possessedKeys.add(String.valueOf(i));
	}

	_shortestPathLength = Key.BIG;
	_memo = new HashMap<>();
	int shortest = findShortestPathLength2(vault, robots, possessedKeys, 0);

	return String.valueOf(shortest);
    }

    private int findShortestPathLength2(NeptuneVault v, List<Key> robots, List<String> possessedKeys, int cost) {
	String memoKey = String.join("", robots.stream().map(r -> r.name).toList()) + "," + cost + "," + String.join("", possessedKeys.stream().sorted().toList());
	if (_memo.containsKey(memoKey)) {
	    //System.out.println(memoKey);
	    return _memo.get(memoKey);
	}

	List<Path> paths = new ArrayList<>();
	for (var robot : robots) {
	    paths.addAll(v.getOpenPathsFrom(robot, possessedKeys));
	}
	if (paths.isEmpty()) {
	    if (cost < _shortestPathLength) {
		System.out.println(cost + ": " + possessedKeys);
		_shortestPathLength = cost;
	    }
	    return cost;
	} else if (cost >= _shortestPathLength) {
	    _memo.put(memoKey, Key.BIG);
	    return Key.BIG;
	}

	int shortest = Key.BIG;
	for (var p : paths) {
	    List<String> newPossessedKeys = new ArrayList<>(possessedKeys);
	    newPossessedKeys.add(p.to.name);
	    List<Key> newRobots = new ArrayList<>();
	    for (var robot : robots) {
		if (robot.name.equals(p.from.name)) {
		    newRobots.add(p.to);
		} else {
		    newRobots.add(robot);
		}
	    }
	    int l = findShortestPathLength2(v, newRobots, newPossessedKeys, cost + p.distance);

	    shortest = Math.min(l, shortest);
	}
	_memo.put(memoKey, shortest);
	return shortest;
    }
}

class NeptuneVault {

    private final Grid2D maze = new Grid2D("#", AdjacencyRule.ROOK);
    private final Map<String, Door> doors = new HashMap<>();
    private final Map<String, Key> keys = new HashMap<>();

    private final List<Coord2D> userStartPositions = new ArrayList<>();
    private List<Path> _cachedPaths;

    public NeptuneVault(List<String> input, boolean numberStartLocations) {
	for (int r = 0; r < input.size(); r++) {
	    String line = input.get(r);
	    var chars = line.split("");
	    for (int c = 0; c < chars.length; c++) {
		Coord2D coord = new Coord2D(c, r);
		if (chars[c].equals("#") || chars[c].equals(".")) {
		    maze.set(coord, chars[c]);
		} else {
		    if (chars[c].equals("@")) {
			userStartPositions.add(coord);
			if (numberStartLocations) {
			    var s = String.valueOf(userStartPositions.size());
			    keys.put(s, new Key(s, coord)); // Calling it a key for simplicity
			} else {
			    keys.put(chars[c], new Key(chars[c], coord)); // Calling it a key for simplicity
			}
		    } else if (chars[c].equals(chars[c].toUpperCase())) {
			doors.put(chars[c], new Door(chars[c], coord));
		    } else {
			keys.put(chars[c], new Key(chars[c], coord));
		    }
		    maze.set(coord, ".");
		}
	    }
	}
    }

    public Grid2D getMaze() {
	return maze;
    }

    public Map<String, Door> getDoors() {
	return doors;
    }

    public Map<String, Key> getKeys() {
	return keys;
    }

    public List<Path> getOpenPathsFrom(Key k, List<String> possessedKeys) {
	if (_cachedPaths == null) {
	    cachePaths();
	}
	List<Path> result = new ArrayList<>();
	Set<String> temp = new HashSet<>(possessedKeys);
	for (var path : _cachedPaths) {
	    if (path.from.equals(k) && !temp.contains(path.to.name) && path.isOpen(temp)) {
		result.add(path);
	    }
	}
	return result;
    }

    private void cachePaths() {
	List<Path> paths = new ArrayList<>();
	List<String> keyNames = new ArrayList<>(keys.keySet());
	for (int i = 0; i < keyNames.size() - 1; i++) {
	    Key k1 = keys.get(keyNames.get(i));
	    for (int j = i + 1; j < keyNames.size(); j++) {
		Key k2 = keys.get(keyNames.get(j));
		int distance = getDistance(k1.position, k2.position, null);
		if (distance > 0) {
		    // Part 2, some keys will not be accessible from start positions
		    Path path = new Path(k1, k2, distance);

		    // Find locked doors en route
		    path.doors.addAll(getDoorsBetween(k1.position, k2.position));
		    paths.add(path);
		    var rPath = path.reversed();
		    paths.add(rPath);
		}
	    }
	}
	// Sorted by distance, ascending
	_cachedPaths = paths.stream().sorted((p1, p2) -> p1.distance - p2.distance).toList();
    }

    private List<Door> getDoorsBetween(Coord2D from, Coord2D to) {
	// Will only be called for coords where there is a path if doors are open
	List<Door> result = new ArrayList<>();
	Set<String> locked = new HashSet<>();
	for (var doorName : getDoors().keySet()) {
	    locked.clear();
	    locked.add(doorName);
	    int distance = getDistance(from, to, locked);
	    if (distance == -1) {
		// Having this door closed blocked path from k1 to k2
		result.add(doors.get(doorName));
	    }
	}
	return result;
    }

    public int getDistance(Coord2D from, Coord2D to, Set<String> lockedDoors) {
	int step = 0;
	if (from.equals(to)) {
	    return step;
	}

	Map<Coord2D, String> lockedDoorCoords = new HashMap<>();
	if (lockedDoors != null) {
	    for (var name : lockedDoors) {
		lockedDoorCoords.put(doors.get(name).position, name);
	    }
	}

	Queue<Coord2D> toVisit = new LinkedList<>();
	Set<Coord2D> visited = new HashSet<>();
	toVisit.add(from);

	while (true) {
	    Queue<Coord2D> nextStepToVisit = new LinkedList<>();
	    while (!toVisit.isEmpty()) {
		var loc = toVisit.remove();
		for (var n : maze.getAdjacentWithValue(loc, ".")) {
		    if (n.equals(to)) {
			step++;
			//System.out.println("distance from " + from + " to " + to + ": " + step);
			return step;
		    }
		    if (visited.contains(n)) {
			continue;
		    }
		    // This is an open space, but is there a locked door?
		    if (lockedDoorCoords.containsKey(n) == false) {
			nextStepToVisit.add(n);
		    }
		}
		visited.add(loc);
	    }
	    if (nextStepToVisit.isEmpty()) {
		break;
	    }
	    toVisit = nextStepToVisit;
	    step++;
	}
	//System.out.println("distance from " + from + " to " + to + ": BLOCKED");
	return -1;
    }

    public void print() {
	Map<Coord2D, String> overlay = new HashMap<>();
	for (var key : keys.values()) {
	    overlay.put(key.position, key.name);
	}
	for (var door : doors.values()) {
	    overlay.put(door.position, door.name);
	}
	for (var c : userStartPositions) {
	    overlay.put(c, "@");
	}
	maze.print(overlay);
    }
}

class Door {

    String name;
    Coord2D position;

    public Door(String name, Coord2D position) {
	this.name = name;
	this.position = position;
    }

    public String getKeyName() {
	return name.toLowerCase();
    }
}

class Key {

    public static final int BIG = 9999;

    String name;
    Coord2D position;

    public Key(String name, Coord2D position) {
	this.name = name;
	this.position = position;
    }

    public String getDoorName() {
	return name.toUpperCase();
    }

    @Override
    public String toString() {
	return "Key " + name;
    }
}

class Path {

    Key from;
    Key to;
    int distance;
    Set<Door> doors = new HashSet<>();

    public Path(Key fromKey, Key toKey, int distance) {
	this.from = fromKey;
	this.to = toKey;
	this.distance = distance;
    }

    public String getName() {
	return from.name + to.name;
    }

    public Key getFrom() {
	return from;
    }

    public Key getTo() {
	return to;
    }

    public int getDistance() {
	return distance;
    }

    public List<Door> getDoors() {
	return doors.stream().toList();
    }

    public boolean isOpen(Set<String> possessedKeys) {
	boolean result = true;
	for (var door : doors) {
	    result = result && possessedKeys.contains(door.name.toLowerCase());
	}
	return result;
    }

    public Path reversed() {
	var r = new Path(to, from, distance);
	r.doors = this.doors;
	return r;
    }

    @Override
    public String toString() {
	return "Path from " + from.name + " to " + to.name;
    }
}
