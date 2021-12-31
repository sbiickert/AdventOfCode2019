//
//  Day01_Tyranny.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2021-12-31.
//

import Foundation

struct Tyranny: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 01 (Tyranny of the Rocket Equation) -> \(filename)")

		let groupedInput = AoCUtil.readGroupedInputFile(named: filename)
		
		var totalFuel = 0
		for input in groupedInput {
			totalFuel = solveEquation(input.map({Int($0)!}), withRecursion: false)
		}
		
		print("Part 1")
		print("The answer is: \(totalFuel)")
		
		totalFuel = 0
		for input in groupedInput {
			totalFuel = solveEquation(input.map({Int($0)!}), withRecursion: true)
		}
		
		print("Part 2")
		print("The answer is: \(totalFuel)")
	}
	
	private static func solveEquation(_ input: [Int], withRecursion: Bool) -> Int {
		var total = 0
		
		for mass in input {
			if withRecursion {
				total += rCalcFuel(for: mass)
			}
			else {
				total += (mass / 3) - 2
			}
		}
		
		return total
	}
	
	private static func rCalcFuel(for mass: Int) -> Int {
		let fuelMass = (mass / 3) - 2
		if fuelMass >= 0 {
			return fuelMass + rCalcFuel(for: fuelMass)
		}
		return 0
	}
}
