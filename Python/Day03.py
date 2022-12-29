# Day 3: Crossed Wires

from typing import List, Dict, Set
import AoC

def delta_for_dir(dir: str) -> List[int]:
	delta = [0,0]
	if dir == 'R':
		delta = [1,0]
	elif dir == 'L':
		delta = [-1,0]
	elif dir == 'U':
		delta = [0,1]
	elif dir == 'D':
		delta = [0,-1]
	return delta

def bread_crumbs(wire: List[str]) -> Set[str]:
	trail = set()
	loc = [0,0]
	#trail.add(f'{loc[0]},{loc[1]}') # crosses at 0,0 don't count
	for run in wire:
		dir = run[0]
		length = int(run[1:])
		delta = delta_for_dir(dir)
		for i in range(0, length):
			loc[0] += delta[0]
			loc[1] += delta[1]
			trail.add(f'{loc[0]},{loc[1]}')
	return trail
	
def calc_signal_distances(wire: List[str], cross_keys: List[str]) -> Dict[str, int]:
	d = {}
	loc = [0,0]
	distance = 0
	
	for run in wire:
		dir = run[0]
		length = int(run[1:])
		delta = delta_for_dir(dir)
		
		for i in range(0, length):
			loc[0] += delta[0]
			loc[1] += delta[1]
			distance += 1
			key = f'{loc[0]},{loc[1]}'
			if key in cross_keys:
				d[key] = distance
	return d


def manhattan_dist(x: int, y: int) -> int:
	return abs(x) + abs(y)

def main():
	input = AoC.read_input_file("Day03_Input.txt", True)
	# wire 1 is input[0] and wire 2 is input[2]
	wire1 = input[0].split(',')
	bc1 = bread_crumbs(wire1)
	wire2 = input[1].split(',')
	bc2 = bread_crumbs(wire2)
	
	crosses = bc1.intersection(bc2)
	#print(crosses)
	cross_coords = list(map(lambda cross: (list(map(lambda x: (int(x)), cross.split(',')))), crosses))
	#print(cross_coords)
	distances = sorted(list(map(lambda coord: (manhattan_dist(coord[0], coord[1])), cross_coords)))
	#print(distances)
	print(f"The closest cross is at distance {distances[0]}")
	signal_distances1 = calc_signal_distances(wire1, crosses)
	#print(signal_distances1)
	signal_distances2 = calc_signal_distances(wire2, crosses)
	sum_sig_dist:List[int] = []
	for key in signal_distances1:
		sd1 = signal_distances1[key]
		sd2 = signal_distances2[key]
		sum_sig_dist.append(sd1 + sd2)
	sum_sig_dist.sort()
	print(f'The closest crossing in signal distance is {sum_sig_dist[0]}')
				
if __name__ == '__main__':
	main()
