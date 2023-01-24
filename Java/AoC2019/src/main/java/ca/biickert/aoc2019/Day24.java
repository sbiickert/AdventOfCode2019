package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Coord3D;
import ca.biickert.aoc2019.spatial.Extent2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author sjb
 */
public class Day24 extends Solution {

    public Day24() {
	super(24, "Planet of Discord", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	Grid2D map = parseMap(input);
	var part1Solution = solvePartOne(map);

	RecursiveGrid2D rMap = new RecursiveGrid2D(0, map);
	var part2Solution = solvePartTwo(rMap);

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(Grid2D map) {
	//int i = 0;
	Set<List<Coord2D>> history = new HashSet<>();
	var ext = map.getExtent();
	var coords = ext.getAllCoords();

	while (!history.contains(map.getCoords())) {
	    history.add(map.getCoords());
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
	    //i++;
	    //System.out.println(i);
	}

	long biodiversity = 0;
	for (var coord : map.getCoordsWithValue("#")) {
	    int power = coord.getCol() + (coord.getRow() * ext.getWidth());
	    biodiversity += Math.pow(2, power);
	}

	return String.valueOf(biodiversity);
    }

    private String solvePartTwo(RecursiveGrid2D rMap) {
	var coords = rMap.getExtent().getAllCoords();
	coords.remove(RecursiveGrid2D.center); // Never get neighbors of center

	for (int minute = 1; minute <= 10; minute++) {
	    var zero = rMap.getGrid(0);
	    var nextGrid = new RecursiveGrid2D(0, rMap.getDefaultValue(), rMap.getAdjacencyRule());

	    String value = (String) zero.get(new Coord2D(1, 1), -5);
	    zero.set(new Coord2D(0, 1), 1, "#");

	    rMap = nextGrid;
	    //rMap.print();
	}
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

class RecursiveGrid2D extends Grid2D {

    public static final Coord2D center = new Coord2D(2, 2);
    private static final Extent2D innerExtent = new Extent2D(1, 1, 3, 3);
    private static final Extent2D outerExtent = new Extent2D(0, 0, 4, 4);

    private int level = 0;
    private RecursiveGrid2D outer = null;

    public RecursiveGrid2D(int level, Object defaultValue, AdjacencyRule rule) {
	super(defaultValue, rule);
	this.level = level;
	this.set(center, null);
    }

    public RecursiveGrid2D(int level, Grid2D source) {
	super(source.getDefaultValue(), source.getAdjacencyRule());
	this.level = level;
	this.data = source.getData();
	this.set(center, null);
    }

    public int getLevel() {
	return this.level;
    }

    public Object get(Coord2D c, int level) {
	var rMap = getGrid(level, false);
	if (rMap == null) { return getDefaultValue(); }
	return rMap.get(c);
    }
    
    public void set(Coord2D key, int level, Object value) {
	if (key.equals(center)) {
	    System.out.println("Attempt to set center from RG2D.set(coord, level, value)");
	    return;
	}
	var rMap = getGrid(level, true);
	rMap.set(key, value);
    }

    public RecursiveGrid2D getGrid(int level, boolean create) {
	RecursiveGrid2D result = null;
	if (this.level == level) {
	    return this;
	}
	
	var rMap = this;
	while (result == null && level < rMap.getLevel()) {
	    var outer = rMap.getOuterGrid();
	    if (outer == null) {
		if (!create) { return null; }
		rMap.initOuterGrid();
		outer = rMap.getOuterGrid();
	    }
	    rMap = outer;
	    if (rMap.level == level) {
		result = rMap;
	    }
	}
	while (result == null && level > rMap.getLevel()) {
	    var inner = rMap.getInnerGrid();
	    if (inner == null) {
		if (!create) { return null; }
		rMap.initInnerGrid();
		inner = rMap.getInnerGrid();
	    }
	    rMap = inner;
	    if (rMap.level == level) {
		result = rMap;
	    }
	}
	return result;
    }

    public RecursiveGrid2D getOuterGrid() {
	return this.outer;
    }

    public RecursiveGrid2D getOutermostGrid() {
	var rMap = this;
	while (rMap.getOuterGrid() != null) {
	    rMap = rMap.getOuterGrid();
	}
	return rMap;
    }

    private void initOuterGrid() {
	if (getOuterGrid() != null) {
	    System.out.println("Tried to init outer grid when already set.");
	}
	this.outer = new RecursiveGrid2D(this.level - 1, this.getDefaultValue(), this.getAdjacencyRule());
	this.outer.set(center, this);
    }

    public RecursiveGrid2D getInnerGrid() {
	var value = this.get(center);
	if (value.equals(getDefaultValue())) {
	    return null;
	}
	return (RecursiveGrid2D) value;
    }

    public RecursiveGrid2D getInnermostGrid() {
	var rMap = this;
	while (rMap.getInnerGrid() != null) {
	    rMap = rMap.getInnerGrid();
	}
	return rMap;
    }

    private void initInnerGrid() {
	if (getInnerGrid() != null) {
	    System.out.println("Tried to init inner grid when already set.");
	}
	var inner = new RecursiveGrid2D(this.level + 1, this.getDefaultValue(), this.getAdjacencyRule());
	inner.outer = this;
	this.set(center, inner);
    }
    
    public List<Coord3D> getRAdjacent(Coord2D c) {
	// Using the z dimension for level -1, 0 or 1
	List<Coord2D> offsets = super.getOffsets(); // N (0), E (1), S (2), W (3)
	List<Coord3D> adjacent = new ArrayList<>();
	for (int i = 0; i < 4; i++) {
	    var n2d = c.add(offsets.get(i));
	    var n3d = new Coord3D(n2d.getX(), n2d.getY(), this.getLevel());
	    
	    if (!getExtent().contains(n2d)) {
		// The cell offset from center, one level up
		n2d = center.add(offsets.get(i));
		n3d = new Coord3D(n2d.getX(), n2d.getY(), this.getLevel() - 1);
	    }
	    
	    else if (center.equals(n2d)) {
		// Five cells along an edge, one level down
		Extent2D ext = null;
		if (i == 0)	    { ext = new Extent2D(0,0,4,0); }
		else if (i == 1)    { ext = new Extent2D(4,0,4,4); }
		else if (i == 2)    { ext = new Extent2D(0,4,4,4); }
		else		    { ext = new Extent2D(0,0,0,4); }
		for (var edgeCoord : ext.getAllCoords()) {
		    adjacent.add(new Coord3D(edgeCoord.getX(), edgeCoord.getY(), this.getLevel() + 1));
		}
	    }
	    
	    else {
		adjacent.add(n3d);
	    }
	}
	return adjacent;
    }

    public boolean coordIsOnInsideBorder(Coord2D c) {
	return innerExtent.contains(c) && center.equals(c) == false;
    }
    
    public boolean coordIsOnOutsideBorder(Coord2D c) {
	return outerExtent.contains(c) && innerExtent.contains(c) == false;
    }
    
    @Override
    public Extent2D getExtent() {
	return outerExtent;
    }

    public void print() {
	var rMap = this.getOutermostGrid();

	while (rMap != null) {
	    System.out.println(rMap.level);
	    for (int r = outerExtent.getYMin(); r <= outerExtent.getYMax(); r++) {
		String line = "";
		for (int c = outerExtent.getXMin(); c <= outerExtent.getXMax(); c++) {
		    if (c == center.getX() && r == center.getY()) {
			var inner = rMap.getInnerGrid();
			if (inner == null) {
			    line += "!";
			} else {
			    line += "?";
			}
		    } else {
			line += String.valueOf(rMap.get(new Coord2D(c, r)));
		    }
		}
		System.out.println(line);
	    }

	    rMap = rMap.getInnerGrid();
	}
    }
}
