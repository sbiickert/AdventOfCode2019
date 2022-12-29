//
//  Day02_ProgramAlarm.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2021-12-31.
//

import Foundation
import Algorithms

struct ProgramAlarm: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 02 (1202 Program Alarm) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		var program = input[0].split(separator: ",").map({Int(String($0))!})
		program[1] = 12
		program[2] = 2
		
		let computer = IntCodeComputer()
		computer.program = program
		computer.run()
		let output = computer.program
		//print(output)
		let answer = output[0]
		
		print("Part 1")
		print("The answer is: \(answer)")
		
		var pt2 = 0
		for (noun, verb) in product(0...99, 0...99) {
			program[1] = noun
			program[2] = verb
			computer.program = program
			computer.run()
			let output = computer.program
			if output[0] == 19690720 {
				pt2 = 100 * noun + verb
				break
			}
		}
		
		print("Part 2")
		print("The answer is: \(pt2)")

	}
	
	static func runTests(filename: String) {
		print("\nDay 02 TESTS (1202 Program Alarm) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let computer = IntCodeComputer()
		for line in input {
			computer.program = line.split(separator: ",").map({Int(String($0))!})
			computer.run()
			let output = computer.program
			print(output)
		}
	}

	class IntCodeComputer {
		var program = [Int]()
		init() {}
		
		func run() {
			var ptr = 0
			
			var bExit = false
			
			while !bExit {
				let op = OpCode(rawValue: program[ptr])!
				switch op {
				case .exit:
					bExit = true
				case .add:
					let i1 = program[program[ptr+1]]
					let i2 = program[program[ptr+2]]
					let o1 = program[ptr+3]
					let result = i1 + i2
					program[o1] = result
					ptr += op.size
				case .mul:
					let i1 = program[program[ptr+1]]
					let i2 = program[program[ptr+2]]
					let o1 = program[ptr+3]
					let result = i1 * i2
					program[o1] = result
					ptr += op.size
				}
			}
		}
		
		enum OpCode: Int {
			case exit = 99
			case add = 1
			case mul = 2
			
			var size: Int {
				switch self {
				case .exit:
					return 0
				default:
					return 4
				}
			}
		}
	}
}
