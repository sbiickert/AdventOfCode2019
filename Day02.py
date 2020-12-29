import AoC

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
	program = list(map(lambda x: (int(x)), input[5].split(',')))
	
	# Instructions were to replace the first two values with 12 and 2
	program[1] = 12
	program[2] = 2
	
	last_opcode = 1
	ptr = 0
	#print(program)
	while last_opcode in [1, 2]:
		(ptr, last_opcode) = cycle_computer(ptr, program)
		#print(f'ptr: {ptr} last_opcode: {last_opcode}')
		#print(program)
	#print(f'End state: {program}')
	print(f'The value at address [0] is {program[0]}')

if __name__ == "__main__":
	main()
