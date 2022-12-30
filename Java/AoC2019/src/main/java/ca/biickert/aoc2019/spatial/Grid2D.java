/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.biickert.aoc2019.spatial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sjb
 */
public class Grid2D {
    private AdjacencyRule rule;
    private Object defaultValue;
    private Map<Coord2D, Object> data = new HashMap<>();
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
	    extent = new Extent2D(new Coord2D[] {key});
	}
	else {
	    extent.expandToFit(key);
	}
    }
    
    public void clear(Coord2D key) {
	data.remove(key);
    }
    
    public Extent2D getExtent() {
	try {
	    return (Extent2D)extent.clone();
	} catch (CloneNotSupportedException e) {}
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
	    hist.replace(o, count+1);
	}
	
	return hist;
    }
    
    public List<Coord2D> getOffsets() {
	if (this.offsets.containsKey(rule)) { return this.offsets.get(rule); }
	List<Coord2D> coords = new ArrayList<>();
	
	if (rule == AdjacencyRule.ROOK || rule == AdjacencyRule.QUEEN) {
	    coords.add(new Coord2D(1,0));
	    coords.add(new Coord2D(0,1));
	    coords.add(new Coord2D(-1,0));
	    coords.add(new Coord2D(0,-1));
	}
	if (rule == AdjacencyRule.BISHOP || rule == AdjacencyRule.QUEEN) {
	    coords.add(new Coord2D(-1,-1));
	    coords.add(new Coord2D(-1,1));
	    coords.add(new Coord2D(1,-1));
	    coords.add(new Coord2D(1,1));
	}
	
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
    
    public void print() {
	print(false);
    }
    
    public void print(boolean flipY) {
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
		var value = data.get(new Coord2D(col, row));
		line += (value == null) ? defaultValue : value;
	    }
	    System.out.println(line);
	    if (row == endRow) { break; }
	    row += step;
	}
    }
}

enum AdjacencyRule {
    ROOK,
    BISHOP,
    QUEEN
}