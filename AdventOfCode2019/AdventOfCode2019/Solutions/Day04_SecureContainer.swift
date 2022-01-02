//
//  Day04_SecureContainer.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-01.
//

import Foundation

struct SecureContainer {
	static let range = 359282...820401
	static let powers = [100000, 10000, 1000, 100, 10, 1]
	
	static func solve() {
		print("\nDay 04 (Secure Container) -> \(range)")

		var part1ValidCount = 0
		var part2ValidCount = 0
		
		var password = [Int](repeating: 0, count: 6)
		for i in range {
			// Turn integer into an array of digits
			for place in AoCUtil.cRangeToArray(r: 0...5) {
				password[place] = i / powers[place] % 10
			}
			
			if isValidForPart1(password) { part1ValidCount += 1 }
			if isValidForPart2(password) { part2ValidCount += 1 }
		}
		
		print("Part 1")
		print("The number of potential passwords is: \(part1ValidCount)")
		
		print("Part 2")
		print("The number of potential passwords is: \(part2ValidCount)")

	}
	
	static func isValidForPart1(_ password: [Int]) -> Bool {
		var bFoundTwo = false
		var prev = password[0]
		for i in 1...5 {
			let curr = password[i]
			if curr < prev {
				return false
			}
			if curr == prev {
				bFoundTwo = true
			}
			prev = curr
		}
		return bFoundTwo
	}
	
	static func isValidForPart2(_ password: [Int]) -> Bool {
		var prev = password[0]
		for i in 1...5 {
			let curr = password[i]
			if curr < prev {
				return false
			}
			prev = curr
		}
		
		// Look for pairs of two that aren't runs of three
		var runningCount = 1
		prev = password[0]
		for i in 1...5 {
			let curr = password[i]
			if prev == curr {
				runningCount += 1
			}
			if prev != curr {
				if runningCount == 2 {
					return true
				}
				runningCount = 1
			}
			prev = curr
		}
		
		return runningCount == 2
	}
}
