# Day 14: Space Stoichiometry

import AoC
import re
from typing import Dict, List, Tuple
import math

reactions: Dict[str, List[Tuple[int, str]]] = {}
reaction_amounts: Dict[str, int] = {}

def parse_reactions(lines: List[str]):
	# Populates two dictionaries. To get chemical (the key), you need
	# the following reagents, which is a list of (amount, chemical)
	for line in lines:
		matches = re.findall(r'(\d+ [A-Z]+)', line)
		(amount, chemical) = matches.pop().split()
		amount = int(amount)
		reagents: List[Tuple[int,str]] = []
		for reagent_match in matches:
			rm = reagent_match.split()
			reagent = (int(rm[0]), rm[1])
			reagents.append(reagent)
		reactions[chemical] = reagents
		reaction_amounts[chemical] = amount

def make_one_fuel():
	chemicals: Dict[str,int] = {'FUEL': 1}
	excess: Dict[str,int] = {}

	while len(chemicals) > 0:
		chem_names = list(chemicals.keys()).copy()
		for chemical in chem_names:
			if chemical != 'ORE':
				result_amount = chemicals.pop(chemical, None)
				# If we have created excess on a previous step
				if chemical in excess:
					previously_created = excess.pop(chemical)
					if previously_created > result_amount:
						put_back = previously_created - result_amount
						result_amount = 0
						assert(put_back > 0)
						excess[chemical] = put_back
					else:
						result_amount -= previously_created
				if result_amount > 0:
					reaction = reactions[chemical]
					rx_output_amount = reaction_amounts[chemical]
					num_rx = 1
					if result_amount > rx_output_amount:
						num_rx = math.ceil(result_amount / rx_output_amount)
					excess_product = num_rx * rx_output_amount - result_amount
					for reagent in reaction:
						#print(f'{result_amount} {chemical} from {reagent[0] * num_rx} {reagent[1]}')
						if reagent[1] in chemicals:
							chemicals[reagent[1]] += reagent[0] * num_rx
						else:
							chemicals[reagent[1]] = reagent[0] * num_rx
					assert(excess_product >= 0)
					excess[chemical] = excess_product
			#print(f'chemicals: {chemicals}   excess: {excess}')
		if len(chemicals.keys()) == 1 and 'ORE' in chemicals:
			# Only ORE left
			break
	
	print(f'Excess: {excess}')
	print(f'Needed {chemicals["ORE"]} ORE')

def main():
	all_input = AoC.read_grouped_input_file('Day14_Input.txt')
	# [0] sample 31 ORE for 1 FUEL
	# [1] sample 165 ORE for 1 FUEL
	# [2] sample 13312 ORE for 1 FUEL
	# [3] sample 180697 ORE for 1 FUEL
	# [4] sample 2210736 ORE for 1 FUEL
	# [5] challenge 
	parse_reactions(all_input[0])
	print(reactions)
	print(reaction_amounts)
	make_one_fuel()

if __name__ == '__main__':
	main()
