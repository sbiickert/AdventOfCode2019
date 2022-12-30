/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.biickert.aoc2019.spatial;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author sjb
 */
public final class Extent2D implements Cloneable {
    private Coord2D min;
    private Coord2D max;
    private int hashCode;

    public Extent2D(Coord2D min, Coord2D max) {
	this(new Coord2D[]{min, max});
    }
    
    public Extent2D(int xmin, int ymin, int xmax, int ymax) {
	this(new Coord2D[]{new Coord2D(xmin, ymin), new Coord2D(xmax, ymax)});
    }
    
    public Extent2D(Coord2D[] coords) {
	try {
	    this.min = (Coord2D)coords[0].clone();
	    this.max = (Coord2D)coords[0].clone();
	}
	catch (CloneNotSupportedException cnse) {}
	for (var i = 1; i < coords.length; i++) {
	    this.expandToFit(coords[i]);
	}
	this.hashCode = Objects.hash(min,max);
    }
    
    public void expandToFit(Coord2D c) {
	this.min.setX(Math.min(this.min.getX(), c.getX()));
	this.max.setX(Math.max(this.max.getX(), c.getX()));
	this.min.setY(Math.min(this.min.getY(), c.getY()));
	this.max.setY(Math.max(this.max.getY(), c.getY()));
    }

    public Coord2D getMin() {
	return min;
    }

    public Coord2D getMax() {
	return max;
    }
    
    public int getXMin() { return min.getX(); }
    public int getXMax() { return max.getX(); }
    public int getYMin() { return min.getY(); }
    public int getYMax() { return max.getY(); }
    
    public int getWidth() {
	return max.getX() - min.getX() + 1;
    }
    
    public int getHeight() {
	return max.getY() - min.getY() + 1;
    }
    
    public int getArea() {
	return getWidth() * getHeight();
    }
    
    public List<Coord2D> getAllCoords() {
	List<Coord2D> result = new ArrayList<>();
	for (var x = min.getX(); x <= max.getX(); x++) {
	    for (var y = min.getY(); y <= max.getY(); y++) {
		result.add(new Coord2D(x,y));
	    }
	}
	return result;
    }
    
    public boolean contains(Coord2D c) {
	if (c == null) {return false;}
	return min.getX() <= c.getX() && c.getX() <= max.getX() &&
		min.getY() <= c.getY() && c.getY() <= max.getY();
    }
    
    public Extent2D inset(int amt) {
	return new Extent2D(min.getX()+amt, min.getY()+amt, max.getX()-amt, max.getY()-amt);
    }
    
    public Extent2D intersect(Extent2D other) {
	if (other == null) { return null; }
	int commonMinX = Math.max(getXMin(), other.getXMin());
	int commonMaxX = Math.min(getXMax(), other.getXMax());
	if (commonMaxX < commonMinX) { return null; }
	int commonMinY = Math.max(getYMin(), other.getYMin());
	int commonMaxY = Math.min(getYMax(), other.getYMax());
	if (commonMaxY < commonMinY) { return null; }
	
	return new Extent2D(commonMinX, commonMinY, commonMaxX, commonMaxY);
    }
    
    public List<Extent2D> union(Extent2D other) {
	System.out.println("Exten2D::union is not implemented.");
	assert(false);
	return null;
    }
    
    @Override
    public String toString() {
	return "{min: " + min + " max: " + max + "}";
    }
    
    @Override
    public boolean equals(Object o) {
	if (this == o) { return true; }
	if (o == null || this.getClass() != o.getClass()) {return false; }
	Extent2D that = (Extent2D) o;
	return this.min.equals(that.getMin()) && this.max.equals(that.getMax());
    }
    
    @Override
    public int hashCode() {
	return this.hashCode;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
	super.clone();
	return new Extent2D((Coord2D)min.clone(), (Coord2D)max.clone());
    }
}
