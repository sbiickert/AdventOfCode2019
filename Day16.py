# Day 16: Flawed Frequency Transmission

import AoC
from typing import List

PATTERN_TEMPLATE: List[int] = [0, 1, 0, -1]

def pattern_for_element(element: int, length: int) -> List[int]:
	# element is 0-based
	out_pattern: List[int] = []
	for digit in PATTERN_TEMPLATE:
		for i in range(0, element+1):
			out_pattern.append(digit)
	while len(out_pattern) <= length:
		out_pattern += out_pattern
	return out_pattern[1:]

def calc_output(signal: List[int], num_phases: int) -> List[int]:
	signal_length = len(signal)
	for phase in range(0, num_phases):
		output_signal = [0] * signal_length
		for element in range(0, signal_length):
			pattern = pattern_for_element(element, signal_length)
			for index in range(0, signal_length):
				output_signal[element] += signal[index] * pattern[index]
		signal = list(map(lambda val: int(str(val)[-1:]), output_signal))
		#print(signal)
	return signal
			

def main():
	all_input = AoC.read_input_file('Day16_Input.txt', True)
	# [0] sample 01029498 after 4 phases
	# [1] sample 24176176 after 100 phases
	# [2] sample 73745418 after 100 phases
	# [3] sample 52432133 after 100 phases
	# [4] challenge
	signal = list(map(lambda digit: int(digit), list(all_input[4])))
	print(signal)
	output_signal = calc_output(signal.copy(), 100)
	sig_str = ''.join([str(i) for i in output_signal])
	print(f'first 8 digits of output signal are: {sig_str[:8]}')
	


if __name__ == '__main__':
	main()