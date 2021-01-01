# Day 5: Sunny with a Chance of Asteroids

from typing import List, Tuple
import AoC

def parse_instruction(instr: int) -> Tuple[int, int, int, int]:
	parsed = [0,0,0,0]
	str_instr = str(instr)
	# Right 2 digits: opcode
	parsed[0] = int(str_instr[-2:])
	modes = list(str_instr[:-2])
	for i in range(1, 4):
		if len(modes) == 0:
			break
		parsed[i] = int(modes.pop())
	return tuple(parsed)

def get_program_value(program: List[int], value: int, mode: int) -> int:
	if mode == 1:
		# Immediate mode
		return value
	# Position mode
	return program[value]

def cycle_computer(ptr: int, program: List[int], input: int) -> Tuple[int, int, int]:
	# Read instruction at ptr
	output = 0
	in_ptr = ptr # holding for printing later
	instr = parse_instruction(program[ptr])
	opcode = instr[0]
	if opcode == 1 or opcode == 2:
		# Add (1) or Multiply (2)
		value1 = get_program_value(program, program[ptr+1], instr[1])
		value2 = get_program_value(program, program[ptr+2], instr[2])
		writeAddress = program[ptr+3]
		#print(f'[{read1Address}], [{read2Address}] -> [{writeAddress}]')
		if opcode == 1:
			action = f'write {value1} + {value2} to {writeAddress}'
			program[writeAddress] = value1 + value2
		else:
			action = f'write {value1} * {value2} to {writeAddress}'
			program[writeAddress] = value1 * value2
		ptr += 4
	elif opcode == 3:
		writeAddress = program[ptr+1]
		action = f'write {input} to address {writeAddress}'
		program[writeAddress] = input
		ptr += 2
	elif opcode == 4:
		readAddress = program[ptr+1]
		output = program[readAddress]
		action = f'read {output} from address {readAddress}'
		ptr += 2
	elif opcode == 99:
		# End program
		action = 'end program'
		ptr += 1
		pass
	else:
		ptr += 1
		action = f"Unknown opcode {opcode}"
		print(action)
	print(f'{in_ptr} -> {instr} -> {action}')
	return (ptr, opcode, output)

def main():
		program_str = AoC.read_input_file("Day05_Input.txt", True)
		program = list(map(lambda x: (int(x)), program_str[2].split(',')))
		input = 1 # the ID for the ship's air conditioner unit
		ptr = 0
		opcode = 0
		while opcode != 99:
			(ptr, opcode, output) = cycle_computer(ptr, program, input)
		print(f'Output: {output}')

if __name__ == "__main__":
	main()
