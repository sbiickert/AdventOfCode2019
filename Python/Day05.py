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

def get_program_values(program: List[int], ptr: int, count: int, modes: Tuple[int, int, int]) -> List[int]:
	values = []
	for offset in range(1, count + 1):
		value = get_program_value(program, program[ptr+offset], modes[offset-1])
		values.append(value)
	return values

def cycle_computer(ptr: int, program: List[int], input: int) -> Tuple[int, int, int]:
	# Read instruction at ptr
	output = 0
	in_ptr = ptr # holding for printing later
	instr = parse_instruction(program[ptr])
	opcode = instr[0]
	modes = instr[1:]
	if opcode == 1:
		# Add
		values = get_program_values(program, ptr, 2, modes)
		writeAddress = program[ptr+3]
		action = f'write {values[0]} + {values[1]} to {writeAddress}'
		program[writeAddress] = values[0] + values[1]
		ptr += 4
	elif opcode == 2:
		# Multiply
		values = get_program_values(program, ptr, 2, modes)
		writeAddress = program[ptr+3]
		action = f'write {values[0]} + {values[1]} to {writeAddress}'
		program[writeAddress] = values[0] * values[1]
		ptr += 4
	elif opcode == 3:
		# Write input
		writeAddress = program[ptr+1]
		action = f'write {input} to address {writeAddress}'
		program[writeAddress] = input
		ptr += 2
	elif opcode == 4:
		# Read and send to output
		readAddress = ptr+1
		output = get_program_value(program, program[readAddress], modes[0])
		action = f'read {output} from address {readAddress}'
		ptr += 2
	elif opcode == 5:
		# Jump if true
		values = get_program_values(program, ptr, 2, modes)
		if values[0] > 0:
			action = f'jump {ptr} to address {values[1]}'
			ptr = values[1]
		else:
			action = 'jump if true no action'
			ptr += 3
	elif opcode == 6:
		# Jump if false
		values = get_program_values(program, ptr, 2, modes)
		if values[0] == 0:
			action = f'jump {ptr} to address {values[1]}'
			ptr = values[1]
		else:
			action = 'jump if false no action'
			ptr += 3
	elif opcode == 7:
		# Less than
		values = get_program_values(program, ptr, 2, modes)
		writeAddress = program[ptr+3]
		if values[0] < values[1]:
			program[writeAddress] = 1
		else:
			program[writeAddress] = 0
		action = f'write {program[writeAddress]} to address {writeAddress}'
		ptr += 4
	elif opcode == 8:
		# Equals
		values = get_program_values(program, ptr, 2, modes)
		writeAddress = program[ptr+3]
		if values[0] == values[1]:
			program[writeAddress] = 1
		else:
			program[writeAddress] = 0
		action = f'write {program[writeAddress]} to address {writeAddress}'
		ptr += 4
	elif opcode == 99:
		# End program
		action = 'end program'
		ptr += 1
		pass
	else:
		ptr += 1
		action = f"Unknown opcode {opcode}"
		print(action)
	#print(f'{in_ptr} -> {instr} -> {action}')
	return (ptr, opcode, output)

def main():
		program_str = AoC.read_input_file("Day05_Input.txt", True)
		program = list(map(lambda x: (int(x)), program_str[3].split(',')))
		
		#input = 1 # the ID for the ship's air conditioner unit
		input = 5 # the ID for the ship's thermal radiator controller
		print(f'Input: {input}')
		
		ptr = 0
		opcode = 0
		output = 0
		#print(program)
		
		while opcode != 99:
			(ptr, opcode, cycle_output) = cycle_computer(ptr, program, input)
			if cycle_output != 0:
				output = cycle_output
				
		print(f'Output: {output}')

if __name__ == "__main__":
	main()
