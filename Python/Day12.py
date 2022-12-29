# Day 12: The N-Body Problem

import AoC
from typing import List, Dict, Set
import re
import functools
from copy import deepcopy
import numpy as np

def parse_moon(line: str) -> List[int]:
	x = re.findall("[-\d]+", line)
	return list(map(lambda val: (int(val)), x))

def apply_gravity(positions: List[List[int]], velocities: List[List[int]]):
	for a in range(0, len(positions)-1):
		for b in range(a+1, len(positions)):
			for axis in range(0, 3):
				if positions[a][axis] < positions[b][axis]:
					velocities[a][axis] += 1
					velocities[b][axis] -= 1
				elif positions[a][axis] > positions[b][axis]:
					velocities[a][axis] -= 1
					velocities[b][axis] += 1

def apply_velocity(positions: List[List[int]], velocities: List[List[int]]):
	for moon in range(0, len(positions)):
		for axis in range(0, 3):
			positions[moon][axis] += velocities[moon][axis]



def calc_energy(positions: List[List[int]], velocities: List[List[int]]) -> int:
	energies = [0,0,0,0]
	for moon in range(0, len(positions)):
		kin = 0
		pot = 0
		for axis in range(0, 3):
			pot += abs(positions[moon][axis])
			kin += abs(velocities[moon][axis])
		energies[moon] += kin * pot

	print(f'Energies: {energies}')
	total_energy = functools.reduce(lambda a,b: a+b, energies)
	print(f'Total energy: {total_energy}')
	return total_energy

def counted_simulate(positions: List[List[int]], velocities: List[List[int]], n_steps: int):
	for step in range(0, n_steps):
		#print(f'({step}) Positions:  {positions}')
		#print(f'({step}) Velocities: {velocities}')
		apply_gravity(positions, velocities)
		apply_velocity(positions, velocities)

	print(f'(Final) Positions:  {positions}')
	print(f'(Final) Velocities: {velocities}')

	energy = calc_energy(positions, velocities)

# def simulate_to_repeat(positions: List[List[int]], velocities: List[List[int]]):
# 	orig_pos = deepcopy(positions)
# 	orig_vel = deepcopy(velocities)

# 	steps = 0
# 	while True:
# 		steps += 1
# 		if steps % 1000000 == 0:
# 			print(f'Step {steps}')
# 		apply_gravity(positions, velocities)
# 		apply_velocity(positions, velocities)
# 		if orig_pos == positions:
# 			if orig_vel == velocities:
# 				break
	
	# print(f"Universe repeated in {steps} steps")

def find_cycle(track: np.ndarray, start_at: int) -> int:
	# See if the first half of the list equals the second half
	assert len(track) % 2 == 0
	for i in range(start_at, len(track), 2):
		half = int(i / 2)
		front = track[:half]
		back = track[half:half+len(front)]
		if np.array_equal(front, back):
			#print(f'found cycle {front[:6]}') # {front} == {back}')
			return half
	return 0

def analyze_cycles(positions: List[List[int]], velocities: List[List[int]]) -> List[List[int]]:
	# How many cycles to a repeat
	# x, y, z for pos and vel for each moon
	cycles = []
	# Stack of values for tracking cycles
	tracking: List[List[np.ndarray]] = []
	for moon in range(0, len(positions)):
		track:List[np.ndarray] = []
		cycles.append([0]*6)
		for i in range(0,6):
			track.append(np.empty(0, dtype=int))
		tracking.append(track)
	
	analyze_after = 10000
	all_cycles_measured = False
	cycle = 0
	while not all_cycles_measured:
		for moon in range(0, len(positions)):
			for axis in range(0, 3):
				if cycles[moon][axis] == 0:
					tracking[moon][axis] = np.append(tracking[moon][axis], positions[moon][axis])
				if cycles[moon][axis+3] == 0:
					tracking[moon][axis+3] = np.append(tracking[moon][axis+3], velocities[moon][axis])
		cycle += 1
		if cycle % analyze_after == 0:
			print(f'cycle {cycle}. So far: {cycles}')
			# Analyze for cycling. Has to be even number of cycles.
			for moon in range(0, len(positions)):
				for i in range(0, 6):
					if cycles[moon][i] == 0:
						# Returns 0 if not found
						c = find_cycle(tracking[moon][i], (cycle - analyze_after + 2))
						if c > 0:
							print(f'moon {moon} axis {i} cycled after {c} steps.')
							cycles[moon][i] = c
			# Are we done?
			all_cycles_measured = True
			for moon in range(0, len(positions)):
				if 0 in cycles[moon]:
					all_cycles_measured = False
					break
		apply_gravity(positions, velocities)
		apply_velocity(positions, velocities)
	
	return cycles

def main():
	all_input = AoC.read_grouped_input_file('Day12_Input.txt')
	# [0] sample with 179 energy after 10 steps
	# [1] sample with 1940 energy after 100 steps
	# [2] challenge with ? energy after 1000 steps
	input_index = 2
	n_steps = 1000

	moon_positions: List[List[int]] = []
	for line in all_input[input_index]:
		moon_positions.append(parse_moon(line))
	moon_velocities: List[List[int]] = []
	for m in range(0, 4):
		moon_velocities.append([0,0,0])

	# Part 1
	counted_simulate(deepcopy(moon_positions), deepcopy(moon_velocities), n_steps)

	# Part 2
	#simulate_to_repeat(moon_positions, moon_velocities) # too slow
	repeat_cycles_per_moon = analyze_cycles(deepcopy(moon_positions), deepcopy(moon_velocities))
	all_cycles: Set[int] = set()
	for moon in repeat_cycles_per_moon:
		for cycle in moon:
			all_cycles.add(cycle)
	repeat_cycles = sorted(list(all_cycles), reverse=True)
	print(repeat_cycles)

	# When do all repeat_cycles sync?
	max_sync = 0
	jump = repeat_cycles[max_sync]
	ts = jump
	remainders = list(map(lambda x: (ts % x), repeat_cycles))

	while True:
		if remainders[0:max_sync+2] == [0] * (max_sync + 2):
			max_sync += 1
			if (remainders == [0] * len(repeat_cycles)):
				print(f"found future state at time {ts}")
				break
			if max_sync < len(repeat_cycles)-2:
				# Don't want to change the jump for the last digit
				jump *= repeat_cycles[max_sync]
				print(f'jump changed to {jump}')
		ts += jump
		remainders = list(map(lambda cycle: (ts % cycle), repeat_cycles))

if __name__ == '__main__':
	main()
