package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author sjb
 */
public class Day14 extends Solution {

    public Day14() {
	super(14, "Space Stoichiometry", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);
	var recipes = parseRecipes(input);

	var part1Solution = solvePartOne(recipes);
	var part2Solution = solvePartTwo(recipes);

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(Map<String, ChemicalFormula> recipes) {
	stockpile = new HashMap<>();
	tracking = new HashMap<>();

	generateChemical("FUEL", 1, recipes);

	return String.valueOf(tracking.get("ORE"));
    }

    private String solvePartTwo(Map<String, ChemicalFormula> recipes) {
	stockpile = new HashMap<>();
	tracking = new TreeMap<>();

	int[] trend = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	int fuelGenerated = 0;
	while (generateChemical("FUEL", 1, recipes)) {
	    fuelGenerated += stockpile.get("FUEL");
	    stockpile.put("FUEL", 0L);
	    if (fuelGenerated % 1000 == 0) {
		System.out.println(fuelGenerated);
		for (int i = trend.length-1; i > 0; i--) {
		    trend[i] = trend[i-1];
		}
		var avg = (tracking.get("ORE") / (double)fuelGenerated);
		trend[0] = (int)Math.round(1000000000000L / avg);
		//System.out.println("ORE per FUEL: " + avg);
		System.out.println("Projected FUEL from 1 Trillion ORE: " + trend[0]);
		int largest = 0;
		for (int i = 0; i < trend.length; i++) {
		    largest = Math.max(largest, trend[i]);
		}
		// Looking for a run of 10 identical projections
		boolean agreement = true;
		for (int i = 1; i < 10; i++) {
		    agreement = agreement && trend[i] == trend[0];
		}
		if (agreement) {
		    return String.valueOf(largest);
		}
	    }
	}

	return "Did not find trend.";
    }

    private Map<String, Long> stockpile;
    private Map<String, Long> tracking;

    private long getStockpileLevel(String chemical) {
	if (stockpile.containsKey(chemical)) {
	    return stockpile.get(chemical);
	}
	return 0;
    }

    private void addToStockpile(String chemical, int amount) {
	long oldLevel = getStockpileLevel(chemical);
	long newLevel = oldLevel + amount;
	//System.out.println(String.format("ADD %d %s to %d -> %d", amount, chemical, oldLevel, newLevel));
	stockpile.put(chemical, newLevel);
	tracking.put(chemical, getTrackingLevel(chemical) + amount);
    }

    private long getTrackingLevel(String chemical) {
	if (tracking.containsKey(chemical)) {
	    return tracking.get(chemical);
	}
	return 0;
    }

    private void takeFromStockpile(String chemical, int amount) {
	long oldLevel = getStockpileLevel(chemical);
	long newLevel = oldLevel - amount;
	//System.out.println(String.format("TAKE %d %s from %d -> %d", amount, chemical, oldLevel, newLevel));
	stockpile.put(chemical, newLevel);
	if (stockpile.get(chemical) < 0) {
	    System.out.println("Took too much " + chemical);
	    System.exit(1);
	}
    }

    private boolean generateChemical(String chemical, int amount,
	    Map<String, ChemicalFormula> recipes) {
	//System.out.println("Generating " + cq.amount() + " " + cq.chemical());
	ChemicalFormula cf = recipes.get(chemical);
	boolean success = true;
	while (getStockpileLevel(chemical) < amount) {
	    for (var input : cf.inputs()) {
		long needed = (long) input.amount() - getStockpileLevel(input.chemical());
		//System.out.println("Need " + needed + " " + input.chemical());
		if (needed > 0) {
		    if (input.chemical().equals("ORE")) {
			addToStockpile("ORE", (int)needed);
			// Are we out of ORE?
			success = success && (getTrackingLevel("ORE") < 1000000000000L);
		    } else {
			success = success && generateChemical(input.chemical(), input.amount(), recipes);
		    }
		    if (!success) {
			return success;
		    }
		}
		takeFromStockpile(input.chemical(), input.amount());
	    }
	    addToStockpile(chemical, cf.product().amount());
	}
	return success;
    }

    private Map<String, ChemicalFormula> parseRecipes(List<String> input) {
	Map<String, ChemicalFormula> result = new HashMap<>();
	for (var line : input) {
	    var csv = line.replace(" => ", ", ");
	    var cqStrings = csv.split(", ");
	    var product = parseChemicalQuantity(cqStrings[cqStrings.length - 1]);
	    List<ChemicalQuantity> inputs = new ArrayList<>();
	    for (int i = 0; i < cqStrings.length - 1; i++) {
		inputs.add(parseChemicalQuantity(cqStrings[i]));
	    }
	    var formula = new ChemicalFormula(product, inputs);
	    result.put(product.chemical(), formula);
	}
	return result;
    }

    private ChemicalQuantity parseChemicalQuantity(String s) {
	var x = s.split(" ");
	return new ChemicalQuantity(x[1], Integer.parseInt(x[0]));
    }
}

record ChemicalFormula(ChemicalQuantity product, List<ChemicalQuantity> inputs) {

}

record ChemicalQuantity(String chemical, int amount) {

}
