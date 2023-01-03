
package ca.biickert.aoc2019.util;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author sbiickert
 */
public class AlgorithmsTest {
    
    public AlgorithmsTest() {
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
     * Test of getAllPermutations method, of class Algorithms.
     */
    @Test
    public void testGetAllPermutations() {
        System.out.println("getAllPermutations");
        List<String> originals = new ArrayList<>();
        originals.add("A");
        originals.add("B");
        originals.add("C");
        List<List<String>> result = Algorithms.getAllPermutations(originals);
        System.out.println(result);
    }
    
}
