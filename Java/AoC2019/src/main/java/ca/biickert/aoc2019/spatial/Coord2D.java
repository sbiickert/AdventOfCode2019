
package ca.biickert.aoc2019.spatial;

import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author sjb
 */
public class Coord2D implements Cloneable {
    protected int x;
    protected int y;
    protected int hashCode;

    public Coord2D(int x, int y) {
	this.x = x;
	this.y = y;
	this.hashCode = Objects.hash(x,y);
    }

    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }

    public int getY() {
	return y;
    }

    public void setY(int y) {
	this.y = y;
    }

    public int getCol() {
	return x;
    }

    public void setCol(int col) {
	this.x = col;
    }

    public int getRow() {
	return y;
    }

    public void setRow(int row) {
	this.y = row;
    }
    
    @Override
    public String toString() {
	return String.format("[%d,%d]", x, y);
    }
    
    @Override
    public boolean equals(Object o) {
	if (this == o) { return true; }
	if (o == null || this.getClass() != o.getClass()) {return false; }
	Coord2D that = (Coord2D) o;
	return this.x == that.getX() && this.y == that.getY();
    }
    
    @Override
    public int hashCode() {
	return this.hashCode;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
	super.clone();
	return new Coord2D(x, y);
    }
    
    public Coord2D add(Coord2D other) {
	return new Coord2D(this.x + other.getX(), this.y + other.getY());
    }
    
    public Coord2D delta(Coord2D other) {
	return new Coord2D(other.getX() - this.x, other.getY() - this.y);
    }
    
    public double distanceTo(Coord2D other) {
	var delta = this.delta(other);
	return Math.sqrt(Math.pow(delta.getX(), 2) + Math.pow(delta.getY(), 2));
    }
    
    public int manhattanDistanceTo(Coord2D other) {
	var delta = this.delta(other);
	return Math.abs(delta.getX()) + Math.abs(delta.getY());
    }
    
    public Comparator<Coord2D> readingOrder() {
	return (c1, c2) -> {
	    if (c1.y == c2.y) {
		return Integer.compare(c1.x, c2.x);
	    }
	    return Integer.compare(c1.y, c2.y);
	};
    }
}
