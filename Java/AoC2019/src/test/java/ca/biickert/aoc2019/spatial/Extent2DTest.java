/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ca.biickert.aoc2019.spatial;

import java.util.List;
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
public class Extent2DTest {
    
    public Extent2DTest() {
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
     * Test of expandToFit method, of class Extent2D.
     */
    @Test
    public void testExpandToFit() {
	System.out.println("expandToFit");
	Coord2D c = new Coord2D(-1,-1);
	Extent2D instance = new Extent2D(0,0,5,5);
	instance.expandToFit(c);
	assertEquals(instance, new Extent2D(-1,-1,5,5));
    }

    /**
     * Test of getMin method, of class Extent2D.
     */
    @Test
    public void testGetMin() {
	System.out.println("getMin");
	Extent2D instance = new Extent2D(0,0,1,1);
	Coord2D expResult = new Coord2D(0,0);
	Coord2D result = instance.getMin();
	assertEquals(expResult, result);
	result = instance.getMax();
	assertFalse(expResult.equals(result));
    }

    /**
     * Test of getMax method, of class Extent2D.
     */
    @Test
    public void testGetMax() {
	System.out.println("getMax");
	Extent2D instance = new Extent2D(0,0,1,1);
	Coord2D expResult = new Coord2D(1,1);
	Coord2D result = instance.getMax();
	assertEquals(expResult, result);
	result = instance.getMin();
	assertFalse(expResult.equals(result));
    }

    /**
     * Test of getXMin method, of class Extent2D.
     */
    @Test
    public void testGetXMin() {
	System.out.println("getXMin");
	Extent2D instance = new Extent2D(1,2,3,4);
	int expResult = 1;
	int result = instance.getXMin();
	assertEquals(expResult, result);
    }

    /**
     * Test of getXMax method, of class Extent2D.
     */
    @Test
    public void testGetXMax() {
	System.out.println("getXMax");
	Extent2D instance = new Extent2D(1,2,3,4);
	int expResult = 3;
	int result = instance.getXMax();
	assertEquals(expResult, result);
    }

    /**
     * Test of getYMin method, of class Extent2D.
     */
    @Test
    public void testGetYMin() {
	System.out.println("getYMin");
	Extent2D instance = new Extent2D(1,2,3,4);
	int expResult = 2;
	int result = instance.getYMin();
	assertEquals(expResult, result);
    }

    /**
     * Test of getYMax method, of class Extent2D.
     */
    @Test
    public void testGetYMax() {
	System.out.println("getYMax");
	Extent2D instance = new Extent2D(1,2,3,4);
	int expResult = 4;
	int result = instance.getYMax();
	assertEquals(expResult, result);
    }

    /**
     * Test of getWidth method, of class Extent2D.
     */
    @Test
    public void testGetWidth() {
	System.out.println("getWidth");
	Extent2D instance = new Extent2D(1,1,5,6);
	int expResult = 5;
	int result = instance.getWidth();
	assertEquals(expResult, result);
    }

    /**
     * Test of getHeight method, of class Extent2D.
     */
    @Test
    public void testGetHeight() {
	System.out.println("getHeight");
	Extent2D instance = new Extent2D(1,1,5,6);
	int expResult = 6;
	int result = instance.getHeight();
	assertEquals(expResult, result);
    }

    /**
     * Test of getArea method, of class Extent2D.
     */
    @Test
    public void testGetArea() {
	System.out.println("getArea");
	Extent2D instance = new Extent2D(1,1,10,20);
	int expResult = 200;
	int result = instance.getArea();
	assertEquals(expResult, result);
    }

    /**
     * Test of getAllCoords method, of class Extent2D.
     */
    @Test
    public void testGetAllCoords() {
	System.out.println("getAllCoords");
	Extent2D instance = new Extent2D(1,1,2,2);
	List<Coord2D> result = instance.getAllCoords();
	assertEquals(4, result.size());
	assertTrue(result.contains(new Coord2D(1,2)));
   }

    /**
     * Test of contains method, of class Extent2D.
     */
    @Test
    public void testContains() {
	System.out.println("contains");
	Extent2D instance = new Extent2D(-1,-1,1,1);
	Coord2D c = new Coord2D(0,0);
	boolean expResult = true;
	boolean result = instance.contains(c);
	assertEquals(expResult, result);
		
	c = new Coord2D(1,-1);
	expResult = true;
	result = instance.contains(c);
	assertEquals(expResult, result);
		
	c = new Coord2D(3,2);
	expResult = false;
	result = instance.contains(c);
	assertEquals(expResult, result);
    }

    /**
     * Test of inset method, of class Extent2D.
     */
    @Test
    public void testInset() {
	System.out.println("inset");
	int amt = 1;
	Extent2D instance = new Extent2D(-10,-11, 12, 13);
	Extent2D expResult = new Extent2D(-9,-10,11,12);
	Extent2D result = instance.inset(amt);
	assertEquals(expResult, result);
    }

    /**
     * Test of intersect method, of class Extent2D.
     */
    @Test
    public void testIntersect() throws CloneNotSupportedException {
	System.out.println("intersect");
	Extent2D instance = new Extent2D(0,0,5,5);
	Extent2D other = null;
	Extent2D expResult = null;
	Extent2D result = instance.intersect(other);
	assertEquals(expResult, result);
	
	other = new Extent2D(1,1,2,2);
	expResult = (Extent2D)other.clone();
	result = instance.intersect(other);
	assertEquals(expResult, result);
	
	other = new Extent2D(-1,-1,20,20);
	expResult = (Extent2D)instance.clone();
	result = instance.intersect(other);
	assertEquals(expResult, result);
	
	other = new Extent2D(-1,1,6,6);
	expResult = new Extent2D(0,1,5,5);
 	result = instance.intersect(other);
	assertEquals(expResult, result);
   }

    /**
     * Test of union method, of class Extent2D.
     */
//    @Test
//    public void testUnion() {
//	System.out.println("union");
//	Extent2D other = null;
//	Extent2D instance = null;
//	List<Extent2D> expResult = null;
//	List<Extent2D> result = instance.union(other);
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }

    /**
     * Test of toString method, of class Extent2D.
     */
    @Test
    public void testToString() {
	System.out.println("toString");
	Extent2D instance = new Extent2D(0,1,2,3);
	String expResult = "{min: [0,1] max: [2,3]}";
	String result = instance.toString();
	assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Extent2D.
     */
//    @Test
//    public void testEquals() {
//	System.out.println("equals");
//	Object o = null;
//	Extent2D instance = null;
//	boolean expResult = false;
//	boolean result = instance.equals(o);
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }

    /**
     * Test of hashCode method, of class Extent2D.
     */
//    @Test
//    public void testHashCode() {
//	System.out.println("hashCode");
//	Extent2D instance = null;
//	int expResult = 0;
//	int result = instance.hashCode();
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
    
}
