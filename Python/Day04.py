# Day 4: Secure Container

RANGE_LOW =  359282
RANGE_HIGH = 820401

def meets_criteria(num: int) -> bool:
	str_num = str(num)
	possible_adjacents = []
	digits_decrease = True
		
	for i in range(0, 5):
		this_num = str_num[i]
		next_num = str_num[i+1]
		
		if this_num == next_num and this_num not in possible_adjacents:
			possible_adjacents.append(this_num)
			
		if this_num > next_num:
			# Can compare strings b/c '0' is less than '1'
			digits_decrease = False
			break
	
	found_adj_same = False
	for poss in possible_adjacents:
		# We found 2, make sure not 3 in a row
		if ((3 * poss) in str_num) == True:
			#print(f'Found adjacent {poss} in {str_num}')
			#print('but more than 2 in a row.')
			pass
		else:
			found_adj_same = True
			break
	
	return digits_decrease and found_adj_same

def main():
	valid_count = 0
	for num in range(RANGE_LOW, RANGE_HIGH + 1):
		if meets_criteria(num):
			valid_count += 1
	print(num)
	print(f'{valid_count} numbers meet the rules.')

if __name__ == '__main__':
	main()
