# 

import AoC
from typing import List
from itertools import permutations

def measure_signal(phases: List[int], program: List[int]) -> int:
	signal = 0
	for phase in phases:
		amp_values = [signal, phase]
		amp = AoC.IntcodeComputer(amp_values, program.copy())
		amp.run()
		#print(f'Output: {amp.output}')
		signal = amp.output
	return signal

def main():
	all_input = AoC.read_input_file('Day07_input.txt', True)
	program = list(map(lambda x: (int(x)), all_input[0].split(',')))
	#print(program)
	
	max_signal = 0
	for phases in permutations([0,1,2,3,4]):
		signal = measure_signal(phases, program)
		print(f'{phases}: {signal}')
		max_signal = max(signal, max_signal)
	
	print(f"maximum signal: {max_signal}")
	
if __name__ == '__main__':
	main()
