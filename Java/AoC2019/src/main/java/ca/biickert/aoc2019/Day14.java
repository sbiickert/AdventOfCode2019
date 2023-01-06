package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	stockpile.put("ORE", 1000000000000L);

	ChemicalQuantity fuel = new ChemicalQuantity("FUEL", 1);
	generateChemical(fuel, recipes);

	return String.valueOf(1000000000000L - stockpile.get("ORE"));
    }

    private String solvePartTwo(Map<String, ChemicalFormula> recipes) {
	stockpile = new HashMap<>();
	stockpile.put("ORE", 1000000000000L);

	ChemicalQuantity fuel = new ChemicalQuantity("FUEL", 1);
	int fuelGenerated = 0;
	while (generateChemical(fuel, recipes)) {
	    fuelGenerated++;
	    stockpile.put("FUEL", 0L);
	    if (fuelGenerated % 1000 == 0) {
		System.out.println(fuelGenerated);
	    }
	}

	return String.valueOf(fuelGenerated);
    }

    private Map<String, Long> stockpile;

    private long getStockpileLevel(String chemical) {
	if (stockpile.containsKey(chemical) == false) {
	    return 0;
	}
	return stockpile.get(chemical);
    }

    private void addToStockpile(String chemical, int amount) {
	long oldLevel = getStockpileLevel(chemical);
	long newLevel = oldLevel + amount;
	//System.out.println(String.format("ADD %d %s to %d -> %d", amount, chemical, oldLevel, newLevel));
	stockpile.put(chemical, newLevel);
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

    private boolean generateChemical(ChemicalQuantity cq,
	    Map<String, ChemicalFormula> recipes) {
	//System.out.println("Generating " + cq.amount() + " " + cq.chemical());
	ChemicalFormula cf = recipes.get(cq.chemical());
	boolean success = true;
	while (getStockpileLevel(cq.chemical()) < cq.amount()) {
	    for (var input : cf.inputs()) {
		long needed = (long) input.amount() - getStockpileLevel(input.chemical());
		//System.out.println("Need " + needed + " " + input.chemical());
		if (needed > 0) {
		    if (input.chemical().equals("ORE")) {
			// We're out of ORE!
			return false;
		    }
		    success = success && generateChemical(new ChemicalQuantity(input.chemical(), input.amount()), recipes);
		    if (!success) {
			return success;
		    }
		}
		takeFromStockpile(input.chemical(), input.amount());
	    }
	    addToStockpile(cq.chemical(), cf.product().amount());
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
