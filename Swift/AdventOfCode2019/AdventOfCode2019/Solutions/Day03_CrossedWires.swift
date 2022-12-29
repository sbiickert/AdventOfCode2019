//
//  Day03_CrossedWires.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-01.
//

import Foundation
import Algorithms

struct CrossedWires: AoCSolution {
	static func runTests(filename: String) {
		print("\nDay 03 TESTS (1202 Program Alarm) -> \(filename)")
		let groupedInput = AoCUtil.readGroupedInputFile(named: filename)
		
		for input in groupedInput {
			let wire0 = Wire(defn: input[0])
			let wire1 = Wire(defn: input[1])
			let manhattanSorted = Array(Set<Coord2D>(wire0.coords).intersection(Set<Coord2D>(wire1.coords)))
				.sorted(by: {$0.manhattan < $1.manhattan}).dropFirst() //first is 0,0
			let closest = manhattanSorted.first!
			print("Manhattan \(closest): \(closest.manhattan)")

			var minSignalDistance = Int.max
			for coord in manhattanSorted {
				let s0 = wire0.signalDistance(to: coord)
				let s1 = wire1.signalDistance(to: coord)
				minSignalDistance = min(minSignalDistance, (s0+s1))
			}
			print("Signal: \(minSignalDistance)")
		}
	}
	
	static func solve(filename: String) {
		print("\nDay 03 (Crossed Wires) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let wire0 = Wire(defn: input[0])
		let wire1 = Wire(defn: input[1])
		let commonCoords = Array(Set<Coord2D>(wire0.coords).intersection(Set<Coord2D>(wire1.coords)))
		let manhattanSorted = commonCoords.sorted(by: {$0.manhattan < $1.manhattan}).dropFirst() //first is 0,0
		let closest = manhattanSorted.first!
		
		print("Part 1")
		print("The Manhattan distance is: \(closest.manhattan)")

		var minSignalDistance = Int.max
		for coord in manhattanSorted {
			let s0 = wire0.signalDistance(to: coord)
			let s1 = wire1.signalDistance(to: coord)
			minSignalDistance = min(minSignalDistance, (s0+s1))
		}
		
		print("Part 2")
		print("The signal distance is: \(minSignalDistance)")
	}
	
	
	struct Wire {
		let coords: [Coord2D]
		
		init(defn: String) {
			var pos = Coord2D(x: 0, y: 0)
			var path = [Coord2D]()
			path.append(pos)
			
			let steps = defn.split(separator: ",")
			for step in steps {
				let dir = Direction2D(rawValue: String(step.first!))!
				let dist = Int(step.dropFirst())!
				for _ in 0..<dist {
					pos = pos + dir.offset
					path.append(pos)
				}
			}
			coords = path
		}
		
		func signalDistance(to coord: Coord2D) -> Int {
			let d = coords.firstIndex(of: coord)!
			return d
		}
	}
	
	struct Coord2D: Hashable {
		let x: Int
		let y: Int
		
		static func +(left: Coord2D, right: Coord2D) -> Coord2D {
			let sum = Coord2D(x: left.x + right.x, y: left.y + right.y)
			return sum
		}
		
		var manhattan: Int {
			return abs(x) + abs(y)
		}
	}
	
	enum Direction2D: String {
		case R = "R"
		case L = "L"
		case U = "U"
		case D = "D"
		
		var offset: Coord2D {
			switch self {
			case .R:
				return Coord2D(x: 1, y: 0)
			case .L:
				return Coord2D(x: -1, y: 0)
			case .U:
				return Coord2D(x: 0, y: 1)
			case .D:
				return Coord2D(x: 0, y: -1)
			}
		}
	}
}
