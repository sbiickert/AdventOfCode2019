/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ca.biickert.aoc2019.spatial;

import ca.biickert.aoc2019.spatial.Coord2D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author sjb
 */
public class Coord2DTest {
    
    public Coord2DTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getX method, of class Coord2D.
     */
    @Test
    public void testGetX() {
	System.out.println("getX");
	Coord2D instance = new Coord2D(5, 8);
	int expResult = 5;
	int result = instance.getX();
	assertEquals(expResult, result);
    }

    /**
     * Test of setX method, of class Coord2D.
     */
    @Test
    public void testSetX() {
	System.out.println("setX");
	int x = 0;
	Coord2D instance = new Coord2D(5, 8);
	instance.setX(x);
	int result = instance.getX();
	assertEquals(result, x);
    }

    /**
     * Test of getY method, of class Coord2D.
     */
    @Test
    public void testGetY() {
	System.out.println("getY");
	Coord2D instance = new Coord2D(5,8);
	int expResult = 8;
	int result = instance.getY();
	assertEquals(expResult, result);
    }

    /**
     * Test of setY method, of class Coord2D.
     */
    @Test
    public void testSetY() {
	System.out.println("setY");
	int y = 0;
	Coord2D instance = new Coord2D(5, 8);
	instance.setY(y);
	int result = instance.getY();
	assertEquals(result, y);
    }

    /**
     * Test of getCol method, of class Coord2D.
     */
    @Test
    public void testGetCol() {
	System.out.println("getCol");
	Coord2D instance = new Coord2D(5,8);
	int expResult = 5;
	int result = instance.getCol();
	assertEquals(expResult, result);
    }

    /**
     * Test of setCol method, of class Coord2D.
     */
    @Test
    public void testSetCol() {
	System.out.println("setCol");
	int col = 0;
	Coord2D instance = new Coord2D(5,8);
	instance.setCol(col);
	int result = instance.getCol();
	assertEquals(col, result);
    }

    /**
     * Test of getRow method, of class Coord2D.
     */
    @Test
    public void testGetRow() {
	System.out.println("getRow");
	Coord2D instance = new Coord2D(5,8);
	int expResult = 8;
	int result = instance.getRow();
	assertEquals(expResult, result);
    }

    /**
     * Test of setRow method, of class Coord2D.
     */
    @Test
    public void testSetRow() {
	System.out.println("setRow");
	int row = 0;
	Coord2D instance = new Coord2D(5,8);
	instance.setRow(row);
	int result = instance.getRow();
	assertEquals(row, result);
   }

    /**
     * Test of toString method, of class Coord2D.
     */
    @Test
    public void testToString() {
	System.out.println("toString");
	Coord2D instance = new Coord2D(5,8);
	String expResult = "[5,8]";
	String result = instance.toString();
	assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Coord2D.
     */
    @Test
    public void testEquals() {
	System.out.println("equals");
	Coord2D instance = new Coord2D(5,8);
	Coord2D otherNE = new Coord2D(10,11);
	Coord2D otherE = new Coord2D(5,8);
	assertTrue(instance.equals(otherE));
	assertTrue(otherE.equals(instance));
	assertFalse(instance.equals(otherNE));
	assertFalse(otherNE.equals(instance));
   }

    /**
     * Test of add method, of class Coord2D.
     */
    @Test
    public void testAdd() {
	System.out.println("add");
	Coord2D other = new Coord2D(1,5);
	Coord2D instance = new Coord2D(5,8);
	Coord2D expResult = new Coord2D(6,13);
	Coord2D result = instance.add(other);
	assertTrue(expResult.equals(result));
    }

    /**
     * Test of delta method, of class Coord2D.
     */
    @Test
    public void testDelta() {
	System.out.println("delta");
	Coord2D other = new Coord2D(1,2);
	Coord2D instance = new Coord2D(5,8);
	Coord2D expResult = new Coord2D(-4,-6);
	Coord2D result = instance.delta(other);
	assertTrue(expResult.equals(result));
	
	expResult = new Coord2D(4,6);
	result = other.delta(instance);
	assertTrue(expResult.equals(result));
    }

    /**
     * Test of distanceTo method, of class Coord2D.
     */
    @Test
    public void testDistanceTo() {
	System.out.println("distanceTo");
	Coord2D other = new Coord2D(6,9);
	Coord2D instance = new Coord2D(5,8);
	double expResult = Math.sqrt(2);
	double result = instance.distanceTo(other);
	assertEquals(expResult, result, 0);
    }

    /**
     * Test of manhattanDistanceTo method, of class Coord2D.
     */
    @Test
    public void testManhattanDistanceTo() {
	System.out.println("manhattanDistanceTo");
	Coord2D other = new Coord2D(1,2);
	Coord2D instance = new Coord2D(5,8);
	int expResult = 10;
	int result = instance.manhattanDistanceTo(other);
	assertEquals(expResult, result);
	result = other.manhattanDistanceTo(instance);
	assertEquals(expResult, result);
    }
    
}
