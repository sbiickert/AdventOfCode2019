//
//  Day12_NBodyProblem.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-05.
//

import Foundation
import Algorithms

struct NBodyProblem: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 12 (The N-Body Problem) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		var moons = parseMoons(input: input)
		simulate(moons, steps: 1000)
		var e = 0
		moons.forEach({e += $0.totalEnergy})
		
		print("Part One")
		print("Total energy: \(e)")
	}
	
	static func runTests(filename: String) {
		print("\nDay 12 TEST (The N-Body Problem) -> \(filename)")
		let groupedInput = AoCUtil.readGroupedInputFile(named: filename)
		
		var moons = parseMoons(input: groupedInput[0])
		simulate(moons, steps: 10)
		var e = 0
		moons.forEach({e += $0.totalEnergy})
		print("total energy: \(e)")
		
		moons = parseMoons(input: groupedInput[1])
		simulate(moons, steps: 100)
		e = 0
		moons.forEach({e += $0.totalEnergy})
		print("total energy: \(e)")
	}
	
	static func simulate(_ moons: [Moon], steps: Int) {
		for _ in 1...steps {
			// Apply gravity
			for m in moons.combinations(ofCount: 2) {
				let deltaVs = calcDeltaV(moon1: m[0], moon2: m[1])
				m[0].vel = m[0].vel + deltaVs.dv1
				m[1].vel = m[1].vel + deltaVs.dv2
			}
			
			// Apply velocity
			for moon in moons {
				moon.pos = moon.pos + moon.vel
			}
		}
	}
	
	static func calcDeltaV(moon1: Moon, moon2: Moon) -> (dv1: Coord3D, dv2: Coord3D) {
		let diff = moon1.pos - moon2.pos
		var dv1 = Coord3D.zero
		var dv2 = Coord3D.zero
		if diff.x > 0 {
			dv1.x = -1
			dv2.x = 1
		}
		else if diff.x < 0 {
			dv1.x = 1
			dv2.x = -1
		}
		if diff.y > 0 {
			dv1.y = -1
			dv2.y = 1
		}
		else if diff.y < 0 {
			dv1.y = 1
			dv2.y = -1
		}
		if diff.z > 0 {
			dv1.z = -1
			dv2.z = 1
		}
		else if diff.z < 0 {
			dv1.z = 1
			dv2.z = -1
		}
		return (dv1, dv2)
	}
	
	static func parseMoons(input: [String]) -> [Moon] {
		var result = [Moon]()
		for var line in input {
			line = line.replacingOccurrences(of: "<", with: "")
			line = line.replacingOccurrences(of: ">", with: "")
			line = line.replacingOccurrences(of: "=", with: "")
			line = line.replacingOccurrences(of: " ", with: "")
			line = line.replacingOccurrences(of: "x", with: "")
			line = line.replacingOccurrences(of: "y", with: "")
			line = line.replacingOccurrences(of: "z", with: "")
			let c = line.split(separator: ",").map({Int(String($0))!})
			let c3d = Coord3D(x: c[0], y: c[1], z: c[2])
			let m = Moon(position: c3d)
			result.append(m)
		}

		return result
	}
	
	class Moon {
		var pos = Coord3D.zero
		var vel = Coord3D.zero
		
		init() {}
		
		init(position: Coord3D) {
			self.pos = position
		}
		
		var potentialEnergy: Int {
			let pE = abs(pos.x) + abs(pos.y) + abs(pos.z)
			return pE
		}
		
		var kineticEnergy: Int {
			let kE = abs(vel.x) + abs(vel.y) + abs(vel.z)
			return kE
		}
		
		var totalEnergy: Int {
			return potentialEnergy * kineticEnergy
		}
	}
	
	struct Coord3D {
		static var zero: Coord3D {
			return Coord3D(x: 0, y: 0, z: 0)
		}
		
		var x: Int
		var y: Int
		var z: Int
		
		static func +(lhs: Coord3D, rhs: Coord3D) -> Coord3D {
			return Coord3D(x: lhs.x+rhs.x, y: lhs.y+rhs.y, z: lhs.z+rhs.z)
		}
		
		static func -(lhs: Coord3D, rhs: Coord3D) -> Coord3D {
			return Coord3D(x: lhs.x-rhs.x, y: lhs.y-rhs.y, z: lhs.z-rhs.z)
		}
	}
}
