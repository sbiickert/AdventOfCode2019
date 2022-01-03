//
//  Day06_UniversalOrbit.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-03.
//

import Foundation

struct UniversalOrbit: AoCSolution {
	static func solve(filename: String) {
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		print("\nDay 06 (Universal Orbit Map) -> \(filename)")
		
		let system = parseSystem(input)
		var totalOrbits = 0
		for label in system.keys {
			//print("\(label): \(system[label]!.countOrbits())")
			totalOrbits += system[label]!.countOrbits()
		}
		
		print("Part One")
		print("The total number of orbits is \(totalOrbits)")
	}
	
	static func parseSystem(_ input: [String]) -> Dictionary<String, Body> {
		var lookup = Dictionary<String, Body>()
		
		for line in input {
			let labels = line.split(separator: ")").map({String($0)})
			var bodies = [Body]()
			for label in labels {
				if lookup.keys.contains(label) == false {
					lookup[label] = Body(label)
				}
				bodies.append(lookup[label]!)
			}
			bodies[0].orbitedBy.append(bodies[1])
			bodies[1].parent = bodies[0]
		}
		return lookup
//		let centerOfMass = lookup.values.first(where: {$0.orbiting == nil})!
//		return centerOfMass
	}
	
	class Body {
		let label: String
		var parent: Body?
		var orbitedBy = [Body]()
		
		init(_ label: String) {
			self.label = label
		}
		
		func countOrbits() -> Int {
			var count = 0
			if parent != nil {
				count += 1
				count += parent!.countOrbits()
			}
			return count
		}
	}
}
