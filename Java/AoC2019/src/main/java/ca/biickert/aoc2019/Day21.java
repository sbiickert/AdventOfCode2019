package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.IntCodeComputer;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day21 extends Solution {

    public Day21() {
	super(21, "Springdroid Adventure", true);
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

    private String solvePartOne(String intCodeProgram) {
	var script = new String[]{
	    "NOT A T",
	    "NOT B J",
	    "OR T J",
	    "NOT C T",
	    "OR T J",
	    "AND D J"};

	var bot = new SpringBot(intCodeProgram);
	for (var cmd : script) {
	    bot.addScriptCommand(cmd);
	}
	var result = bot.walk();

	return String.valueOf(result);
    }

    private String solvePartTwo(String intCodeProgram) {
	// Courtesy dartcoder on Reddit
	var script = new String[]{
	    "NOT C J ",
	    "AND D J ",
	    "AND H J",
	    "NOT B T ",
	    "AND D T ",
	    "OR T J",
	    "NOT A T ",
	    "OR T J"};

	var bot = new SpringBot(intCodeProgram);
	for (var cmd : script) {
	    bot.addScriptCommand(cmd);
	}
	var result = bot.run();

	return String.valueOf(result);
    }

}

class SpringBot {

    private IntCodeComputer computer;
    List<String> script = new ArrayList<>();

    public SpringBot(String intCode) {
	computer = new IntCodeComputer(intCode);
    }

    public void addScriptCommand(String cmd) {
	script.add(cmd);
    }

    public Long walk() {
	for (var cmd : script) {
	    computer.inputAscii(cmd);
	}
	computer.inputAscii("WALK");
	return go();
    }

    public Long run() {
	for (var cmd : script) {
	    computer.inputAscii(cmd);
	}
	computer.inputAscii("RUN");
	return go();
    }

    private Long go() {
	int newLineCount = 0; // Death is followed by many newlines. Stop after 3.

	while (computer.outputAscii() != null && newLineCount < 4) {
	    computer.run(true, false);

	    if (computer.output == 10L) {
		newLineCount++;
	    } else {
		newLineCount = 0;
	    }

	    String ascii = computer.outputAscii();
	    if (ascii != null) {
		System.out.print(ascii);
		System.out.flush();
	    }
	    //wait(1);
	}

	return computer.output;
    }

//    private void wait(int ms) {
//	try {
//	    Thread.sleep(ms);
//	} catch (InterruptedException ex) {
//	    Thread.currentThread().interrupt();
//	}
//    }

}
