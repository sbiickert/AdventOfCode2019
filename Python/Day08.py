import AoC
import numpy as np

INPUT_INDEX = 1
DIMENSIONS = (25,6)

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

def part2(image):
	dim = tuple(reversed(DIMENSIONS)) # Shape is nrows, ncols
	result = np.full(dim, 2)
	shape = image.shape
	for z in range(0, shape[0]):
		for y in range(0, shape[1]):
			for x in range(0, shape[2]):
				pixel = image[z, y, x]
				rPixel = result[y, x]
				if rPixel == 2 and pixel != 2:
					result[y, x] = pixel
	print(result)
# [[0 1 1 0 0 0 0 1 1 0 1 0 0 0 1 1 1 1 1 0 0 1 1 0 0]
#  [1 0 0 1 0 0 0 0 1 0 1 0 0 0 1 1 0 0 0 0 1 0 0 1 0]
#  [1 0 0 0 0 0 0 0 1 0 0 1 0 1 0 1 1 1 0 0 1 0 0 1 0]
#  [1 0 1 1 0 0 0 0 1 0 0 0 1 0 0 1 0 0 0 0 1 1 1 1 0]
#  [1 0 0 1 0 1 0 0 1 0 0 0 1 0 0 1 0 0 0 0 1 0 0 1 0]
#  [0 1 1 1 0 0 1 1 0 0 0 0 1 0 0 1 1 1 1 0 1 0 0 1 0]]


def main():
	all_input = AoC.read_input_file('Day08_Input.txt', True)
	input = list(map(lambda num: int(num), all_input[INPUT_INDEX]))
	input.reverse() # so we can pop numbers off a stack
	layers = []
	while len(input) > 0:
		layer = []
		for y in range(0, DIMENSIONS[1]):
			row: List[int] = []
			for x in range(0, DIMENSIONS[0]):
				row.append(input.pop())
			layer.append(row)
		layers.append(layer)
	# print(layers)
	image = np.array(layers)
	# Input for challenge is ndim: 3 shape: (100, 6, 25) size: 15000
	print(f'ndim: {image.ndim} shape: {image.shape} size: {image.size}')

	part1(image)
	part2(image)

if __name__ == '__main__':
	main()
