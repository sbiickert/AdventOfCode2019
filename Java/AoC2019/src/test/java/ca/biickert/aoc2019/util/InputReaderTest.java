
package ca.biickert.aoc2019.util;

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
public class InputReaderTest {
    
    public InputReaderTest() {
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
     * Test of readInputFile method, of class InputReader.
     */
    @Test
    public void testReadInputFile() {
	System.out.println("readInputFile");
	String filename = "../../Input/day00_test.txt";
	List<String> result = InputReader.readInputFile(filename);
	assertEquals(8, result.size());
	assertEquals("ABCDEF", result.get(7));
    }

    /**
     * Test of readGroupedInputFile method, of class InputReader.
     */
    @Test
    public void testReadGroupedInputFile_String_int() {
	System.out.println("readGroupedInputFile");
	String filename = "../../Input/day00_test.txt";
	int groupIndex = 0;
	List<String> result = InputReader.readGroupedInputFile(filename, groupIndex);
	assertEquals(3, result.size());
	assertEquals("BBBBB", result.get(1));
    }

    /**
     * Test of readGroupedInputFile method, of class InputReader.
     */
    @Test
    public void testReadGroupedInputFile_String() {
	System.out.println("readGroupedInputFile");
	String filename = "../../Input/day00_test.txt";
	List<List<String>> result = InputReader.readGroupedInputFile(filename);
	assertEquals(3, result.size());
	assertEquals("BB", result.get(1).get(1));
    }
    
}
