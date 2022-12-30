/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ca.biickert.aoc2019.spatial;

import java.util.List;
import java.util.Map;
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
public class Grid2DTest {
    
    private Grid2D testGrid;
    
    public Grid2DTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
 	testGrid = new Grid2D();
	
	testGrid.set(new Coord2D(0,0), "A");
	testGrid.set(new Coord2D(10,0), "B");
	testGrid.set(new Coord2D(10,10), "C");
	testGrid.set(new Coord2D(0,10), "D");
   }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of get method, of class Grid2D.
     */
    @Test
    public void testGet() {
	System.out.println("get");
	Coord2D key = new Coord2D(10,0);
	Object expResult = "B";
	Object result = testGrid.get(key);
	assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class Grid2D.
     */
    @Test
    public void testSet() {
	System.out.println("set");
	Coord2D key = new Coord2D(3,4);
	Object value = "X";
	testGrid.set(key, value);
	Object check = testGrid.get(key);
	assertEquals(value,check);
    }

    /**
     * Test of clear method, of class Grid2D.
     */
    @Test
    public void testClear() {
	System.out.println("clear");
	Coord2D key = new Coord2D(10,0);
	testGrid.clear(key);
	Object check = testGrid.get(key);
	assertEquals(testGrid.getDefaultValue(), check);
    }

    /**
     * Test of getExtent method, of class Grid2D.
     */
    @Test
    public void testGetExtent() {
	System.out.println("getExtent");
	Extent2D expResult = new Extent2D(0,0,10,10);
	Extent2D result = testGrid.getExtent();
	assertEquals(expResult, result);
    }

    /**
     * Test of getCoords method, of class Grid2D.
     */
    @Test
    public void testGetCoords() {
	System.out.println("getCoords");
	List<Coord2D> result = testGrid.getCoords();
	assertEquals(4, result.size());
	assertTrue(result.contains(new Coord2D(0,10)));
    }

    /**
     * Test of getCoordsWithValue method, of class Grid2D.
     */
    @Test
    public void testGetCoordsWithValue() {
	System.out.println("getCoordsWithValue");
	Object o = "C";
	Coord2D expResult = new Coord2D(10,10);
	List<Coord2D> result = testGrid.getCoordsWithValue(o);
	assertEquals(1, result.size());
	assertEquals(expResult, result.get(0));
    }

    /**
     * Test of getHistogram method, of class Grid2D.
     */
    @Test
    public void testGetHistogram() {
	System.out.println("getHistogram");
	testGrid.set(new Coord2D(1,3), "A");
	testGrid.set(new Coord2D(1,4), "A");
	testGrid.set(new Coord2D(1,5), "A");
	testGrid.set(new Coord2D(2,3), "B");
	Map<Object, Integer> result = testGrid.getHistogram();
	assertEquals(4, result.size());
	assertTrue(result.containsKey("A"));
	assertTrue(result.containsKey("B"));
	assertTrue(result.containsKey("C"));
	assertTrue(result.containsKey("D"));
	assertEquals(4, result.get("A"));
	assertEquals(2, result.get("B"));
	assertEquals(1, result.get("C"));
	assertEquals(1, result.get("D"));
    }

    /**
     * Test of getOffsets method, of class Grid2D.
     */
    @Test
    public void testGetOffsets() {
	System.out.println("getOffsets");
	var north = new Coord2D(0,-1);
	var west = new Coord2D(-1,0);
	var nw = new Coord2D(-1,-1);
	var se = new Coord2D(1,1);
	Grid2D instance = new Grid2D(".", AdjacencyRule.ROOK);
	List<Coord2D> result = instance.getOffsets();
	assertEquals(4, result.size());
	assertTrue(result.contains(north));
	assertTrue(result.contains(west));
	
	instance = new Grid2D(".", AdjacencyRule.QUEEN);
	result = instance.getOffsets();
	assertEquals(8, result.size());
	assertTrue(result.contains(north));
	assertTrue(result.contains(west));
	assertTrue(result.contains(nw));
	assertTrue(result.contains(se));
	
	instance = new Grid2D(".", AdjacencyRule.BISHOP);
	result = instance.getOffsets();
	assertEquals(4, result.size());
	assertFalse(result.contains(north));
	assertFalse(result.contains(west));
	assertTrue(result.contains(nw));
	assertTrue(result.contains(se));
    }

    /**
     * Test of getAdjacent method, of class Grid2D.
     */
    @Test
    public void testGetAdjacent() {
	System.out.println("getAdjacent");
	var north = new Coord2D(5,5);
	var west = new Coord2D(4,6);
	var nw = new Coord2D(4,5);
	var se = new Coord2D(6,7);
	Coord2D c = new Coord2D(5,6);
	Grid2D instance = new Grid2D(".", AdjacencyRule.ROOK);
	List<Coord2D> result = instance.getAdjacent(c);
	assertEquals(4, result.size());
	assertTrue(result.contains(north));
	assertTrue(result.contains(west));
	assertFalse(result.contains(nw));
	assertFalse(result.contains(se));
	
	instance = new Grid2D(".", AdjacencyRule.QUEEN);
	result = instance.getAdjacent(c);
	assertEquals(8, result.size());
	assertTrue(result.contains(north));
	assertTrue(result.contains(west));
	assertTrue(result.contains(nw));
	assertTrue(result.contains(se));
	
	instance = new Grid2D(".", AdjacencyRule.BISHOP);
	result = instance.getAdjacent(c);
	assertEquals(4, result.size());
	assertFalse(result.contains(north));
	assertFalse(result.contains(west));
	assertTrue(result.contains(nw));
	assertTrue(result.contains(se));

    }

    /**
     * Test of print method, of class Grid2D.
     */
    @Test
    public void testPrint_0args() {
	System.out.println("print");
	testGrid.print();
    }

    /**
     * Test of print method, of class Grid2D.
     */
    @Test
    public void testPrint_boolean() {
	System.out.println("print");
	boolean flipY = true;
	testGrid.print(flipY);
    }
    
}
