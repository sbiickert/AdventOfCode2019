//
//  Day15_OxygenSystem.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-10.
//

import Foundation
import Algorithms

struct OxygenSystem: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 15 (Oxygen System) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let program = IntCodeComputer.parseProgram(input[0])
		
		let droid = Droid(program: program)
		mapSpace(with: droid)
		let space = droid.map
		//drawSpace(space, droidLocation: droid.location, path: nil)
		let oxLoc = findOxygenSystem(in: space)!
		let path = findPath(from: droid.location, to: oxLoc, in: space)[0]
		//drawSpace(space, droidLocation: droid.location, path: path)

		print("Part One")
		print("The fewest movement commands to reach the oxygen system is \(path.count - 1)")
			
		let diffusionPaths = findPath(from: oxLoc, to: nil, in: space).sorted(by: {$0.count < $1.count})
		let longestPathCount = diffusionPaths.last!.count

		print("Part Two")
		print("The time needed to oxygenate the area is \(longestPathCount - 1) minutes.") // 1112 too high
	}
	
	private static func mapSpace(with droid: Droid) {
		for direction in Direction.allCases {
			let location = droid.location
			if droid.neighbour(direction) == .unknown {
				// If we're picking up the droid and putting it down in an earlier spot, need to reset its brain
				let state = droid.brain.saveState()
				if droid.move(direction) != .wall {
					mapSpace(with: droid)
					droid.location = location
					droid.brain.restore(state: state)
				}
			}
		}
	}
	
	// Made endLoc optional for Part 2, because we want to return all paths.
	// When endLoc is non-nil, result[0] is the shortest path to endLoc.
	private static func findPath(from startLoc: Coord2D, to endLoc: Coord2D?, in space: [[Mapped]]) -> [[Coord2D]] {
		var explored = Set<Coord2D>()
		var paths = Dictionary<Coord2D, [Coord2D]>()
		var q = [Coord2D]()
		
		paths[startLoc] = [startLoc]
		q.append(startLoc)
		
		while q.isEmpty == false {
			let c = q.removeFirst()
			let pathToCoord = paths[c]!
			if let endLoc = endLoc,
			   c == endLoc {
				return [pathToCoord]
			}
			explored.insert(c)
			for dir in Direction.allCases {
				let nc = c + dir.offset
				if space[nc.y][nc.x] != .wall && explored.contains(nc) == false {
					q.append(nc)
					var pathToN = pathToCoord
					pathToN.append(nc)
					paths[nc] = pathToN
				}
			}
		}
		return Array(paths.values)
	}
	
	enum Mapped: Int {
		case wall = 0
		case empty = 1
		case oxygenSystem = 2
		case unknown = 3
		case droid = -1
		case path = -2
		
		var character: String {
			switch self {
			case .wall:
				return "#"
			case .empty:
				return " "
			case .oxygenSystem:
				return "O"
			case .unknown:
				return "~"
			case .droid:
				return "D"
			case .path:
				return "."
			}
		}
	}
	
	enum Direction: Int, CaseIterable {
		case north = 1
		case south = 2
		case west = 3
		case east = 4
		
		var offset: Coord2D {
			switch self {
			case .east:
				return Coord2D(x: 1, y: 0)
			case .west:
				return Coord2D(x: -1, y: 0)
			case .north:
				return Coord2D(x: 0, y: 1)
			case .south:
				return Coord2D(x: 0, y: -1)
			}
		}
	}
	
	class Droid {
		static let MAP_SIZE = 50 // Determined by test runs to be big enough
		
		let brain: IntCodeComputer
		var map: [[Mapped]]
		var location: Coord2D
		
		init(program: [Int]) {
			brain = IntCodeComputer(program)
			map = [[Mapped]](repeating: [Mapped](repeating: .unknown, count: Droid.MAP_SIZE), count: Droid.MAP_SIZE)
			location = Coord2D(x: Droid.MAP_SIZE / 2, y: Droid.MAP_SIZE / 2)
			self.setSpace(at: location, info: .empty) // Starting point
		}
		
		func move(_ dir: Direction) -> Mapped {
			brain.input.append(dir.rawValue)
			brain.run(toOutput: true, resetPtr: false)
			let result = Mapped(rawValue: brain.output[0])!
			let nCoord = location + dir.offset
			switch result {
			case .wall:
				setSpace(at: nCoord, info: .wall)
			case .empty:
				location = nCoord
				setSpace(at: location, info: .empty)
			case .oxygenSystem:
				location = nCoord
				setSpace(at: location, info: .oxygenSystem)
			default:
				print("Got \(result) back from move. WHY?")
			}
			return result
		}
		
		func getSpace(at coord: Coord2D) -> Mapped {
			guard coord.containedBy(xRange: (0..<Droid.MAP_SIZE), yRange: (0..<Droid.MAP_SIZE)) else { return .wall }
			return map[coord.y][coord.x]
		}
		
		func setSpace(at coord: Coord2D, info: Mapped) {
			assert(coord.containedBy(xRange: (0..<Droid.MAP_SIZE), yRange: (0..<Droid.MAP_SIZE)))
			map[coord.y][coord.x] = info
		}
		
		func neighbour(_ dir: Direction) -> Mapped {
			let nCoord = location + dir.offset
			return getSpace(at: nCoord)
		}
	}
	
	private static func findOxygenSystem(in space: [[Mapped]]) -> Coord2D? {
		for (x, y) in product(0..<space[0].count, 0..<space.count) {
			if space[y][x] == .oxygenSystem {
				return Coord2D(x: x, y: y)
			}
		}
		return nil
	}
	
	private static func drawSpace(_ space: [[Mapped]],
								  droidLocation: Coord2D,
								  path: [Coord2D]?) {
		for (y, var row) in space.enumerated() {
			if let p = path {
				for x in 0..<row.count {
					if row[x] != .oxygenSystem && p.contains(Coord2D(x: x, y: y)) {
						row[x] = .path
					}
				}
			}
			if y == droidLocation.y {
				row[droidLocation.x] = .droid
			}
			print(row.map({$0.character}).joined())
		}
	}
}
