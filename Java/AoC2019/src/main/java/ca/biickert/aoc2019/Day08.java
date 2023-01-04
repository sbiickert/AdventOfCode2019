package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day08 extends Solution {

    public Day08() {
	super(8, "Space Image Format", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	SpaceImage image = new SpaceImage(input.get(0));

	var part1Solution = solvePartOne(image);
	var part2Solution = solvePartTwo(image);

	result = new Result(Integer.toString(part1Solution), Integer.toString(part2Solution));

	return result;
    }

    private int solvePartOne(SpaceImage image) {
	int least = Integer.MAX_VALUE;
	int result = 0;

	for (int l = 0; l < image.getLayerCount(); l++) {
	    int zeroCount = 0;
	    int oneCount = 0;
	    int twoCount = 0;
	    for (int r = 0; r < image.getHeight(); r++) {
		for (int c = 0; c < image.getWidth(); c++) {
		    int digit = image.getDigit(l, r, c);
		    if (digit == 0) {
			zeroCount++;
		    }
		    if (digit == 1) {
			oneCount++;
		    }
		    if (digit == 2) {
			twoCount++;
		    }
		}
	    }
	    if (zeroCount < least) {
		least = zeroCount;
		result = oneCount * twoCount;
	    }
	}

	return result;
    }

    private int solvePartTwo(SpaceImage image) {
	image.print();
	return 0;
    }

}

class SpaceImage {

    private int width;
    private int height;
    private List<Integer> digits;

    public SpaceImage(String defn) {
	var list = Arrays.asList(defn.split(""));
	digits = list.stream().map(Integer::valueOf).toList();
	switch (digits.size()) {
	    case 12:
		width = 3;
		height = 2;
		break;
	    case 16:
		width = 2;
		height = 2;
		break;
	    default:
		width = 25;
		height = 6;
	}
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public int getLayerCount() {
	return digits.size() / (width * height);
    }

    public int getDigit(int layer, int row, int col) {
	int index = (layer * width * height) + (row * width) + col;
	return digits.get(index);
    }

    static final String WHITE = "#";
    static final String BLACK = " ";

    public void print() {
	for (int r = 0; r < height; r++) {
	    String line = "";
	    for (int c = 0; c < width; c++) {
		String color = "";
		for (int l = getLayerCount() - 1; l >= 0; l--) {
		    switch (getDigit(l, r, c)) {
			case 0:
			    color = BLACK;
			    break;
			case 1:
			    color = WHITE;
			    break;
		    }
		}
		line += color;
	    }
	    System.out.println(line);
	}
    }
}
