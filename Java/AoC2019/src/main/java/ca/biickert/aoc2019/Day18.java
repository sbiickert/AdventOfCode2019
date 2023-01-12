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
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

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

	NeptuneVault vault = new NeptuneVault(input);
	vault.print();

	var part1Solution = solvePartOne(vault);
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(NeptuneVault vault) {
	Set<String> lockedDoors = new TreeSet<>();
	for (var door : vault.getDoors().values()) {
	    lockedDoors.add(door.name);
	}
	Set<String> keysRemaining = new TreeSet<>();
	for (var key : vault.getKeys().values()) {
	    keysRemaining.add(key.name);
	}
	List<String> keysInPossession = new ArrayList<>();

	int shortestPath = getShortestPathToKeys(vault,
		null,
		lockedDoors,
		keysRemaining,
		keysInPossession,
		0);
	return String.valueOf(shortestPath);
    }

    private Map<String, Integer> _cache = new HashMap<>();
    private int _best = 200;//Integer.MAX_VALUE;

    private int getShortestPathToKeys(NeptuneVault vault,
	    String atMarkedLocation,
	    Set<String> lockedDoors,
	    Set<String> keysRemaining,
	    List<String> keysInPossession,
	    int cost) {

	String cacheKey = atMarkedLocation + "|"
		+ lockedDoors.toString() + "|"
		+ keysRemaining.toString() + "|"
		+ keysInPossession.toString();

	if (_cache.containsKey(cacheKey)) {
	    return _cache.get(cacheKey);
	}

	int shortest = Integer.MAX_VALUE;

	Set<String> destinations = new HashSet<>();
	for (var keyName : keysInPossession) {
	    if (lockedDoors.contains(keyName.toUpperCase())) {
		destinations.add(vault.getKeys().get(keyName).getDoorName());
	    }
	}
	for (var keyName : keysRemaining) {
	    destinations.add(keyName);
	}

	if (destinations.isEmpty()) {
	    _cache.put(cacheKey, 0);
	    if (cost < _best) {
		_best = cost;
	    }
	    System.out.println(cost + " " + keysInPossession);
	    return 0;
	}

	Coord2D start;
	if (atMarkedLocation != null) {
	    if (vault.getDoors().containsKey(atMarkedLocation)) {
		start = vault.getDoors().get(atMarkedLocation).position;
	    } else {
		start = vault.getKeys().get(atMarkedLocation).position;
	    }
	} else {
	    start = vault.getUserStartPosition();
	}

	// Can either pick up an accessible key or an unlockable door
	int temp = 0; // for debugging
	for (var destination : destinations) {
	    int distance = Integer.MAX_VALUE;
	    Coord2D end;
	    boolean isDoor = true;
	    if (vault.getDoors().containsKey(destination)) {
		end = vault.getDoors().get(destination).position;
	    } else {
		end = vault.getKeys().get(destination).position;
		isDoor = false;
	    }

	    distance = vault.getDistance(start, end, lockedDoors);

	    if (distance != -1) {
		// Can get to destination
		// But does that put us over the best distance so far?
		if (cost + distance >= _best) {
		    _cache.put(cacheKey, Integer.MAX_VALUE);
		    return Integer.MAX_VALUE;
		}

		var newKeysRemaining = new TreeSet<>(keysRemaining);
		var newLockedDoors = new TreeSet<>(lockedDoors);
		var newKeysInPossession = new ArrayList<>(keysInPossession);
		if (isDoor) {
		    newLockedDoors.remove(destination);
		} else {
		    newKeysRemaining.remove(destination);
		    newKeysInPossession.add(destination);
		}

		temp = getShortestPathToKeys(vault, destination,
			newLockedDoors, newKeysRemaining, newKeysInPossession,
			cost + distance);

		distance = (temp == Integer.MAX_VALUE) ? temp : distance + temp;
		if (distance > _best) {
		    distance = Integer.MAX_VALUE;
		}
	    } else {
		distance = Integer.MAX_VALUE;
	    }
	    //System.out.println("  ".repeat(depth) + atMarkedLocation + " to " + destination + " = " + distance);

	    shortest = Math.min(shortest, distance);
	}
	//System.out.println("  ".repeat(depth) + "Returning " + shortest);
	_cache.put(cacheKey, shortest);
	return shortest;
    }

    private String solvePartTwo() {
	return "";
    }
}

class NeptuneVault {

    private final Grid2D maze = new Grid2D("#", AdjacencyRule.ROOK);
    private final Map<String, Door> doors = new HashMap<>();
    private final Map<String, Key> keys = new HashMap<>();

    private Coord2D userStartPosition;
    private Map<String, Integer> _cachedDistances = new TreeMap();

    public NeptuneVault(List<String> input) {
	for (int r = 0; r < input.size(); r++) {
	    String line = input.get(r);
	    var chars = line.split("");
	    for (int c = 0; c < chars.length; c++) {
		Coord2D coord = new Coord2D(c, r);
		if (chars[c].equals("#") || chars[c].equals(".")) {
		    maze.set(coord, chars[c]);
		} else {
		    if (chars[c].equals("@")) {
			userStartPosition = coord;
		    } else if (chars[c].equals(chars[c].toUpperCase())) {
			doors.put(chars[c], new Door(chars[c], coord));
		    } else {
			keys.put(chars[c], new Key(chars[c], coord));
		    }
		    maze.set(coord, ".");
		}
	    }
	}
	cacheDistances();
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

    public Coord2D getUserStartPosition() {
	return userStartPosition;
    }

    private void cacheDistances() {
	List<String> allNames = new ArrayList<>(keys.keySet());
	allNames.addAll(doors.keySet());

	for (int i = 0; i < allNames.size() - 1; i++) {
	    for (int j = i + 1; j < allNames.size(); j++) {
		Coord2D c1;
		Coord2D c2;
		if (keys.containsKey(allNames.get(i))) {
		    c1 = keys.get(allNames.get(i)).position;
		} else {
		    c1 = doors.get(allNames.get(i)).position;
		}		    
		if (keys.containsKey(allNames.get(j))) {
		    c2 = keys.get(allNames.get(j)).position;
		} else {
		    c2 = doors.get(allNames.get(j)).position;
		}		    
		int distance = getDistance(c1, c2, null);
		_cachedDistances.put(allNames.get(i) + allNames.get(j), distance);
		_cachedDistances.put(allNames.get(j) + allNames.get(i), distance);
	    }
	}
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
	overlay.put(userStartPosition, "@");
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

    String name;
    Coord2D position;

    public Key(String name, Coord2D position) {
	this.name = name;
	this.position = position;
    }

    public String getDoorName() {
	return name.toUpperCase();
    }
}
