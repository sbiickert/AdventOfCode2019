# Day 2: 1202 Program Alarm

import AoC

TARGET_SOLUTION = 19690720

def cycle_computer(ptr, program):
	# Read opcode at ptr
	opcode = program[ptr]
	if opcode == 1 or opcode == 2:
		# Add (1) or Multiply (2)
		read1Address = program[ptr+1]
		read2Address = program[ptr+2]
		writeAddress = program[ptr+3]
		value1 = program[read1Address]
		value2 = program[read2Address]
		#print(f'[{read1Address}], [{read2Address}] -> [{writeAddress}]')
		if opcode == 1:
			program[writeAddress] = value1 + value2
		else:
			program[writeAddress] = value1 * value2
		ptr += 4
	elif opcode == 99:
		# End program
		pass
	else:
		print(f"Unknown opcode {opcode}")
	return (ptr, opcode)

def main():
	input = AoC.read_input_file("Day02_Input.txt", True)
	master_program = list(map(lambda x: (int(x)), input[5].split(',')))
	
	solution = 0
	while solution != TARGET_SOLUTION:
		for noun in range(0, 100):
			for verb in range(0, 100):
				program = master_program.copy()
				# Part 1 instructions were to replace the first two values with 12 and 2
				program[1] = noun
				program[2] = verb
				
				last_opcode = 1
				ptr = 0
				#print(program)
				while last_opcode in [1, 2]:
					(ptr, last_opcode) = cycle_computer(ptr, program)
					#print(f'ptr: {ptr} last_opcode: {last_opcode}')
					#print(program)
				#print(f'End state: {program}')
				solution = program[0]
				print(f'The value at address [0] is {solution}')
				if solution == TARGET_SOLUTION:
					break
			if solution == TARGET_SOLUTION:
				break
	
	print(f'100 * {noun} + {verb} = {100 * noun + verb}')
		

if __name__ == "__main__":
	main()
