//
//  Day10_MonitoringStation.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-04.
//

import Foundation
import Algorithms

struct MonitoringStation: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 10 (Monitoring Station) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let astroMap = Map(input: input)
		//astroMap.draw()
		let bestLocation = findBestLocation(in: astroMap)
		//print(bestLocation)
		
		print("Part One")
		print("The best location is \(bestLocation.0) and can see \(bestLocation.1) other asteroids.")

		let result = vaporizeAsteroids(from: bestLocation.0, inMap: astroMap)
		
		print("Part Two")
		print("The 200th asteroid score is \(result)")

	}
	
	static func runTests(filename: String) {
		print("\nDay 10 TEST (Monitoring Station) -> \(filename)")
		let groupedInput = AoCUtil.readGroupedInputFile(named: filename)
		
//		for input in groupedInput {
//			let astroMap = Map(input: input)
//			astroMap.draw()
//			let bestLocation = findBestLocation(in: astroMap)
//			print(bestLocation)
//		}
		
		// Vaporization
		let input = groupedInput[4]
		let astroMap = Map(input: input)
		astroMap.draw()
		let laser = Coord2D(x: 11, y: 13)
		let result = vaporizeAsteroids(from: laser, inMap: astroMap)
		
		
	}
	
	static func findBestLocation(in astroMap: Map) -> (Coord2D, Int) {
		var result: Coord2D?
		var maxVisible = 0
		for candidate in astroMap.asteroidLocations {
			let losData = analyzeLineOfSight(from: candidate, inMap: astroMap)
			if losData.count > maxVisible {
				maxVisible = losData.count
				result = candidate
			}
		}
		return (result!, maxVisible)
	}
	
	static func analyzeLineOfSight(from location: Coord2D, inMap astroMap:Map) -> Dictionary<Int, Int> {
		var result = Dictionary<Int, Int>()
		for other in astroMap.asteroidLocations {
			if location != other {
				let diff = location - other
				let bearing = astroMap.bearingLookup[diff]!
				if result.keys.contains(bearing) == false {
					result[bearing] = 0
				}
				result[bearing]! += 1
			}
		}
		return result
	}
	
	static func vaporizeAsteroids(from laser: Coord2D, inMap astroMap: Map) -> Int {
		var result = 0
		var asteroids = collectAsteroidsByBearing(from: laser, inMap: astroMap)
		let bearings = [Int](asteroids.keys.sorted())
		var vaporizedCount = 0
		var idx = bearings.firstIndex(of: 90000)! // up
		while true {
			let b = bearings[idx]
			if asteroids[b]!.count > 0 {
				let vaporized = asteroids[b]!.first!
				asteroids[b]! = Array(asteroids[b]!.dropFirst())
				vaporizedCount += 1
				//print("\(vaporizedCount): Asteroid at \(vaporized)")
				if vaporizedCount >= 200 {
					result = vaporized.x * 100 + vaporized.y
					break
				}
			}
			
			idx += 1
			if idx >= bearings.count {
				idx = 0
			}
		}
		return result
	}
	
	static func collectAsteroidsByBearing(from location: Coord2D, inMap astroMap: Map) -> Dictionary<Int, [Coord2D]> {
		var result = Dictionary<Int, [Coord2D]>()
		for other in astroMap.asteroidLocations {
			if location != other {
				let diff = location - other
				let bearing = astroMap.bearingLookup[diff]!
				if result.keys.contains(bearing) == false {
					result[bearing] = [Coord2D]()
				}
				result[bearing]!.append(other)
			}
		}
		for key in result.keys {
			result[key] = result[key]!.sorted(by: {$0.distanceFrom(location) < $1.distanceFrom(location)})
		}
		return result
	}

	class Map {
		var data: [[Bool]]
		var bearingLookup = Dictionary<Coord2D, Int>()
		
		init(input: [String]) {
			data = [[Bool]]()
			for line in input {
				var row = [Bool]()
				line.forEach({row.append(String($0) == "#" ? true : false)})
				data.append(row)
			}
			
			// Pre-cook bearings for all coordinate differences from -size to +size
			for dx in (-1 * size)...(size + 1) {
				for dy in (-1 * size)...(size + 1) {
					// Calc bearing (0 is west)
					let bearing = (atan2(Double(dy), Double(dx)) * 180 / Double.pi * 1000).rounded()
					bearingLookup[Coord2D(x: dx, y: dy)] = Int(bearing)
				}
			}
		}
		
		var size: Int {
			return data.count
		}
		
		var _asteroidLocations: [Coord2D]?
		var asteroidLocations: [Coord2D] {
			if _asteroidLocations == nil {
				_asteroidLocations = [Coord2D]()
				for (x, y) in product(0..<size, 0..<size) {
					if data[y][x] {
						_asteroidLocations!.append(Coord2D(x: x, y: y))
					}
				}
			}
			return _asteroidLocations!
		}
		
		func draw() {
			print("    \((0..<size).map({String(format: "%1d", $0 % 10)}).joined())")
			for (x, row) in data.enumerated() {
				print(String(format: "%2d: ", x) + row.map({$0 ? "#" : "."}).joined())
			}
		}
	}
	
	struct Coord2D: Equatable, Hashable {
		let x: Int
		let y: Int
		
		static func -(lhs: Coord2D, rhs: Coord2D) -> Coord2D {
			return Coord2D(x: lhs.x - rhs.x, y: lhs.y - rhs.y)
		}
		
		var angle: Int {
			let sth = atan2(Double(y), Double(y)) * 1000000
			return Int(sth)
		}
		
		func distanceFrom(_ other: Coord2D) -> Double {
			let diff = self-other
			return sqrt(Double(diff.x * diff.x + diff.y * diff.y))
		}
	}
}
