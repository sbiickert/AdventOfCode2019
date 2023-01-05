package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.AdjacencyRule;
import ca.biickert.aoc2019.spatial.Coord2D;
import ca.biickert.aoc2019.spatial.Grid2D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.IntCodeComputer;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sjb
 */
public class Day13 extends Solution {

    public Day13() {
	super(13, "Care Package", true);
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

    private String solvePartOne(String pgm) {
	ArcadeCabinet game = new ArcadeCabinet(pgm);
	game.startGame(0);
	game.runGame(false);
	game.getScreen().print();

	List<Coord2D> blocks = game.getScreen().getCoordsWithValue("#");

	return String.valueOf(blocks.size());
    }

    private String solvePartTwo(String pgm) {
	ArcadeCabinet game = new ArcadeCabinet(pgm);
	game.startGame(2);
	while (game.gameIsRunning()) {
	    game.runGame(true);

	    int ballX = game.getLastBallPosition().getX();
	    int paddleX = game.getLastPaddlePosition().getX();

	    if (ballX < paddleX) {
		game.setJoystickPosition(ArcadeCabinet.LEFT);
	    } else if (ballX > paddleX) {
		game.setJoystickPosition(ArcadeCabinet.RIGHT);
	    } else {
		game.setJoystickPosition(ArcadeCabinet.CENTER);
	    }

	}
	game.getScreen().print();

	return String.valueOf(game.getScore());
    }

}

class ArcadeCabinet {

    static final long LEFT = -1L;
    static final long CENTER = 0L;
    static final long RIGHT = 1L;
    static final Map<Integer, String> charForCode;

    static {
	charForCode = Map.of(0, ".", 1, "|", 2, "#", 3, "=", 4, "O");
    }

    final private IntCodeComputer computer;
    final private String program;
    final private Grid2D screen;

    private Coord2D lastPaddlePosition;
    private Coord2D lastBallPosition;
    private long joystickPosition;
    private int score;

    public ArcadeCabinet(String programDefn) {
	program = programDefn;
	computer = new IntCodeComputer(programDefn);
	screen = new Grid2D(".", AdjacencyRule.ROOK);
    }

    public Grid2D getScreen() {
	return screen;
    }

    public int getScore() {
	return score;
    }

    public Coord2D getLastPaddlePosition() {
	return lastPaddlePosition;
    }

    public Coord2D getLastBallPosition() {
	return lastBallPosition;
    }

    public long getJoystickPosition() {
	return joystickPosition;
    }

    public void setJoystickPosition(long joystickPosition) {
	this.joystickPosition = joystickPosition;
    }

    public void startGame(int quarters) {
	computer.load(program);
	if (quarters > 0) {
	    computer.setValue(0, 2L, 0);
	}
	score = 0;
	screen.clearAll();
	joystickPosition = CENTER;
    }

    public void runGame(boolean allowUserInput) {
	computer.input.add(getJoystickPosition());

	while (true) {
	    computer.run(true, false);
	    if (computer.isHalted) {
		break;
	    }
	    int x = computer.output.intValue();

	    computer.run(true, false);
	    if (computer.isHalted) {
		break;
	    }
	    int y = computer.output.intValue();

	    computer.run(true, false);
	    if (computer.isHalted) {
		break;
	    }
	    int tile = computer.output.intValue();

	    if (x == -1 && y == 0) {
		score = tile;
	    } else {
		Coord2D loc = new Coord2D(x, y);
		String glyph = charForCode.get(tile);
		screen.set(loc, glyph);

		if (glyph.equals("O")) {
		    lastBallPosition = loc;
		}
		if (glyph.equals("=")) {
		    lastPaddlePosition = loc;
		}
	    }
	    // Here is where the user can change the joystick setting
	    if (allowUserInput && getLastBallPosition() != null && getLastPaddlePosition() != null) {
		//screen.print();
		//wait(100);
		break;
	    }
	}

	computer.input.clear();
    }

    private void wait(int ms) {
	try {
	    Thread.sleep(ms);
	} catch (InterruptedException ex) {
	    Thread.currentThread().interrupt();
	}
    }

    public boolean gameIsRunning() {
	return !computer.isHalted;
    }
}
