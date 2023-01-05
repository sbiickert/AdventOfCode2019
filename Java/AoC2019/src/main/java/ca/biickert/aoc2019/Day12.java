package ca.biickert.aoc2019;

import ca.biickert.aoc2019.spatial.Coord3D;
import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author sjb
 */
public class Day12 extends Solution {

    public Day12() {
	super(12, "The N-Body Problem", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	var part1Solution = solvePartOne(input);
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(List<String> input) {
	List<Moon> moons = new ArrayList<>();
	for (var line : input) {
	    moons.add(new Moon(line));
	}
	
	for (int i = 1; i <= 1000; i++) {
	    for (int a = 0; a < moons.size()-1; a++) {
		for (int b = a+1; b < moons.size(); b++) {
		    moons.get(a).interactWith(moons.get(b));
		}
	    }

	    for (var moon : moons) {
		moon.applyVelocity();
	    }
	}
	
	int totalEnergy = 0;
	for (var moon : moons) {
	    totalEnergy += moon.getKineticEnergy() * moon.getPotentialEnergy();
	}
	
	return String.valueOf(totalEnergy);
    }

    private String solvePartTwo() {
	return "";
    }

}

class Moon {
    static final Pattern REGEX = Pattern.compile("x=([\\-\\d]+), y=([\\-\\d]+), z=([\\-\\d]+)");
    Coord3D pos;
    Coord3D vel;
    
    public Moon(String defn) {
	vel = new Coord3D(0,0,0);
	var matcher = REGEX.matcher(defn);
	if (matcher.find()) {
	    int x = Integer.parseInt(matcher.group(1));
	    int y = Integer.parseInt(matcher.group(2));
	    int z = Integer.parseInt(matcher.group(3));
	    pos = new Coord3D(x,y,z);
	}
	else {
	    System.out.println("regex didn't work on " + defn);
	}
    }
    
    public void interactWith(Moon other) {
	int dx = compare(pos.getX(), other.pos.getX());
	int dy = compare(pos.getY(), other.pos.getY());
	int dz = compare(pos.getZ(), other.pos.getZ());
	vel.setX(vel.getX() + dx);
	vel.setY(vel.getY() + dy);
	vel.setZ(vel.getZ() + dz);
	other.vel.setX(other.vel.getX() - dx);
	other.vel.setY(other.vel.getY() - dy);
	other.vel.setZ(other.vel.getZ() - dz);
    }
    
    public void applyVelocity() {
	pos = pos.add(vel);
    }
    
    public int getKineticEnergy() {
	return vel.manhattanDistanceTo(new Coord3D(0,0,0));
    }
    
    public int getPotentialEnergy() {
	return pos.manhattanDistanceTo(new Coord3D(0,0,0));
    }
    
    private int compare(int a, int b) {
	if (a < b) { return 1; }
	if (a == b) { return 0; }
	return -1; // if (b < a)
    }
    
    @Override
    public String toString() {
	return "pos: " + pos + " vel: " + vel;
    }
}
