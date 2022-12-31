/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.Solution;

/**
 *
 * @author sjb
 */
public class AoC2019 {

    public static void main(String[] args) {
        System.out.println("Advent of Code 2019");
	System.out.println("Working Directory = " + System.getProperty("user.dir"));
	
	var day01 = new Day01();
	var input = Solution.inputsForSolution(day01).get(0);
	var r01 = day01.solve(input.filename(), input.index());
	System.out.println(r01);
    }
}
