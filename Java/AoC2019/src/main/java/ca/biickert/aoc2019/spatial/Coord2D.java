/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.biickert.aoc2019.spatial;

/**
 *
 * @author sjb
 */
public class Coord2D {
    private int x;
    private int y;

    public Coord2D(int x, int y) {
	this.x = x;
	this.y = y;
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
    
    public boolean equals(Coord2D other) {
	return this.x == other.getX() && this.y == other.getY();
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
}
