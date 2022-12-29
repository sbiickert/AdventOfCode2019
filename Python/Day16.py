# Day 16: Flawed Frequency Transmission

import AoC
from typing import List, Dict, Tuple
import numpy as np

PATTERN_TEMPLATE: List[int] = [0, 1, 0, -1]

pattern_cache: Dict[Tuple[int, int], np.ndarray] = {}

def pattern_for_element(element: int, length: int) -> np.ndarray:
	key = (element, length)
	if key not in pattern_cache:
		# element is 0-based
		out_pattern: np.ndarray = np.zeros(length + 1, dtype=int)
		i = 0
		p = 0
		e = 0
		while i < out_pattern.size:
			out_pattern[i] = PATTERN_TEMPLATE[p]
			e += 1
			if e > element:
				e = 0
				p += 1
				if p >= len(PATTERN_TEMPLATE):
					p = 0
			i += 1
		pattern_cache[key] = np.delete(out_pattern, 0)
	return pattern_cache[key]

def calc_output(signal: List[int], num_phases: int) -> List[int]:
	signal_length = len(signal)
	sig_arr = np.array(signal)
	for phase in range(0, num_phases):
		output_signal = np.zeros(signal_length, dtype=int)
		for element in range(0, signal_length):
			pattern = pattern_for_element(element, signal_length)
			for index in range(0, signal_length):
				output_signal[element] += sig_arr[index] * pattern[index]
		for i in range(0, signal_length):
			sig_arr[i] = abs(output_signal[i]) % 10
		print(f'Phase {phase+1} complete') # {sig_arr.tolist()}')
	return sig_arr.tolist()

# Taken from https://github.com/XorZy/Aoc_2019_Day_16/blob/master/Program.cs
def cheat(signal: np.ndarray, offset:int, num_phases: int):
	signal_len = signal.size
	_cache = np.zeros(signal_len, dtype=int)
	for phase in range(0, num_phases):
		long_sum = 0
		for k in range(0, signal_len):
			long_sum += signal[k]
		
		for i in range(offset, signal_len):
			_cache[i] = long_sum % 10
			long_sum -= signal[i]
		
		tmp = signal
		signal = _cache
		_cache = tmp
	
	output_signal = signal[offset:offset+8]
	print(output_signal)

def main():
	all_input = AoC.read_input_file('Day16_Input.txt', True)
	# [0] sample 01029498 after 4 phases
	# [1] sample 24176176 after 100 phases
	# [2] sample 73745418 after 100 phases
	# [3] sample 52432133 after 100 phases
	# [4] challenge
	input_signal = all_input[4]
	signal = list(map(lambda digit: int(digit), list(input_signal)))

	# Part 1
	output_signal = calc_output(signal.copy(), 100)
	sig_str = ''.join([str(i) for i in output_signal])
	print(f'first 8 digits of output signal are: {sig_str[:8]}')

	# Part 2
	signal *= 10000
	print(len(signal))
	offset = int(input_signal[:7])
	cheat(np.array(signal), offset, 100)

if __name__ == '__main__':
	main()