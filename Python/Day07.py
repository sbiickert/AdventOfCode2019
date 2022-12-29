# Day 7: Amplification Circuit

import AoC
from Intcode import IntcodeComputer
from typing import List
from itertools import permutations

def measure_signal(phases: List[int], program: List[int]) -> int:
	signal = 0
	for phase in phases:
		amp_values = [signal, phase]
		amp = IntcodeComputer(amp_values, program.copy())
		amp.run()
		#print(f'Output: {amp.output}')
		signal = amp.output
	return signal

def feedback_signal(phases: List[int], program: List[int]) -> int:
	signal = 0
	amps = [
		IntcodeComputer([signal, phases[0]], program.copy()),
		IntcodeComputer([phases[1]], program.copy()),
		IntcodeComputer([phases[2]], program.copy()),
		IntcodeComputer([phases[3]], program.copy()),
		IntcodeComputer([phases[4]], program.copy())]
	
	# Feedback Loop
	all_amps_have_phase = False
	while amps[0].is_halted == False:
		for i in range(0, 5):
			processing_amp = amps[i]
			#print(f'amp[{i}] input: {processing_amp.input}')
			processing_amp.run_to_signal()
			if processing_amp.is_halted == False:
				signal = processing_amp.output
			#print(f'amp[{i}] output: {processing_amp.output}. Is halted? {processing_amp.is_halted}')
			# Reset output
			processing_amp.output = 0
			if i < 4:
				next_amp = amps[i+1]
			else:
				next_amp = amps[0]
			if all_amps_have_phase:
				next_amp.input = [signal]
			else:
				next_amp.input.insert(0, signal)
		all_amps_have_phase = True
	return signal

def main():
	all_input = AoC.read_input_file('Day07_input.txt', True)
	program = list(map(lambda x: (int(x)), all_input[0].split(',')))
	#print(program)
	
	
	# Part 1
	#max_signal = 0
	#for phases in permutations([0,1,2,3,4]):
	#	signal = measure_signal(phases, program)
	#	print(f'{phases}: {signal}')
	#	max_signal = max(signal, max_signal)
	#print(f"Part 1 maximum signal: {max_signal}")
	
	# Part 2
	max_signal = 0
	for phases in permutations([5,6,7,8,9]):
		#phases = [9,8,7,6,5]
		#phases = [9,7,8,5,6]
		signal = feedback_signal(phases, program)
		print(f'{phases}: {signal}')
		max_signal = max(signal, max_signal)
	
	print(f"Part 2 maximum signal: {max_signal}")
	
if __name__ == '__main__':
	main()
