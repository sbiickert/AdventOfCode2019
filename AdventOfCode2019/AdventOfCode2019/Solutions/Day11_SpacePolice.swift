//
//  Day11_SpacePolice.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-05.
//

import Foundation

struct SpacePolice: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 11 (Space Police) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		let program = IntCodeComputer.parseProgram(input[0])
		let numPaintedPanels = solvePartOne(program)
		
		print("Part One")
		print("Number of painted panels: \(numPaintedPanels)")
		
		print("Part Two")
		solvePartTwo(program)
	}
	
	private static func solvePartOne(_ program: [Int]) -> Int {
		var visited = Dictionary<Coord2D, Paint>()
		visited[Coord2D.zero] = .black
		
		let robot = Robot(program)
		robot.computer.debug = false
		var lastPositionIsPainted = false
		
		while true {
			// Input the current color of the square
			robot.computer.input.append(visited[robot.position]!.rawValue)
			// Run to first output: color to paint
			robot.computer.run(toOutput: true, resetPtr: false)
			if robot.computer.isHalted { break }
			let newPaint = Paint(rawValue: robot.computer.output[0])!
			visited[robot.position] = newPaint
			// Run to second output: direction to turn
			robot.computer.run(toOutput: true, resetPtr: false)
			let turn = Turn(rawValue: robot.computer.output[0])!
			robot.facing = (turn == .left) ? robot.facing.left : robot.facing.right
			robot.moveForward()
			
			lastPositionIsPainted = visited.keys.contains(robot.position)
			if lastPositionIsPainted == false {
				visited[robot.position] = .black
			}
		}
		
		// Number of unique painted squares, minus 1 if the last spot wasn't painted.
		return visited.count - (lastPositionIsPainted ? 0 : 1)
	}
	
	private static func solvePartTwo(_ program: [Int]) {
		var visited = Dictionary<Coord2D, Paint>()
		visited[Coord2D.zero] = .white
		
		let robot = Robot(program)
		robot.computer.debug = false
		var lastPositionIsPainted = false
		
		while true {
			// Input the current color of the square
			robot.computer.input.append(visited[robot.position]!.rawValue)
			// Run to first output: color to paint
			robot.computer.run(toOutput: true, resetPtr: false)
			if robot.computer.isHalted { break }
			let newPaint = Paint(rawValue: robot.computer.output[0])!
			visited[robot.position] = newPaint
			// Run to second output: direction to turn
			robot.computer.run(toOutput: true, resetPtr: false)
			let turn = Turn(rawValue: robot.computer.output[0])!
			robot.facing = (turn == .left) ? robot.facing.left : robot.facing.right
			robot.moveForward()
			
			lastPositionIsPainted = visited.keys.contains(robot.position)
			if lastPositionIsPainted == false {
				visited[robot.position] = .black
			}
		}
		draw(visited)
	}
	
	static func draw(_ paintRecord: Dictionary<Coord2D, Paint>) {
		var minX = Int.max
		var minY = Int.max
		var maxX = Int.min
		var maxY = Int.min
		for c in paintRecord.keys {
			if c.x < minX { minX = c.x }
			if c.y < minY { minY = c.y }
			if c.x > maxX { maxX = c.x }
			if c.y > maxY { maxY = c.y }
		}
		print("minX: \(minX) minY: \(minY) maxX: \(maxX) maxY: \(maxY) ")
		
		var grid = [[String]](repeating: [String](repeating: " ", count: maxX-minX+1), count: maxY-minY+1)
		for record in paintRecord {
			if record.value == .white {
				grid[record.key.y - minY][record.key.x - minX] = "#"
			}
		}
		for row in grid.reversed() { // My y axis is flipped. Letters were upside down.
			print(row.joined())
		}
	}
	
	class Robot {
		let computer = IntCodeComputer()
		var facing = Direction2D.U
		var position = Coord2D.zero
		
		init (_ program: [Int]) {
			computer.program = program
		}
		
		func moveForward() {
			position = position + facing.offset
		}
	}
	
	enum Paint: Int {
		case black = 0
		case white = 1
	}
	
	enum Turn: Int {
		case left = 0
		case right = 1
	}
}
