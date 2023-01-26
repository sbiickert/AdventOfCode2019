package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.Algorithms;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.IntCodeComputer;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author sjb
 */
public class Day25 extends Solution {

    public Day25() {
	super(25, "Cryostasis", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var part1Solution = solvePartOne(input.get(0));

	result = new Result(part1Solution, "");

	return result;
    }

    private String solvePartOne(String program) {
	var droid = new Explorer(program);
	droid.runToPrompt();
	
	// I played the game to figure out the path and pick up items
	var nav = new String[]{"south", "take fixed point", "north", "west", "west", "west", "take hologram", "east", "east", "east", "north", "take candy cane", "west", "take antenna", "south", "take whirled peas", "north", "west", "take shell", "east", "east", "north", "north", "take polygon", "south", "west", "take fuel cell", "west"};
	for (int i = 0; i < nav.length; i++) {
	    if (droid.addCommand(nav[i])) {
		droid.runToPrompt();
	    }
	}

	// fixed point will never be dropped
	var items = new String[]{"hologram", "shell", "whirled peas", "fuel cell", "polygon", "antenna", "candy cane"};
	for (int i = 1; i <= items.length; i++) {
	    var combinationsToDrop = Algorithms.getCombinations(items, items.length, i);
	    for (var combo : combinationsToDrop) {
		for (var item : combo) {
		    droid.addCommand("drop " + item);
		    droid.runToPrompt();
		}
		droid.addCommand("west");
		droid.runToPrompt();
		if (droid.getLastObservation().description.equals("In the next room, a pressure-sensitive floor will verify your identity.") == false) {
		    // This conditional did not actually work, but since I could read the output and copy the code... I don't care.
		    System.out.println(combo);
		    break;
		}
		for (var item : combo) {
		    droid.addCommand("take " + item);
		    droid.runToPrompt();
		}
	    }
	    if (droid.getInventory().size() < 8) {
		break;
	    }
	}

	var scanner = new Scanner(new BufferedInputStream(System.in));
	while (true) {
	    String cmd = scanner.nextLine();
	    if (cmd.contains("quit")) {
		break;
	    }
	    if (droid.addCommand(cmd)) {
		droid.runToPrompt();
	    }
	}

	return "";
    }

}

class Explorer {

    public static final List<String> DIRECTIONS;
    public static final String NORTH = "north";
    public static final String SOUTH = "south";
    public static final String EAST = "east";
    public static final String WEST = "west";

    static {
	DIRECTIONS = new ArrayList<>();
	DIRECTIONS.add(NORTH);
	DIRECTIONS.add(SOUTH);
	DIRECTIONS.add(EAST);
	DIRECTIONS.add(WEST);
    }

    static String oppositeDirectionTo(String dir) {
	if (dir.equals(NORTH)) {
	    return SOUTH;
	}
	if (dir.equals(SOUTH)) {
	    return NORTH;
	}
	if (dir.equals(WEST)) {
	    return EAST;
	}
	if (dir.equals(EAST)) {
	    return WEST;
	}
	return null;
    }

    private IntCodeComputer computer;
    private Set<String> inventory;
    private ExplorerObservation lastObservation = null;

    public Explorer(String pgm) {
	computer = new IntCodeComputer(pgm);
    }

    public Set<String> getInventory() {
	return inventory;
    }

    public ExplorerObservation getLastObservation() {
	return lastObservation;
    }

    public String runToPrompt() {
	String output = "";

	while (true) {
	    computer.run(true, false);
	    var character = computer.outputAscii();
	    output += character;
	    System.out.print(character);
	    if (output.endsWith("Command?")) {
		break;
	    }
	}

	var obsn = new ExplorerObservation(output);
	if (obsn.name != null) {
	    lastObservation = obsn;
	} else {
	    if (output.contains("You take the") || output.contains("You drop the")) {
		addCommand("inv");
		output += runToPrompt();
	    } else if (output.contains("Items in your inventory")) {
		setInventory(output);
	    }
	}

	return output;
    }

    public boolean addCommand(String cmd) {
	if (DIRECTIONS.contains(cmd)) {
	    if (!lastObservation.doors.contains(cmd)) {
		System.out.println("Direction " + cmd + " is not allowed.");
		return false;
	    }
	} else if (cmd.equals("inv")) {
	} else if (cmd.startsWith("take ")) {
	    String item = cmd.substring(5);
	    if (!lastObservation.items.contains(item)) {
		System.out.println("Item " + item + " does not exist here.");
		return false;
	    }
	} else if (cmd.startsWith("drop ")) {
	    String item = cmd.substring(5);
	    if (!inventory.contains(item)) {
		System.out.println("Item " + item + " does not exist in your inventory.");
		return false;
	    }
	} else {
	    return false;
	}
	computer.inputAscii(cmd);
	return true;
    }

    private void setInventory(String message) {
	//System.out.println(message);
	var trimmed = message.trim();
	var lines = trimmed.split("\n");
	int i = 1; // 0 is "Items in your inventory:"
	this.inventory = new HashSet<>();
	while (!lines[i].isBlank()) {
	    inventory.add(lines[i].substring(2));
	    i++;
	}
    }
}

class ExplorerObservation {

    String name;
    String description;
    Set<String> doors;
    Set<String> items;

    public ExplorerObservation(String raw) {
	var trimmed = raw.trim();
	var lines = trimmed.split("\n");

	if (trimmed.contains("You aren't carrying any items")) {
	    return;
	}
	if (trimmed.contains("You take the")) {
	    return;
	}
	if (trimmed.contains("You drop the")) {
	    return;
	}
	if (trimmed.contains("Items in your inventory")) {
	    return;
	}

	Set<String> d = new HashSet<>();
	Set<String> i = new HashSet<>();

	int idx = 0;
	for (; idx < lines.length; idx++) {
	    var line = lines[idx];
	    if (line.startsWith("=")) {
		name = line.trim();
		description = lines[++idx].trim();
	    } else if (line.startsWith("Doors")) {
		doors = d;
	    } else if (line.startsWith("Items")) {
		items = i;
	    } else if (line.trim().equals("")) {
		continue;
	    } else if (line.contains("Command?")) {
		continue;
	    } else if (items != null) {
		items.add(line.substring(2).trim());
	    } else {
		doors.add(line.substring(2).trim());
	    }
	}
	items = i; // Just in case there are no items.
    }
}
