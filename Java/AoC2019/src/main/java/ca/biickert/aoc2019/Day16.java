package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day16 extends Solution {

    public Day16() {
	super(16, "Flawed Frequency Transmission", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var part1Solution = solvePartOne(input.get(0));
	var part2Solution = solvePartTwo(input.get(0));

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(String input) {
	var fft = new FFT(input);
	var output = fft.calculate(100);
	String result = "";
	for (int i = 0; i < 8; i++) {
	    result += String.valueOf(output.get(i));
	}
	
	return result;
    }

    private String solvePartTwo(String input) {
	String bigInput = input.repeat(10000);
	int offset = Integer.parseInt(input.substring(0, 7));
	var output = FFT.cheat(FFT.stringToIntegerList(bigInput), offset, 100);
	String result = "";
	for (int i = 0; i < 8; i++) {
	    result += String.valueOf(output.get(i));
	}
	
	return result;
    }

}

class FFT {
    
    static List<Integer> stringToIntegerList(String numbers) {
	List<Integer> result = new ArrayList<>();
	for (String s : numbers.split("")) {
	    result.add(Integer.valueOf(s));
	}
	return result;
    }
    
    List<Integer> pattern;
    List<Integer> signal;
    
    public FFT(String numbers) {
	pattern = new ArrayList<>();
	pattern.add(0);
	pattern.add(1);
	pattern.add(0);
	pattern.add(-1);
	signal = stringToIntegerList(numbers);
    }
    
    public List<Integer> calculate(int phaseCount) {
	var data = signal;
	for (int p = 0; p < phaseCount; p++) {
	    data = calculatePhase(data);
	}
	return data;
    }
    
    private List<Integer> calculatePhase(List<Integer> data) {
	List<Integer> output = new ArrayList<>();
	List<Integer> pat = new ArrayList<>(pattern);
	for (int i = 0; i < data.size(); i++) {
	    int p = 1;
	    int sum = 0;
	    for (int j = 0; j < data.size(); j++) {
		sum += pat.get(p) * data.get(j);
		p++;
		if (p >= pat.size()) { p = 0; }
	    }
	    output.add(Math.abs(sum) % 10);
	    pat = new ArrayList<>();
	    for (int x = 0; x < pattern.size(); x++) {
		for (int y = 0; y <= i+1; y++) {
		    pat.add(pattern.get(x));
		}
	    }
	}
	return output;
    }
    
        
    public static List<Integer> cheat(List<Integer> signal, int offset, int phaseCount) {
	List<Integer> cache = new ArrayList<>(signal.size());
	for (int i = 0; i < signal.size(); i++) { cache.add(0); }
	
	for (int phase = 0; phase < phaseCount; phase++) {
	    long sum = 0;
	    for (int k = 0; k < signal.size(); k++) {
		sum += signal.get(k);
	    }
	    
	    for (int i = offset; i < signal.size(); i++) {
		cache.set(i, (int)(sum % 10));
		sum -= signal.get(i);
	    }
	    
	    List<Integer> temp = signal;
	    signal = cache;
	    cache = temp;
	}
	
	return signal.subList(offset, offset + 8);
    }

}
