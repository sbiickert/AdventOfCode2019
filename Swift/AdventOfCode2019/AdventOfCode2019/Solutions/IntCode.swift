//
//  IntCode.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-05.
//

import Foundation

class IntCodeComputer {
	static func parseProgram(_ value: String) -> [Int] {
		return value.split(separator: ",").map({Int(String($0))!})
	}
	
	// State
	var program = [Int]()
	var input = [Int]()
	var output = [Int]()
	var ptr = 0
	var isHalted = false
	var relativeBase = 0
	
	// For writing out readMem, writeMem, etc.
	var debug = false
	
	init() {}
	
	init(_ program: [Int]) {
		self.program = program
	}
	
	func readMem(_ address: Int) -> Int {
		assert( address >= 0 )
		if address >= program.count {
			// Resize memory to accomodate
			let newMem = [Int](repeating: 0, count: address - (program.count-1))
			program.append(contentsOf: newMem)
		}
		if debug { print("read \(program[address]) from [\(address)]") }
		return program[address]
	}
	
	func writeMem(_ address: Int, value: Int) {
		assert( address >= 0 )
		if address >= program.count {
			// Resize memory to accomodate
			let newMem = [Int](repeating: 0, count: address - (program.count-1))
			program.append(contentsOf: newMem)
		}
		if debug { print("write \(value) to [\(address)]") }
		program[address] = value
	}

	func run(toOutput: Bool, resetPtr: Bool) {
		if resetPtr {
			ptr = 0
			relativeBase = 0
		}
		isHalted = false
		output.removeAll()
		
		var bExit = false
		
		while !bExit {
			let instr = Instruction(readMem(ptr))
			if debug {
				print("\(ptr)--> \(instr.description) \(program[ptr..<ptr+instr.op.size])")
			}
			
			switch instr.op {
			case .exit:
				bExit = true
				isHalted = true
			case .add:
				let p = readParams(count: 2)
				let result = p[1] + p[2]
				var o = readMem(ptr+3)
				if instr.pModes[3] == .rel { o += relativeBase }
				writeMem(o, value: result)
				ptr += instr.op.size
			case .mul:
				let p = readParams(count: 2)
				let result = p[1] * p[2]
				var o = readMem(ptr+3)
				if instr.pModes[3] == .rel { o += relativeBase }
				writeMem(o, value: result)
				ptr += instr.op.size
			case .inp:
				let i1 = input.removeFirst()
				var o = readMem(ptr+1)
				if instr.pModes[1] == .rel { o += relativeBase }
				writeMem(o, value: i1)
				ptr += instr.op.size
			case .out:
				let p = readParams(count: 1)
				output.append(p[1])
				ptr += instr.op.size
				if toOutput {
					bExit = true
				}
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
				var o = readMem(ptr+3)
				if instr.pModes[3] == .rel { o += relativeBase }
				writeMem(o, value: (p[1] < p[2]) ? 1 : 0)
				ptr += instr.op.size
			case .eq:
				let p = readParams(count: 2)
				var o = readMem(ptr+3)
				if instr.pModes[3] == .rel { o += relativeBase }
				writeMem(o, value: (p[1] == p[2]) ? 1 : 0)
				ptr += instr.op.size
			case .rel:
				let p = readParams(count: 1)
				relativeBase += p[1]
				if debug { print("relative base now \(relativeBase)")}
				ptr += instr.op.size
			}
			
			func readParams(count: Int) -> [Int] {
				// Get the parameters
				var p = [-1] //params are indexed on 1. Putting -1 in the zero.
				for i in 1...count {
					let mode = instr.pModes[i]
					if mode == .pos {
						p.append(readMem(readMem(ptr+i)))
					}
					else if mode == .rel {
						p.append(readMem(relativeBase + readMem(ptr+i)))
					}
					else { //.imm
						p.append(readMem(ptr+i))
					}
				}
				return p
			}
		}
	}
	
	func saveState() -> (program: [Int], input: [Int], output: [Int], ptr: Int, isHalted: Bool, relativeBase: Int) {
		return (program, input, output, ptr, isHalted, relativeBase)
	}
	
	func restore(state: (program: [Int], input: [Int], output: [Int], ptr: Int, isHalted: Bool, relativeBase: Int)) {
		self.program = state.program
		self.input = state.input
		self.output = state.output
		self.ptr = state.ptr
		self.isHalted = state.isHalted
		self.relativeBase = state.relativeBase
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
		
		var description: String {
			return "\(op)\(pModes.dropFirst().map({$0.rawValue}))"
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
		case rel = 9
		
		var size: Int {
			switch self {
			case .exit:
				return 1
			case .inp, .out, .rel:
				return 2
			case .jmpT, .jmpF:
				return 3
			default:
				return 4
			}
		}
	}
	
	enum ParamMode: Int {
		case pos = 0
		case imm = 1
		case rel = 2
	}
}
