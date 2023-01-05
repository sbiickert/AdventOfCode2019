package ca.biickert.aoc2019.spatial;

import java.util.Objects;
import jdk.jshell.spi.ExecutionControl;

/**
 *
 * @author sjb
 */
public class Coord3D extends Coord2D {

    protected int z;

    public Coord3D(int x, int y, int z) {
	super(x, y);
	this.z = z;
	this.hashCode = Objects.hash(x, y, z);
    }

    public int getZ() {
	return z;
    }

    public void setZ(int z) {
	this.z = z;
    }

    @Override
    public String toString() {
	return String.format("[%d,%d,%d]", x, y, z);
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || this.getClass() != o.getClass()) {
	    return false;
	}
	Coord3D that = (Coord3D) o;
	return this.x == that.getX() && this.y == that.getY() && this.z == that.getZ();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
	super.clone();
	return new Coord3D(x, y, z);
    }

    public Coord3D add(Coord3D other) {
	return new Coord3D(this.x + other.getX(),
		this.y + other.getY(),
		this.z + other.getZ());
    }

    public Coord3D delta(Coord3D other) {
	return new Coord3D(other.getX() - this.x,
		other.getY() - this.y,
		other.getZ() - this.z);
    }

    public double distanceTo(Coord3D other) {
	System.out.println("Coord3D.distanceTo is not implemented yet.");
	return 0.0;
    }

    public int manhattanDistanceTo(Coord3D other) {
	var delta = this.delta(other);
	return Math.abs(delta.getX()) + Math.abs(delta.getY()) + Math.abs(delta.getZ());
    }

}
