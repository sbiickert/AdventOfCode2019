
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
public class SolutionTest {
    
    public SolutionTest() {
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
     * Test of inputsForSolution method, of class Solution.
     */
    @Test
    public void testInputsForSolution() {
	System.out.println("inputsForSolution");
	Solution s = new SolutionImpl(0, "Test Name", true);
	List<Input> result = Solution.inputsForSolution(s);
	assertEquals(4, result.size());
	assertTrue(result.get(0).filename().contains("challenge"));
	assertTrue(result.get(1).filename().endsWith("day00_test.txt"));
    }

    /**
     * Test of filename method, of class Solution.
     */
    @Test
    public void testFilename() {
	System.out.println("filename");
	int day = 0;
	
	String expResultChallenge = String.format("%s/day%02d_challenge.txt", Solution.RELATIVE_INPUT_PATH, day);
	String resultChallenge = Solution.filename(day, false);
	assertEquals(expResultChallenge, resultChallenge);
	
	String expResultTest = String.format("%s/day%02d_test.txt", Solution.RELATIVE_INPUT_PATH, day);
	String resultTest = Solution.filename(day, true);
	assertEquals(expResultTest, resultTest);
    }

    /**
     * Test of solve method, of class Solution.
     */
    @Test
    public void testSolve() {
	System.out.println("solve");
	Solution s = new SolutionImpl(0, "Test Name", true);
	Result result = s.solve(Solution.filename(0, true), 0);
	Result expResult = new Result("", "");
	assertEquals(expResult, result);
    }

    public class SolutionImpl extends Solution {

	public SolutionImpl(int day, String name, boolean emptyLinesIndicateMultipleInputs) {
	    super(day, name, emptyLinesIndicateMultipleInputs);
	}
    }
    
}
