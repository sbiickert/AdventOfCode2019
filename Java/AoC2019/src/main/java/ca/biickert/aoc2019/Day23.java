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

	var solution = solveParts(input.get(0));

	result = new Result(String.valueOf(solution[0]), String.valueOf(solution[1]));

	return result;
    }

    private Long[] solveParts(String nicProgram) {
	Long[] results = new Long[] {null,null};
	List<NetworkNode> nodes = new ArrayList<>();
	for (long i = 0L; i < 50L; i++) {
	    nodes.add(new NetworkNode(i, nicProgram));
	}
	
	int id = 0;
	boolean foundRepeat = false;
	Packet natPacket = null;
	Packet natPacketSentToZero = null;

	while (!foundRepeat) {
	    var node = nodes.get(id);
	    node.work();
	    while (!node.outbox.isEmpty()) {
		var packet = node.outbox.remove();
		if (packet.to() == 255) {
		    if (results[0] == null) {
			// First packet.y sent to 255 is part 1 result
			results[0] = packet.y();
		    }
		    natPacket = packet;
		}
		else {
		    nodes.get((int)packet.to()).inbox.add(packet);
		}
	    }
	    
	    id++;
	    
	    if (id >= nodes.size()) {
		id = 0;
		// We've done a round-robin. Check for network idle.
		if (networkIsIdle(nodes)) {
		    //System.out.println("Sending " + natPacket + " to node 0.");
		    nodes.get(0).inbox.add(natPacket);
		    if (natPacketSentToZero != null && natPacketSentToZero.y() == natPacket.y()) {
			foundRepeat = true;
			results[1] = natPacket.y();
		    }
		    natPacketSentToZero = natPacket;
		}
	    }
	}
	
	return results;
    }

    private boolean networkIsIdle(List<NetworkNode> nodes) {
	boolean idle = true;
	
	for (int i = 0; i < nodes.size(); i++) {
	    idle = idle && nodes.get(i).isIdle();
	    if (!idle) { break; }
	}
	
	return idle;
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
    
    public boolean isIdle() {
	return inbox.isEmpty() && outbox.isEmpty() && outputs.isEmpty();
    }
}

record Packet (long from, long to, long x, long y) {}
