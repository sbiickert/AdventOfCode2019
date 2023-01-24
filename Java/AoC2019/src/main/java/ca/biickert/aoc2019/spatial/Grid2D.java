package ca.biickert.aoc2019.spatial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sjb
 */
public class Grid2D implements Cloneable {

    public static final Coord2D NORTH = new Coord2D(0, -1);
    public static final Coord2D SOUTH = new Coord2D(0, 1);
    public static final Coord2D WEST = new Coord2D(-1, 0);
    public static final Coord2D EAST = new Coord2D(1, 0);
    public static final Coord2D NW = new Coord2D(-1, -1);
    public static final Coord2D NE = new Coord2D(-1, 1);
    public static final Coord2D SW = new Coord2D(1, -1);
    public static final Coord2D SE = new Coord2D(1, 1);
    public static final Coord2D UP = NORTH;
    public static final Coord2D DOWN = SOUTH;
    public static final Coord2D LEFT = WEST;
    public static final Coord2D RIGHT = EAST;

    private AdjacencyRule rule;
    private Object defaultValue;
    protected Map<Coord2D, Object> data = new HashMap<>();
    private Extent2D extent = null;
    private Map<AdjacencyRule, List<Coord2D>> offsets = new HashMap<>();

    public Grid2D() {
	this(".", AdjacencyRule.ROOK);
    }

    public Grid2D(Object defaultValue, AdjacencyRule rule) {
	this.defaultValue = defaultValue;
	this.rule = rule;
    }

    public AdjacencyRule getAdjacencyRule() {
	return rule;
    }

    public Object getDefaultValue() {
	return defaultValue;
    }

    public Object get(Coord2D key) {
	var value = data.get(key);
	return (value == null) ? defaultValue : value;
    }

    public void set(Coord2D key, Object value) {
	data.put(key, value);
	if (extent == null) {
	    extent = new Extent2D(new Coord2D[]{key});
	} else {
	    extent.expandToFit(key);
	}
    }

    public void clear(Coord2D key) {
	data.remove(key);
    }
    
    public void clearAll() {
	data.clear();
    }
    
    public Map<Coord2D, Object> getData() {
	return new HashMap<>(data);
    }

    public Extent2D getExtent() {
	try {
	    return (Extent2D) extent.clone();
	} catch (CloneNotSupportedException e) {
	}
	return null;
    }

    public List<Coord2D> getCoords() {
	List<Coord2D> result = new ArrayList<>();
	for (var key : data.keySet()) {
	    result.add(key);
	}
	return result;
    }

    public List<Coord2D> getCoordsWithValue(Object o) {
	return getCoords().stream()
		.filter(key -> data.get(key).equals(o))
		.toList();
    }

    public Map<Object, Integer> getHistogram() {
	Map<Object, Integer> hist = new HashMap<>();

	for (var key : getCoords()) {
	    var o = data.get(key);
	    if (hist.containsKey(o) == false) {
		hist.put(o, 0);
	    }
	    var count = hist.get(o);
	    hist.replace(o, count + 1);
	}

	return hist;
    }

    public List<Coord2D> getOffsets() {
	if (this.offsets.containsKey(rule)) {
	    return this.offsets.get(rule);
	}
	List<Coord2D> coords = new ArrayList<>();

	try {
	    if (rule == AdjacencyRule.ROOK || rule == AdjacencyRule.QUEEN) {
		coords.add((Coord2D)NORTH.clone());
		coords.add((Coord2D)EAST.clone());
		coords.add((Coord2D)SOUTH.clone());
		coords.add((Coord2D)WEST.clone());
	    }
	    if (rule == AdjacencyRule.BISHOP || rule == AdjacencyRule.QUEEN) {
		coords.add((Coord2D)NW.clone());
		coords.add((Coord2D)NE.clone());
		coords.add((Coord2D)SW.clone());
		coords.add((Coord2D)SE.clone());
	    }
	} catch (CloneNotSupportedException e) {}

	this.offsets.put(rule, coords);

	return coords;
    }

    public List<Coord2D> getAdjacent(Coord2D c) {
	List<Coord2D> result = new ArrayList<>();

	for (var offset : getOffsets()) {
	    result.add(c.add(offset));
	}

	return result;
    }

    public List<Coord2D> getAdjacentWithValue(Coord2D c, Object value) {
	List<Coord2D> result = getAdjacent(c);
	result = result.stream().filter(n -> get(n).equals(value)).toList();
	return result;
    }

    public List<Coord2D> getLineOfSightCoords(Coord2D c) {
	List<Coord2D> result = new ArrayList<>();

	for (var offset : getOffsets()) {
	    Coord2D losCoord = c.add(offset);
	    while (true) {
		if (getExtent().contains(losCoord) == false) {
		    break;
		}
		if (get(losCoord).equals(defaultValue) == false) {
		    result.add(losCoord);
		    break;
		}
		losCoord = losCoord.add(offset);
	    }
	}

	return result;
    }

    public void print() {
	print(new HashMap<>(), false);
    }
    
    public void print(Map<Coord2D, String> overlay) {
	print(overlay, false);
    }

    public void print(Map<Coord2D, String> overlay, boolean flipY) {
	var ext = getExtent();
	int startRow = ext.getYMin();
	int endRow = ext.getYMax();
	int step = 1;

	if (flipY) {
	    step = -1;
	    int temp = startRow;
	    startRow = endRow;
	    endRow = temp;
	}
	var row = startRow;
	while (true) {
	    String line = "";
	    for (var col = ext.getXMin(); col <= ext.getXMax(); col++) {
		Coord2D c = new Coord2D(col, row);
		var value = data.get(c);
		if (overlay != null && overlay.containsKey(c)) {
		    value = overlay.get(c);
		}
		value = (value == null) ? defaultValue : value;
		line += value;
	    }
	    System.out.println(line);
	    if (row == endRow) {
		break;
	    }
	    row += step;
	}
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
	super.clone();
	var grid = new Grid2D(defaultValue, rule);
	grid.data = new HashMap<>(data);
	grid.extent = (Extent2D)extent.clone();
	grid.offsets = new HashMap<>(offsets);
	return grid;
    }
}
