def read_input_file(name, remove_empty_lines):
	with open(name, 'r') as file:
		content = file.read()
		input = content.splitlines()
	if remove_empty_lines:
		input = list(filter(lambda line: (line != ''), input))
	return input

def read_grouped_input_file(name):
	input = read_input_file(name, False)
	group = []
	groups = []
	for line in input:
		if line != '':
			group.append(line)
		else:
			groups.append(group)
			group = []
	groups.append(group)
	return groups
	

		
if __name__ == "__main__":
	print("AoC is a library.")
