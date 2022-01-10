//
//  AoCUtil.swift
//  AdventOfCode2021
//
//  Created by Simon Biickert on 2021-11-07.
//

import Foundation

protocol AoCSolution {
	static func solve(filename: String)
}

class AoCUtil {
	public static func readInputFile(named name:String, removingEmptyLines removeEmpty:Bool) -> [String] {
		var results = [String]()
		if let inputPath = Bundle.main.path(forResource: name, ofType: "txt") {
			do {
				let input = try String(contentsOfFile: inputPath)
				results = input.components(separatedBy: "\n")
			} catch {
				print("Could not read file \(name)")
			}
		}
		if removeEmpty {
			results = results.filter { $0.count > 0 }
		}
		return results
	}
	
	public static func readGroupedInputFile(named name: String) -> [[String]] {
		var results = [[String]]()
		let lines = readInputFile(named: name, removingEmptyLines: false)
		
		var group = [String]()
		for line in lines {
			if line.count > 0 {
				group.append(line)
			}
			else {
				results.append(group)
				group = [String]()
			}
		}
		if group.count > 0 {
			results.append(group)
		}
		
		return results
	}
	
	static func rangeToArray(r: Range<Int>) -> [Int] {
		var result = [Int]()
		for i in r {
			result.append(i)
		}
		return result
	}
	
	static func cRangeToArray(r: ClosedRange<Int>) -> [Int] {
		var result = [Int]()
		for i in r {
			result.append(i)
		}
		return result
	}
}

struct Coord2D: Hashable {
	static var zero: Coord2D {
		return Coord2D(x: 0, y: 0)
	}
	
	let x: Int
	let y: Int
	
	static func +(left: Coord2D, right: Coord2D) -> Coord2D {
		let sum = Coord2D(x: left.x + right.x, y: left.y + right.y)
		return sum
	}
	
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

	var manhattan: Int {
		return abs(x) + abs(y)
	}
	
	func containedBy(xRange: Range<Int>, yRange: Range<Int>) -> Bool {
		return xRange.contains(x) && yRange.contains(y)
	}
	
	func containedBy(xClosedRange: ClosedRange<Int>, yClosedRange: ClosedRange<Int>) -> Bool {
		return xClosedRange.contains(x) && yClosedRange.contains(y)
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
	
	var left: Direction2D {
		switch self {
		case .R:
			return .U
		case .L:
			return .D
		case .U:
			return .L
		case .D:
			return .R
		}
	}
	
	var right: Direction2D {
		switch self {
		case .R:
			return .D
		case .L:
			return .U
		case .U:
			return .R
		case .D:
			return .L
		}
	}
	
	var reverse: Direction2D {
		return self.left.left
	}
}
