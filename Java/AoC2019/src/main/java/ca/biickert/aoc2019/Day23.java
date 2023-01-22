package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author sjb
 */
public class Day23 extends Solution {

    public Day23() {
	super(23, "Category Six", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var part1Solution = solvePartOne(input.get(0));
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(String nicProgram) {
	List<NetworkNode> nodes = new ArrayList<>();
	for (long i = 0L; i < 50L; i++) {
	    nodes.add(new NetworkNode(i, nicProgram));
	}
	
	long result = -1L;
	int id = 0;

	while (result < 0L) {
	    var node = nodes.get(id);
	    node.work();
	    while (!node.outbox.isEmpty()) {
		var packet = node.outbox.remove();
		if (packet.to() == 255) {
		    result = packet.y();
		    break;
		}
		nodes.get((int)packet.to()).inbox.add(packet);
	    }
	    id++;
	    if (id >= nodes.size()) {
		id = 0;
	    }
	}
	
	return String.valueOf(result);
    }

    private String solvePartTwo() {
	return "";
    }

}

class NetworkNode {
    
    long id;
    private IntCodeComputer computer;
    Queue<Packet> inbox = new LinkedList<>();
    Queue<Packet> outbox = new LinkedList<>();
    
    public NetworkNode(long id, String program) {
	this.id = id;
	this.computer = new IntCodeComputer(program);
	this.computer.input.add(id);
	this.computer.removeOutput();
    }
    
    List<Long> outputs = new ArrayList<>();
    public void work() {
	while (!inbox.isEmpty()) {
	    var p = inbox.remove();
	    computer.input.add(p.x());
	    computer.input.add(p.y());
	}
	
	final int RUN_COUNT = 8; // Arbitrary number
	for (int i = 0; i < RUN_COUNT; i++) {
	    computer.run(true, true, false);
	    if (computer.output != null) {
		outputs.add(computer.removeOutput());
	    }
	}
	while (outputs.size() >= 3) {
	    var p = new Packet(this.id, outputs.remove(0), outputs.remove(0), outputs.remove(0));
	    outbox.add(p);
	}
    }
}

record Packet (long from, long to, long x, long y) {}
