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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sjb
 */
public class Day20 extends Solution {

    Donut donut;

    public Day20() {
	super(20, "Donut Maze", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	donut = new Donut(input);

	var part1Solution = solvePartOne();
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne() {
	// Do a BFS of the donut, following Portals where found
	int step = 0;
	Set<Coord2D> visited = new HashSet<>();
	List<Coord2D> toVisit = new ArrayList<>();
	toVisit.add(donut.getStart());
	
	while (true) {
	    List<Coord2D> nextToVisit = new ArrayList<>();
	    
	    for (var coord : toVisit) {
		if (coord.equals(donut.getEnd())) {
		    return String.valueOf(step);
		}
		for (var n : donut.getMaze().getAdjacent(coord)) {
		    if (visited.contains(n)) { continue; }
		    var value = donut.getMaze().get(n);
		    if (value instanceof Portal) {
			nextToVisit.add(((Portal)value).out);
		    }
		    else if (value.equals(".")) {
			nextToVisit.add(n);
		    }
		}
		visited.add(coord);
	    }
	    
	    toVisit = nextToVisit;
	    step++;
	}
    }

    private String solvePartTwo() {
	// Do a BFS of the donut, following Portals where found
	// Tracking recursion depth
	final int MAX_DEPTH = 200;
	int step = 0;
	Set<RLocation> visited = new HashSet<>();
	List<RLocation> toVisit = new ArrayList<>();
	toVisit.add(new RLocation(0, donut.getStart()));
	
	while (true) {
	    List<RLocation> nextToVisit = new ArrayList<>();
	    
	    for (var rloc : toVisit) {
		//System.out.println(rloc);
		if (rloc.depth() > MAX_DEPTH) {
		    return "Safety limit of " + MAX_DEPTH + " reached.";
		}
		if (rloc.depth() == 0 && rloc.location().equals(donut.getEnd())) {
		    // EXIT
		    return String.valueOf(step);
		}
		for (var n : donut.getMaze().getAdjacent(rloc.location())) {
		    var nRLoc = new RLocation(rloc.depth(), n);
		    if (visited.contains(nRLoc)) { continue; }
		    var value = donut.getMaze().get(nRLoc.location());
		    if (value instanceof Portal portal) {
			if (portal.name.equals("AA")) { continue; }
			if (portal.name.equals("ZZ")) { continue; }
			else if (portal.isInner) {
			    nRLoc = new RLocation(rloc.depth()+1, portal.out);
			    // Don't backtrack into the portal
			    visited.add(new RLocation(rloc.depth()+1, portal.opposite.in));
			}
			else {
			    // Can't go to a level less than zero
			    if (rloc.depth() < 1) { continue; }
			    nRLoc = new RLocation(rloc.depth()-1, portal.out);
			}
			nextToVisit.add(nRLoc);
		    }
		    else if (value.equals(".")) {
			nextToVisit.add(nRLoc);
		    }
		}
		visited.add(rloc);
	    }
	    
	    toVisit = nextToVisit;
	    step++;
	}
    }

}

class Donut {
    
    private Grid2D maze;
    private Coord2D start;
    private Coord2D end;
    
    public Donut(List<String> defn) {
	maze = new Grid2D("#", AdjacencyRule.ROOK);
	for (int r = 0; r < defn.size(); r++) {
	    var line = defn.get(r);
	    var chars = line.split("");
	    for (int c = 0; c < chars.length; c++) {
		var coord = new Coord2D(c, r);
		if (!chars[c].equals(" ") && !chars[c].equals("#")) {
		    maze.set(coord, chars[c]);
		}
	    }
	}
	//maze.print();
	gatherPortals();
    }

    public Grid2D getMaze() {
	return maze;
    }

    public Coord2D getStart() {
	return start;
    }

    public Coord2D getEnd() {
	return end;
    }
    
    
    private void gatherPortals() {
	Map<String,Portal> gather = new HashMap<>();
	var innerExt = maze.getExtent().inset(3);
	
	for (var coord : maze.getCoords()) {
	    String value = (String)maze.get(coord);
	    if (value.equals(".")) { continue; }
	    // This is a letter. If there is neighbouring . and a neighbouring letter,
	    // This is a portal.
	    Coord2D openSpace = null;
	    Coord2D otherLetter = null;
	    for (var n : maze.getAdjacent(coord)) {
		if (maze.get(n).equals(".")) {
		    openSpace = n;
		} 
		else if (maze.get(n).equals("#")) {}
		else {
		    otherLetter = n;
		}
	    }
	    if (otherLetter != null && openSpace != null) {
		List<Coord2D> l = new ArrayList<>();
		l.add(coord);
		l.add(otherLetter);
		l = l.stream().sorted(coord.readingOrder()).toList();
		String key = String.join("", l.stream().map(c -> (String)maze.get(c)).toList());
		if (!gather.containsKey(key)) {
		    //System.out.println(key);
		    var p = new Portal(key, coord, openSpace);
		    p.isInner = innerExt.contains(coord);
		    gather.put(key, p); // Temp, points to own open space
		    maze.set(coord, p);
		}
		else {
		    var other = gather.remove(key);
		    var temp = other.out;
		    other.out = openSpace; // Now points to the partner
		    var p = new Portal(key, coord, temp);
		    p.isInner = !other.isInner;
		    p.opposite = other;
		    other.opposite = p;
		    //System.out.println(p.description());
		    //System.out.println(other.description());
		    maze.set(coord, p);
		}
	    }
	}
	
	for (var p : gather.values()) {
	    maze.set(p.in, p);
	    if (p.name.equals("ZZ")) {
		end = p.out; // The open tile next to AA
	    }
	    else if (p.name.equals("AA")) {
		start = p.out; // The open tile next to ZZ
	    }
	}
	
	//maze.print();
    }
}

class Portal {
    String name;
    Coord2D in;
    Coord2D out;
    Portal opposite = null;
    boolean isInner = false;
    
    public Portal(String name, Coord2D inCoord, Coord2D outCoord) {
	this.name = name;
	this.in = inCoord;
	this.out = outCoord;
    }
    
    @Override
    public String toString() {
	if (name.equals("AA")) {
	    return "A";
	}
	if (name.equals("ZZ")) {
	    return "Z";
	}
	return "P";
    }
    public String description() {
	return "Portal " + name + " " + in + " " + (isInner ? "+" : "-") + " " + out;
    }
    
    @Override
    public boolean equals(Object o) {
	if (this == o) { return true; }
	if (o == null || this.getClass() != o.getClass()) {return false; }
	Portal that = (Portal) o;
	return this.name.equals(that.name) && this.in.equals(that.in) && this.out.equals(that.out);
    }
}

record RLocation(int depth, Coord2D location) {}