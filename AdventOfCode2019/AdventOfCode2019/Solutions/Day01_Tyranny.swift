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

		let groupedInput = AOCUtil.readGroupedInputFile(named: filename)
		
		var totalFuel = 0
		for input in groupedInput {
			totalFuel = solveEquation(input.map({Int($0)!}))
		}
		
		print("Part 1")
		print("The answer is: \(totalFuel)")
	}
	
	private static func solveEquation(_ input: [Int]) -> Int {
		var total = 0
		
		for mass in input {
			total += (mass / 3) - 2
		}
		
		return total
	}
}
