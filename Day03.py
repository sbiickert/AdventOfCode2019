# Day 3: Crossed Wires

import AoC

def bread_crumbs(wire):
	trail = set()
	loc = [0,0]
	#trail.add(f'{loc[0]},{loc[1]}') # crosses at 0,0 don't count
	for run in wire:
		dir = run[0]
		length = int(run[1:])
		delta = [0,0]
		if dir == 'R':
			delta = [1,0]
		elif dir == 'L':
			delta = [-1,0]
		elif dir == 'U':
			delta = [0,1]
		elif dir == 'D':
			delta = [0,-1]
		for i in range(0, length):
			loc[0] += delta[0]
			loc[1] += delta[1]
			trail.add(f'{loc[0]},{loc[1]}')
	return trail

def manhattan_dist(x, y):
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
	
if __name__ == '__main__':
	main()
