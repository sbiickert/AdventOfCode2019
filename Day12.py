# Day 12: The N-Body Problem

import AoC
from typing import List, Dict
import re
import functools

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

def counted_simulate(positions: List[List[int]], velocities: List[List[int]], n_steps: int):
	for step in range(0, n_steps):
		#print(f'({step}) Positions:  {positions}')
		#print(f'({step}) Velocities: {velocities}')
		apply_gravity(positions, velocities)
		apply_velocity(positions, velocities)

	print(f'(Final) Positions:  {positions}')
	print(f'(Final) Velocities: {velocities}')

	energy = calc_energy(positions, velocities)


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
	counted_simulate(moon_positions.copy(), moon_velocities.copy(), n_steps)

	

if __name__ == '__main__':
	main()