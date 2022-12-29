//
//  Day05_SunnyAsteroids.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-02.
//

import Foundation

struct SunnyAsteroids: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 05 (Sunny w/a chance of asteroids) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let computer = IntCodeComputer()
		let program = input[0].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(1)
		computer.run()
		print("Part One")
		print("IntCode output: \(computer.output)")
		
		computer.program = program
		computer.input.append(5)
		computer.run()
		print("Part Two")
		print("The thermal radiator diagnostic code: \(computer.output[0])")
	}
	
	static func runTests(filename: String) {
		print("\nDay 05 TESTS (Sunny w/a chance of asteroids) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let computer = IntCodeComputer()
		
		// Multiply test
		var program = input[0].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.run()
		print(computer.program)
		
		// Input output test
		program = input[1].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(69)
		computer.run()
		print("69 went in, what comes out? \(computer.output[0])")

		// Add negative number
		program = input[2].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.run()
		print(computer.program)

		// Input == 8
		program = input[3].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(8)
		computer.run()
		print("is 8 equal to 8? \(computer.output[0])")
		computer.program = program
		computer.input.append(9)
		computer.run()
		print("is 9 equal to 8? \(computer.output[0])")

		// Input < 8
		program = input[4].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(7)
		computer.run()
		print("is 7 less than 8? \(computer.output[0])")
		computer.program = program
		computer.input.append(8)
		computer.run()
		print("is 8 less than 8? \(computer.output[0])")

		// Input == 8
		program = input[5].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(8)
		computer.run()
		print("is 8 equal to 8? \(computer.output[0])")
		computer.program = program
		computer.input.append(9)
		computer.run()
		print("is 9 equal to 8? \(computer.output[0])")

		// Input < 8
		program = input[6].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(7)
		computer.run()
		print("is 7 less than 8? \(computer.output[0])")
		computer.program = program
		computer.input.append(8)
		computer.run()
		print("is 8 less than 8? \(computer.output[0])")

		// Input non-zero
		program = input[7].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(7)
		computer.run()
		print("is 7 non-zero? \(computer.output[0])")
		computer.program = program
		computer.input.append(0)
		computer.run()
		print("is 0 non-zero? \(computer.output[0])")

		program = input[8].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(7)
		computer.run()
		print("is 7 non-zero? \(computer.output[0])")
		computer.program = program
		computer.input.append(0)
		computer.run()
		print("is 0 non-zero? \(computer.output[0])")

		// Less than, equal to or greater than 8
		program = input[9].split(separator: ",").map({Int(String($0))!})
		computer.program = program
		computer.input.append(7)
		computer.run()
		print("is 7 less than 8? \(computer.output[0])")
		computer.program = program
		computer.input.append(8)
		computer.run()
		print("is 8 equal to 8? \(computer.output[0])")
		computer.program = program
		computer.input.append(9)
		computer.run()
		print("is 9 greater than 8? \(computer.output[0])")
	}
	
	class IntCodeComputer {
		var program = [Int]()
		
		var input = [Int]()
		var output = [Int]()
		
		init() {}
		
		func run() {
			var ptr = 0
			output.removeAll()
			
			var bExit = false
			
			while !bExit {
				let instr = Instruction(program[ptr])
				
				switch instr.op {
				case .exit:
					bExit = true
				case .add:
					let p = readParams(count: 2)
					let result = p[1] + p[2]
					let o = program[ptr+3]
					program[o] = result
					ptr += instr.op.size
				case .mul:
					let p = readParams(count: 2)
					let result = p[1] * p[2]
					let o = program[ptr+3]
					program[o] = result
					ptr += instr.op.size
				case .inp:
					let i1 = input.removeFirst()
					let o = program[ptr+1]
					program[o] = i1
					ptr += instr.op.size
				case .out:
					let p = readParams(count: 1)
					output.append(p[1])
					ptr += instr.op.size
				case .jmpT:
					let p = readParams(count: 2)
					if p[1] != 0 {
						ptr = p[2]
					}
					else {
						ptr += instr.op.size
					}
				case .jmpF:
					let p = readParams(count: 2)
					if p[1] == 0 {
						ptr = p[2]
					}
					else {
						ptr += instr.op.size
					}
				case .lt:
					let p = readParams(count: 2)
					let o = program[ptr+3]
					program[o] = (p[1] < p[2]) ? 1 : 0
					ptr += instr.op.size
				case .eq:
					let p = readParams(count: 2)
					let o = program[ptr+3]
					program[o] = (p[1] == p[2]) ? 1 : 0
					ptr += instr.op.size
				}
				
				func readParams(count: Int) -> [Int] {
					// Get the parameters
					var p = [-1] //params are indexed on 1. Putting -1 in the zero.
					for i in 1...count {
						let mode = instr.pModes[i]
						if mode == .pos {
							p.append(program[program[ptr+i]])
						}
						else { //.imm
							p.append(program[ptr+i])
						}
					}
					return p
				}
			}
		}
		
		struct Instruction {
			let op: OpCode
			let pModes: [ParamMode]
			
			/*
			 ABCDE
			  1002

			 DE - two-digit opcode,      02 == opcode 2
			  C - mode of 1st parameter,  0 == position mode
			  B - mode of 2nd parameter,  1 == immediate mode
			  A - mode of 3rd parameter,  0 == position mode,
											   omitted due to being a leading zero
			 */
			init(_ code:Int) {
				let codeStr = String(format: "%05d", code)
				let opCodeValue = Int(codeStr.suffix(2))!
				op = OpCode(rawValue: opCodeValue)!
				var params = codeStr.dropLast(2)
				var modes = [ParamMode.pos] // params start at index 1. Filling index 0 with .pos
				while params.count > 0 {
					let mode = Int(params.suffix(1))!
					modes.append(ParamMode(rawValue: mode)!)
					params = params.dropLast(1)
				}
				pModes = modes
			}
		}
		
		enum OpCode: Int {
			case exit = 99
			case add = 1
			case mul = 2
			case inp = 3
			case out = 4
			case jmpT = 5
			case jmpF = 6
			case lt = 7
			case eq = 8
			
			var size: Int {
				switch self {
				case .exit:
					return 1
				case .inp, .out:
					return 2
				case .jmpT, .jmpF:
					return 3
				default:
					return 4
				}
			}
		}
		
		enum ParamMode: Int {
			case imm = 1
			case pos = 0
		}
	}
}
