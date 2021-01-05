# Day 10: Monitoring Station

import AoC
import numpy as np
import math

def chars_to_nums(char):
	if char == '#':
		return 1
	return 0

def make_astromap(input):
	width = len(input[0])
	height = len(input)
	for i in range(0, len(input)):
		chars = list(input[i])
		nums = list(map(lambda char: (chars_to_nums(char)), chars))
		input[i] = nums
	astromap = np.array(input)
	return astromap

def make_bearing_lookup(size):
	# Maps are square. Create lookup of bearings from -size to +size
	lookup = np.full((size*2+1, size*2+1), 0.0)
	for dx in range(-1 * size, size + 1):
		for dy in range(-1 * size, size + 1):
			# Calc bearing (0 is west)
			bearing = round(math.atan2(dy, dx) * 180 / math.pi, 3)
			lookup[dy+size, dx+size] = bearing
	return lookup

def choose_location(astromap, bearings):
	np_asteroid_coords = np.where(astromap == 1)
	#print(np_asteroid_coords)
	#print(np_asteroid_coords[0])
	asteroid_coords = []
	for i in range(0, len(np_asteroid_coords[0])):
		x = np_asteroid_coords[1][i]
		y = np_asteroid_coords[0][i]
		asteroid_coords.append((x, y))
		#print(f'Asteroid at x: {x}, y: {y}')

	size = astromap.shape[0]
	
	for a_coord in asteroid_coords:
		bearings_to_b = {}
		for b_coord in asteroid_coords:
			dx = b_coord[0] - a_coord[0]
			dy = b_coord[1] - a_coord[1]
			if dx != 0 or dy != 0:
				bearing = bearings[dy+size, dx+size]
				#print(f'{a_coord} -> {b_coord} is bearing {bearing}')
				bearings_to_b[bearing] = b_coord
		#print(bearings_to_b)
		astromap[a_coord[1], a_coord[0]] = len(bearings_to_b)
	
	#print(astromap)

	max_value = np.max(astromap)
	(max_index_y, max_index_x) = np.unravel_index(np.argmax(astromap), astromap.shape)

	print(f'Best location is at {max_index_x}, {max_index_y} detecting {max_value}.')
	return (max_index_x, max_index_y)

def main():
	# [0]: sample with best location (3,4), detecting 8
	# [1]: sample with best location (5,8), detecting 33
	# [2]: sample with best location (1,2), detecting 35
	# [3]: sample with best location (6,3), detecting 41
	# [4]: sample with best location (11,13), detecting 210
	# [5]: challenge input
	all_input = AoC.read_grouped_input_file('Day10_Input.txt')
	astromap = make_astromap(all_input[5])
	#print(astromap)
	size = astromap.shape[0]
	bearings = make_bearing_lookup(size)
	#print(bearings)

	loc = choose_location(astromap, bearings)
	print(loc)

if __name__ == '__main__':
	main()