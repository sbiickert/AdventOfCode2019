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
		
		moons = parseMoons(input: input)
		let nSteps = calculateToRepeat(moons)
		
		print("Part Two")
		print("Number of steps to repeat: \(nSteps)")
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
		
		moons = parseMoons(input: groupedInput[0])
		var nSteps = simulateToRepeat(moons)
		print("Number of steps to repeat: \(nSteps)")

		moons = parseMoons(input: groupedInput[1])
		nSteps = calculateToRepeat(moons)
		print("Number of steps to repeat: \(nSteps)")
	}
	
	private static func simulate(_ moons: [Moon], steps: Int) {
		for _ in 1...steps {
			simulateStep(moons)
		}
	}
	
	private static func simulateToRepeat(_ moons: [Moon]) -> Int {
		var steps = 0
		
		var saved = [Moon]()
		for moon in moons {
			saved.append(moon.copy())
		}
		
		while steps == 0 || moons != saved {
			steps += 1
			simulateStep(moons)
		}
		
		return steps
	}
	
	private static func calculateToRepeat(_ moons: [Moon]) -> Int {
		let cycles = analyzeCycles(moons)
		print(cycles)
		var allCycles = Set<Int>()
		for moon in cycles {
			for cycle in moon {
				allCycles.insert(cycle)
			}
		}
		let repeatCycles = Array(Array(allCycles).sorted().reversed())
		print(repeatCycles)
		var maxSync = 0
		var jump = repeatCycles[maxSync]
		var ts = jump
		var remainders = repeatCycles.map({ts % $0})
		
		while true {
			var bingo = true
			remainders[0...maxSync+1].forEach({bingo = bingo && $0 == 0})
			//if remainders 0...maxSync+1 are all zeros
			if bingo {
				maxSync += 1
				remainders.forEach({bingo = bingo && $0 == 0})
				if bingo {
					// All remainders are zeros
					print("Found future state at time \(ts)")
					break
				}
				if maxSync < repeatCycles.count - 2 {
					// Don't want to change the jump for the last digit
					jump *= repeatCycles[maxSync]
					print("Jump changed to \(jump)")
				}
			}
			
			ts += jump
			remainders = repeatCycles.map({ts % $0})
		}
		return ts
	}
	
	private static func simulateStep(_ moons: [Moon]) {
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
	
	private static func analyzeCycles(_ moons: [Moon]) -> [[Int]] {
		// Set up variables to log progress
		var cycles = [[Int]](repeating: [Int](repeating: 0, count: 6), count: moons.count)
		var tracking = [[[Int]]]()
		for _ in moons {
			let track = [[Int]](repeating: [Int](), count: 6)
			tracking.append(track)
		}
		
		let analyzeAfter = 10000
		var allCyclesMeasured = false
		var cycle = 0
		while allCyclesMeasured == false {
			for (m, moon) in moons.enumerated() {
				for axis in 0..<3 {
					if cycles[m][axis] == 0 {
						tracking[m][axis].append(moon.pos[axis])
					}
					if cycles[m][axis+3] == 0 {
						tracking[m][axis+3].append(moon.vel[axis])
					}
				}
			}
			cycle += 1
			if cycle % analyzeAfter == 0 {
				//print("cycle \(cycle). Progress:\n \(cycles)")
				// Analyze for cycling. Has to be an even number of cycles.
				for (m, _) in moons.enumerated() {
					for i in 0..<6 {
						if cycles[m][i] == 0 {
							// Returns 0 if not found
							let c = findCycle(track: tracking[m][i], startAt: cycle - analyzeAfter + 2)
							if c > 0 {
								//print("Moon \(m) axis \(i) cycled after \(c) steps.")
								cycles[m][i] = c
							}
						}
					}
				}
				// Are we done?
				allCyclesMeasured = true
				for m in 0..<moons.count {
					if cycles[m].contains(0) {
						allCyclesMeasured = false
						break
					}
				}
			}
			simulateStep(moons)
		}
		return cycles
	}
	
	private static func findCycle(track: [Int], startAt: Int) -> Int {
		assert(track.count % 2 == 0)
		for i in stride(from: startAt, to: track.count, by: 2) {
			let half = i / 2
			let front = track[0..<half]
			let back = track[half..<(half+front.count)]
			if front == back {
				return half
			}
		}
		return 0
	}
	
	private static func calcDeltaV(moon1: Moon, moon2: Moon) -> (dv1: Coord3D, dv2: Coord3D) {
		let diff = moon1.pos - moon2.pos
		var dv1 = Coord3D.zero
		var dv2 = Coord3D.zero
		
		for dimension in 0...2 {
			if diff[dimension] > 0 {
				dv1[dimension] = -1
				dv2[dimension] = 1
			}
			else if diff[dimension] < 0 {
				dv1[dimension] = 1
				dv2[dimension] = -1
			}
		}
		return (dv1, dv2)
	}
	
	private static func parseMoons(input: [String]) -> [Moon] {
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
	
	class Moon: Equatable {
		static func == (lhs: NBodyProblem.Moon, rhs: NBodyProblem.Moon) -> Bool {
			return lhs.pos == rhs.pos && lhs.vel == rhs.vel
		}
		
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
				
		func copy() -> Moon {
			let result = Moon(position: self.pos)
			result.vel = self.vel
			return result
		}
	}
	
	struct Coord3D: Equatable {
		static var zero: Coord3D {
			return Coord3D(x: 0, y: 0, z: 0)
		}
		
		var x: Int
		var y: Int
		var z: Int
		
		subscript(dimension: Int) -> Int {
			get {
				assert(0 <= dimension && dimension <= 2)
				if dimension == 0 {
					return x
				}
				if dimension == 1 {
					return y
				}
				return z
			}
			set {
				assert(0 <= dimension && dimension <= 2)
				if dimension == 0 {
					x = newValue
				}
				if dimension == 1 {
					y = newValue
				}
				if dimension == 2 {
					z = newValue
				}
			}
		}
		
		static func +(lhs: Coord3D, rhs: Coord3D) -> Coord3D {
			return Coord3D(x: lhs.x+rhs.x, y: lhs.y+rhs.y, z: lhs.z+rhs.z)
		}
		
		static func -(lhs: Coord3D, rhs: Coord3D) -> Coord3D {
			return Coord3D(x: lhs.x-rhs.x, y: lhs.y-rhs.y, z: lhs.z-rhs.z)
		}
	}
}
