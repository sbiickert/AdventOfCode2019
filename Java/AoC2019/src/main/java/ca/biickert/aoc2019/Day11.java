package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.IntCodeComputer;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day11 extends Solution {

    public Day11() {
	super(11, "Space Police", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	EmergencyHullPaintingRobot.setProgramDefn(input.get(0));

	var part1Solution = solvePart(RobotPaint.BLACK);
	var part2Solution = solvePart(RobotPaint.WHITE);

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePart(RobotPaint startingColor) {
	var hull = new Grid2D(".", AdjacencyRule.ROOK);
	var robot = new EmergencyHullPaintingRobot();
	var loc = new Coord2D(0, 0);
	hull.set(loc, (startingColor == RobotPaint.WHITE) ? "#" : ".");
	while (robot.isHalted() == false) {
	    var color = (hull.get(loc) == ".") ? RobotPaint.BLACK : RobotPaint.WHITE;
	    var moved = robot.move(new RobotMove(loc, color));
	    hull.set(loc, (moved.color() == RobotPaint.WHITE) ? "#" : ".");
	    loc = moved.position();
	}

	if (startingColor == RobotPaint.WHITE) {
	    hull.print();
	}

	int paintedPanelsCount = hull.getCoords().size();

	return String.valueOf(paintedPanelsCount);
    }
}

class EmergencyHullPaintingRobot {

    private static String _programDefn = "";

    public static void setProgramDefn(String defn) {
	_programDefn = defn;
    }

    private IntCodeComputer computer;
    private RobotDirection _dir = RobotDirection.UP;

    public EmergencyHullPaintingRobot() {
	computer = new IntCodeComputer(_programDefn);
    }

    public RobotDirection getDir() {
	return _dir;
    }

    public boolean isHalted() {
	return computer.isHalted;
    }

    public RobotMove move(RobotMove current) {
	computer.input.add((current.color() == RobotPaint.WHITE) ? 1L : 0L);
	computer.run(true, false);
	var color = (computer.output == 1L) ? RobotPaint.WHITE : RobotPaint.BLACK;

	computer.run(true, false);
	var turnDirection = (computer.output == 1L) ? RobotTurn.RIGHT : RobotTurn.LEFT;
	turn(turnDirection);

	var newPos = advance(current.position());

	//System.out.println("Moved to " + newPos + " painting " + color);
	return new RobotMove(newPos, color);
    }

    private void turn(RobotTurn t) {
	switch (_dir) {
	    case UP ->
		_dir = (t == RobotTurn.LEFT) ? RobotDirection.LEFT : RobotDirection.RIGHT;
	    case DOWN ->
		_dir = (t == RobotTurn.LEFT) ? RobotDirection.RIGHT : RobotDirection.LEFT;
	    case LEFT ->
		_dir = (t == RobotTurn.LEFT) ? RobotDirection.DOWN : RobotDirection.UP;
	    case RIGHT ->
		_dir = (t == RobotTurn.LEFT) ? RobotDirection.UP : RobotDirection.DOWN;
	}
    }

    private Coord2D advance(Coord2D pos) {
	Coord2D offset = new Coord2D(0, 0);
	switch (_dir) {
	    case UP ->
		offset = Grid2D.UP;
	    case DOWN ->
		offset = Grid2D.DOWN;
	    case LEFT ->
		offset = Grid2D.LEFT;
	    case RIGHT ->
		offset = Grid2D.RIGHT;
	}
	return pos.add(offset);
    }
}

record RobotMove(Coord2D position, RobotPaint color) {

}

enum RobotTurn {
    LEFT, RIGHT;
}

enum RobotPaint {
    WHITE, BLACK;
}

enum RobotDirection {
    UP, DOWN, LEFT, RIGHT;
}
