//
//  Day13_CarePackage.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-06.
//

import Foundation

struct CarePackage: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 13 (Care Package) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let program = IntCodeComputer.parseProgram(input[0])
		let numTiles = solvePartOne(program)
		
		print("Part One")
		print("The number of drawn tiles is \(numTiles)")
		
		let score = solvePartTwo(program)
		
		print("Part Two")
		print("The score after all tiles are broken is \(score)")
	}
	
	static func solvePartOne(_ program: [Int]) -> Int {
		let cab = ArcadeCabinet()
		cab.program = program
		cab.run()
		cab.draw()
		let s = cab.screen
		var blockCount = 0
		for row in s {
			blockCount += row.filter({$0 == .block}).count
		}
		return blockCount
	}

	static func solvePartTwo(_ program: [Int]) -> Int {
		let cab = ArcadeCabinet()
		cab.program = program
		cab.program[0] = 2
		cab.run()
		cab.draw()
		return cab.score
	}

	class ArcadeCabinet {
		let computer = IntCodeComputer()
		var program = [Int]()
		private var _tiles = Dictionary<Coord2D, Tile>()
		var score = 0
		
		init() {}
		
		func run() {
			computer.program = program
			computer.input.append(0) // neutral joystick
			_tiles.removeAll()
			var drawing = false
			while true {
				computer.input.removeAll()
				computer.input.append(joystickPosition)
				// Get x
				computer.run(toOutput: true, resetPtr: false)
				if computer.isHalted { break }
				let x = computer.output[0]
				// Get y
				computer.run(toOutput: true, resetPtr: false)
				let y = computer.output[0]
				// Get tile or score
				computer.run(toOutput: true, resetPtr: false)
				if x == -1 && y == 0 {
					// Get score
					score = computer.output[0]
					print(score)
					//drawing = true
				}
				else {
					// Get tile
					let c = Coord2D(x: x, y: y)
					let tile = Tile(rawValue: computer.output[0])
					_tiles[c] = tile
					if tile == .paddle { _lastPaddleLocation = c }
					if tile == .ball { _lastBallLocation = c }
				}
				if drawing {
					draw()
					sleep(1)
				}
			}
		}
		
		var joystickPosition: Int {
			if let ball = _lastBallLocation,
			   let paddle = _lastPaddleLocation {
				if ball.x < paddle.x {
					return -1
				}
				if ball.x > paddle.x {
					return 1
				}
				return 0
			}
			return 0
		}
		
		var _lastBallLocation: Coord2D?
		var _lastPaddleLocation: Coord2D?
		
		var screen: [[Tile]] {
			let bounds = getBounds(Array(_tiles.keys))
			var s = [[Tile]](repeating: [Tile](repeating: .empty, count: bounds.max.x + 1), count: bounds.max.y+1)
			for (coord, tile) in _tiles {
				s[coord.y][coord.x] = tile
			}
			return s
		}
		
		func draw() {
			let s = screen
			let j = joystickPosition
			print("Score: \(score)  Joystick: \(j)")
			for row in s {
				print(row.map({$0.character}).joined())
			}
		}
	}
	
	static func getBounds(_ coords: [Coord2D]) -> (min: Coord2D, max: Coord2D) {
		var minX = Int.max
		var minY = Int.max
		var maxX = Int.min
		var maxY = Int.min
		for c in coords {
			if c.x < minX { minX = c.x }
			if c.y < minY { minY = c.y }
			if c.x > maxX { maxX = c.x }
			if c.y > maxY { maxY = c.y }
		}
		return (Coord2D(x: minX, y: minY), Coord2D(x: maxX, y: maxY))
	}
	
	enum Tile: Int {
		case empty = 0
		case wall = 1
		case block = 2
		case paddle = 3
		case ball = 4
		
		var character: String {
			switch self {
			case .empty:
				return " "
			case .wall:
				return "|"
			case .block:
				return "X"
			case .paddle:
				return "—"
			case .ball:
				return "O"
			}
		}
	}
}
