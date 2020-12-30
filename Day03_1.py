# Day 4: Secure Container

RANGE_LOW =  359282
RANGE_HIGH = 820401

def meets_criteria(num: int) -> bool:
	str_num = str(num)
	found_adj_same = False
	digits_decrease = True
		
	for i in range(0, 5):
		if found_adj_same or str_num[i] == str_num[i+1]:
			found_adj_same = True
		if str_num[i] > str_num[i+1]:
			# Can compare strings b/c '0' is less than '1'
			digits_decrease = False
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
