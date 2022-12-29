from typing import List, Tuple

class IntcodeComputer:
	
	def __init__(self, input: List[int], program: List[int]):
		self.program = program
		self.ptr = 0
		self.input = input
		self.output = 0
		self.is_halted = False
	
	@staticmethod
	def parse_instruction(instr: int) -> Tuple[int, ...]:
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
	
	def get_program_value(self, value: int, mode: int) -> int:
		if mode == 1:
			# Immediate mode
			return value
		# Position mode
		return self.program[value]

	def get_program_values(self, count: int, modes: Tuple[int, ...]) -> List[int]:
		values = []
		for offset in range(1, count + 1):
			value = self.get_program_value(self.program[self.ptr+offset], modes[offset-1])
			values.append(value)
		return values
	
	def cycle(self) -> int:
		# Read instruction at ptr
		in_ptr = self.ptr # holding for printing later
		instr = IntcodeComputer.parse_instruction(self.program[self.ptr])
		opcode = instr[0]
		modes = instr[1:]
		if opcode == 1:
			# Add
			values = self.get_program_values(2, modes)
			writeAddress = self.program[self.ptr+3]
			#action = f'write {values[0]} + {values[1]} to {writeAddress}'
			self.program[writeAddress] = values[0] + values[1]
			self.ptr += 4
		elif opcode == 2:
			# Multiply
			values = self.get_program_values(2, modes)
			writeAddress = self.program[self.ptr+3]
			#action = f'write {values[0]} + {values[1]} to {writeAddress}'
			self.program[writeAddress] = values[0] * values[1]
			self.ptr += 4
		elif opcode == 3:
			# Write input
			value = self.input.pop()
			if len(self.input) == 0:
				self.input.append(value) # Not sure why sometimes > 1 input in part 2
			writeAddress = self.program[self.ptr+1]
			#action = f'write {value} to address {writeAddress}'
			self.program[writeAddress] = value
			self.ptr += 2
		elif opcode == 4:
			# Read and send to output
			readAddress = self.ptr+1
			self.output = self.get_program_value(self.program[readAddress], modes[0])
			#action = f'read {self.output} from address {readAddress}'
			self.ptr += 2
		elif opcode == 5:
			# Jump if true
			values = self.get_program_values(2, modes)
			if values[0] > 0:
				#action = f'jump {self.ptr} to address {values[1]}'
				self.ptr = values[1]
			else:
				#action = 'jump if true no action'
				self.ptr += 3
		elif opcode == 6:
			# Jump if false
			values = self.get_program_values(2, modes)
			if values[0] == 0:
				#action = f'jump {self.ptr} to address {values[1]}'
				self.ptr = values[1]
			else:
				#action = 'jump if false no action'
				self.ptr += 3
		elif opcode == 7:
			# Less than
			values = self.get_program_values(2, modes)
			writeAddress = self.program[self.ptr+3]
			if values[0] < values[1]:
				self.program[writeAddress] = 1
			else:
				self.program[writeAddress] = 0
			#action = f'write {self.program[writeAddress]} to address {writeAddress}'
			self.ptr += 4
		elif opcode == 8:
			# Equals
			values = self.get_program_values(2, modes)
			writeAddress = self.program[self.ptr+3]
			if values[0] == values[1]:
				self.program[writeAddress] = 1
			else:
				self.program[writeAddress] = 0
			#action = f'write {self.program[writeAddress]} to address {writeAddress}'
			self.ptr += 4
		elif opcode == 99:
			# End program
			#action = 'end program'
			self.ptr += 1
			pass
		else:
			self.ptr += 1
			action = f"Unknown opcode {opcode}"
			print(action)
		#print(f'{in_ptr} -> {instr} -> {action}')
		return opcode
	
	def run(self):
		opcode = 0
		while opcode != 99:
			opcode = self.cycle()
	
	def run_to_signal(self):
		opcode = 0
		while self.is_halted == False:
			opcode = self.cycle()
			if opcode == 99:
				self.is_halted = True
			if self.output != 0:
				return
		
if __name__ == "__main__":
	print("Intcode is a library.")
