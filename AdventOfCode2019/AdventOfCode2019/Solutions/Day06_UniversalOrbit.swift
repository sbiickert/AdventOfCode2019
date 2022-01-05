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
		
		var system = parseSystem(input)
		var totalOrbits = 0
		for label in system.keys {
			//print("\(label): \(system[label]!.countOrbits())")
			totalOrbits += system[label]!.countOrbits()
		}
		
		print("Part One")
		print("The total number of orbits is \(totalOrbits)")
		
		// Altering test input for Part 2 --> doesn't contain "YOU" or "SAN"
		if filename.contains("test") {
			let y = Body("YOU")
			let s = Body("SAN")
			system["K"]!.orbitedBy.append(y)
			y.parent = system["K"]
			system["I"]!.orbitedBy.append(s)
			s.parent = system["I"]
			system[s.label] = s
			system[y.label] = y
		}
		
		let you = system["YOU"]!
		let yourParents = you.parents
		let santa = system["SAN"]!
		let santasParents = santa.parents
		let commonParents = Array(Set<Body>(yourParents).intersection(Set<Body>(santasParents))).sorted(by: {$0.countOrbits() < $1.countOrbits()})
		let commonParent = commonParents.last!
		//print("Common parent: \(commonParent.label)")
		let transferCount = yourParents.firstIndex(of: commonParent)! + santasParents.firstIndex(of: commonParent)!
		
		print("Part Two")
		print("The total number of transfers to reach Santa is \(transferCount)")
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
	}
	
	class Body: Hashable {
		static func == (lhs: UniversalOrbit.Body, rhs: UniversalOrbit.Body) -> Bool {
			return lhs.label == rhs.label
		}
		
		func hash(into hasher: inout Hasher) {
			hasher.combine(label)
		}
		
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
		
		var parents: [Body] {
			var result = [Body]()
			if parent != nil {
				result.append(parent!)
				result.append(contentsOf: parent!.parents)
			}
			return result
		}
	}
}
