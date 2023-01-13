package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.Algorithms;
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
import java.util.PriorityQueue;
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
	Key start = vault.getKeys().get("@");
	start.costToReach = 0;
	Set<String> unvisited = new HashSet<>();
	for (var k : vault.getKeys().keySet()) {
	    unvisited.add(k);
	}
	
	PriorityQueue<String> working = new PriorityQueue<String>((s1, s2) -> vault.getKeys().get(s1).costToReach - vault.getKeys().get(s2).costToReach);
	working.add(start.name);
	
	while (!unvisited.isEmpty()) {
	    String currentName = working.remove();
	    Key current = vault.getKeys().get(currentName);
	    Door currentDoor = vault.getDoors().get(current.getDoorName());
	    if (currentDoor != null) {
		currentDoor.isLocked = false;
	    }
	    
	    List<Path> paths = vault.getOpenPathsFrom(current);
	    for (var p : paths) {
		int costTo = current.costToReach + p.distance;
		if (p.to.costToReach > costTo) {
		    p.to.costToReach = costTo;
		    working.add(p.to.name);
		}
	    }
	    
	    unvisited.remove(current.name);
	}
	
	var result = vault.getKeys().values().stream().sorted((k1,k2) -> k1.costToReach - k2.costToReach).toList();
	System.out.println(result);
	
	return String.valueOf(result.get(result.size()-1).costToReach);
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
    private List<Path> _cachedPaths = new ArrayList();

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
			keys.put(chars[c], new Key(chars[c], coord)); // Calling it a key for simplicity
		    } else if (chars[c].equals(chars[c].toUpperCase())) {
			doors.put(chars[c], new Door(chars[c], coord));
		    } else {
			keys.put(chars[c], new Key(chars[c], coord));
		    }
		    maze.set(coord, ".");
		}
	    }
	}
	cachePaths();
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
    
//    public Key getLastKey() {
//	Set<String> keysNoDoors = new TreeSet<>();
//	for (var keyName : keys.keySet()) {
//	    if (keyName.equals("@") == false && doors.containsKey(keyName.toUpperCase()) == false) {
//		keysNoDoors.add(keyName);
//	    }
//	}
//	var lastKeyName = (String)keysNoDoors.toArray()[keysNoDoors.size()-1];
//	return keys.get(lastKeyName);
//    }
    
    public List<Path> getOpenPathsFrom(Key k) {
	List<Path> result = new ArrayList<>();
	for (var path : _cachedPaths) {
	    if (path.from.equals(k) && path.isOpen()) {
		result.add(path);
	    }
	}
	return result;
    }

    private void cachePaths() {
	List<String> keyNames = new ArrayList<>(keys.keySet());
	for (int i = 0; i < keyNames.size() - 1; i++) {
	    Key k1 = keys.get(keyNames.get(i));
	    for (int j = i + 1; j < keyNames.size(); j++) {
		Key k2 = keys.get(keyNames.get(j));
		int distance = getDistance(k1.position, k2.position, null);
		Path path = new Path(k1, k2, distance);

		// Find locked doors en route
		path.doors.addAll(getDoorsBetween(k1.position, k2.position));
		_cachedPaths.add(path);
		var rPath = path.reversed();
		_cachedPaths.add(rPath);
	    }
	}
    }

    private List<Door> getDoorsBetween(Coord2D from, Coord2D to) {
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
	overlay.put(userStartPosition, "@");
	maze.print(overlay);
    }
}

class Door {

    String name;
    Coord2D position;
    boolean isLocked;

    public Door(String name, Coord2D position) {
	this.name = name;
	this.position = position;
	this.isLocked = true;
    }

    public String getKeyName() {
	return name.toLowerCase();
    }
}

class Key {

    String name;
    Coord2D position;
    int costToReach = Integer.MAX_VALUE;

    public Key(String name, Coord2D position) {
	this.name = name;
	this.position = position;
    }

    public String getDoorName() {
	return name.toUpperCase();
    }
   
    @Override
    public String toString() {
	return "Key " + name + " cost: " + costToReach;
    }
}

class Path {

    Key from;
    Key to;
    int distance;
    List<Door> doors = new ArrayList<>();

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
	return doors;
    }

    public boolean isOpen() {
	boolean result = true;
	for (var door : doors) {
	    result = result && door.isLocked == false;
	}
	return result;
    }
    
    public Path reversed() {
	return new Path(to, from, distance);
    }
    
    @Override
    public String toString() {
	return "Path from " + from.name + " to " + to.name;
    }
}
