# Day 1: The Tyranny of the Rocket Equation

import math
import AoC

def calc_fuel(mass):
	fuel = 0
	while mass > 0:
		mass = math.floor(mass / 3) - 2
		#print(f'{fuel} + {mass}')
		if mass > 0:
			fuel += mass
	return fuel

def main():
	input = AoC.read_input_file("Day01_Input.txt", True)
	total_fuel = 0
	for mass in input:
		total_fuel += calc_fuel(int(mass))
	print(f'The total fuel needed is {total_fuel}.')

if __name__ == "__main__":
	main()
	#calc_fuel(100756)
