# Day 6: Universal Orbit Map

import AoC
from typing import Dict, List, Set

orbit_data: Dict[str, str] = {'COM': None}

def orbits(orbiting_obj:str) -> List[str]:
	orbits_list: List[str] = []
	in_orbit_around = orbit_data[orbiting_obj]
	while in_orbit_around is not None:
		orbits_list.append(in_orbit_around)
		orbiting_obj = in_orbit_around
		in_orbit_around = orbit_data[orbiting_obj]
	return orbits_list

def count_orbits() -> int:
	total_orbits = 0
	for orbiting_obj in orbit_data:
		orbit_count = len(orbits(orbiting_obj))
		total_orbits += orbit_count
	return total_orbits

def get_orbital_transfers() -> List[str]:
	# Will calculate YOU moving to shared body, then SAN moving to shared body
	i_am_orbiting = orbit_data['YOU']
	santa_is_orbiting = orbit_data['SAN']
	
	my_orbits: List[str] = orbits('YOU')
	#print(f'my orbits: {my_orbits}')
	santas_orbits: List[str] = orbits('SAN')
	#print(f'santa orbits: {santas_orbits}')
	common_orbits = set(my_orbits).intersection(set(santas_orbits))
	#print(f'common orbits (unordered): {common_orbits}')
	
	transfers: List[str] = []
	for orbit in my_orbits:
		transfers.append(orbit)
		if orbit in common_orbits:
			break
	for orbit in santas_orbits:
		transfers.append(orbit)
		if orbit in common_orbits:
			break
	transfers.remove(i_am_orbiting)
	transfers.remove(santa_is_orbiting)
	return transfers

def main():
	all_input = AoC.read_grouped_input_file('Day06_Input.txt')
	input = all_input[2] # [0] part 1 sample, [1] part 2 sample, [2] input
	for line in input:
		info = line.split(')')
		# Dict key is the orbiting object, the value is the thing it is orbiting
		orbit_data[info[1]] = info[0]
	
	# Part 1
	total_orbits = count_orbits()
	print(f"Total orbits: {total_orbits}")

	# Part 2
	transfers = get_orbital_transfers()
	#print(transfers)
	print(f'Transfers to get to Santa: {len(transfers)}')
	
if __name__ == '__main__':
	main()
