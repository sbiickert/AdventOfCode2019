import AoC
import numpy as np

def part1(image):
	number_count_by_layer = []
	for z in range(0, image.shape[0]):
		zero_count = len(np.where(image[z] == 0)[0])
		one_count = len(np.where(image[z] == 1)[0])
		two_count = len(np.where(image[z] == 2)[0])
		number_count_by_layer.append((zero_count, one_count, two_count))
	
	number_count_by_layer.sort(key=lambda x:x[0])
	#print(number_count_by_layer)
	print(f'num 1 digits ({number_count_by_layer[0][1]} x num 2 digits ({number_count_by_layer[0][2]}) = {number_count_by_layer[0][1] * number_count_by_layer[0][2]}')

def main():
	all_input = AoC.read_input_file('Day08_Input.txt', True)
	input = list(map(lambda num: int(num), all_input[1]))
	input.reverse() # so we can pop numbers off a stack
	dimensions = (25,6)
	layers = []
	while len(input) > 0:
		layer = []
		for y in range(0, dimensions[1]):
			row: List[int] = []
			for x in range(0, dimensions[0]):
				row.append(input.pop())
			layer.append(row)
		layers.append(layer)
	# print(layers)
	image = np.array(layers)
	# Input for challenge is ndim: 3 shape: (100, 6, 25) size: 15000
	print(f'ndim: {image.ndim} shape: {image.shape} size: {image.size}')

	part1(image)

if __name__ == '__main__':
	main()
