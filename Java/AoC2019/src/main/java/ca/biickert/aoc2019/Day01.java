
package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day01 extends Solution {
    public Day01() {
	super(1, "The Tyranny of the Rocket Equation");
    }
    
    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);
	
	var input = InputReader.readInputFile(filename);
	
	var part1Solution = solvePartOne(input);
	var part2Solution = solvePartTwo(input);
	
	result = new Result(Integer.toString(part1Solution), Integer.toString(part2Solution));
	
	return result;
    }
    
    private int solvePartOne(List<String> input) {
	var sum = 0;
	for (var line : input) {
	    int mass = Integer.parseInt(line);
	    int fuel = fuelNeededForMass(mass);
	    sum += fuel;
	    //System.out.println(String.format("Mass: %d, Fuel: %d", mass, fuel));
	}
	return sum;
    }
    
    private int solvePartTwo(List<String> input) {
	var sum = 0;
	for (var line : input) {
	    int totalFuel = 0;
	    int mass = Integer.parseInt(line);
	    while (mass >= 0) {
		int fuel = fuelNeededForMass(mass);
		if (fuel > 0) {
		    totalFuel += fuel;
		}
		mass = fuel;
	    }
	    sum += totalFuel;
	    //System.out.println(String.format("Mass: %d, Total Fuel: %d", mass, totalFuel));
	}
	return sum;
    }
    
    private int fuelNeededForMass(int mass) {
	int fuel = Math.round(mass / 3) - 2;
	return fuel;
    }
}
