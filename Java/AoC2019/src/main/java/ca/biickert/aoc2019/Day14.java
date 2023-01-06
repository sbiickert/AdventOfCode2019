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
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(Map<String, ChemicalFormula> recipes) {
	stockpile = new HashMap<>();

	ChemicalQuantity fuel = new ChemicalQuantity("FUEL", 1);
	generateChemical(fuel, recipes);

	return String.valueOf(oreConsumed);
    }

    private String solvePartTwo() {
	return "";
    }

    private int oreConsumed = 0;
    private Map<String, Integer> stockpile;
    
    private int getStockpileLevel(String chemical) {
	if (stockpile.containsKey(chemical) == false) {
	    return 0;
	}
	return stockpile.get(chemical);
    }
    
    private void addToStockpile(String chemical, int amount) {
	int oldLevel = getStockpileLevel(chemical);
	int newLevel = oldLevel + amount;
	//System.out.println(String.format("ADD %d %s to %d -> %d", amount, chemical, oldLevel, newLevel));
	stockpile.put(chemical, newLevel);
    }
    
    private void takeFromStockpile(String chemical, int amount) {
	int oldLevel = getStockpileLevel(chemical);
	int newLevel = oldLevel - amount;
	//System.out.println(String.format("TAKE %d %s from %d -> %d", amount, chemical, oldLevel, newLevel));
	stockpile.put(chemical, newLevel);
	if (stockpile.get(chemical) < 0) {
	    System.out.println("Took too much " + chemical);
	    System.exit(1);
	}
    }

    private void generateChemical(ChemicalQuantity cq,
	    Map<String, ChemicalFormula> recipes) {
	//System.out.println("Generating " + cq.amount() + " " + cq.chemical());
	ChemicalFormula cf = recipes.get(cq.chemical());
	while (getStockpileLevel(cq.chemical()) < cq.amount()) {
	    for (var input : cf.inputs()) {
		int needed = input.amount() - getStockpileLevel(input.chemical());
		if (needed > 0) {
		    //System.out.println("Need " + needed + " " + input.chemical());
		    if (input.chemical().equals("ORE")) {
			oreConsumed += input.amount();
			addToStockpile("ORE", needed);
		    } else {
			generateChemical(new ChemicalQuantity(input.chemical(), input.amount()), recipes);
		    }
		}
		takeFromStockpile(input.chemical(), input.amount());
	    }
	    addToStockpile(cq.chemical(), cf.product().amount());
	}
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
