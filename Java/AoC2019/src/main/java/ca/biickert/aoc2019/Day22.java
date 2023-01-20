package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author sjb
 */
public class Day22 extends Solution {

    public Day22() {
	super(22, "Slam Shuffle", true);
    }

    @Override
    public Result solve(String filename, int index) {
	var result = super.solve(filename, index);

	List<String> input = InputReader.readGroupedInputFile(filename, index);

	List<Shuffle> shuffles = new ArrayList<>();
	for (var line : input) {
	    shuffles.add(new Shuffle(line));
	}
	
	int size = (shuffles.size() < 20) ? 10 : 10007;
	var part1Solution = solvePartOne(shuffles, size);
	var part2Solution = solvePartTwo();

	result = new Result(part1Solution, part2Solution);

	return result;
    }

    private String solvePartOne(List<Shuffle> shuffles, int size) {
	var deck = new SpaceDeck(size);
	
	for (var s : shuffles) {
	    //System.out.println(s);
	    deck.doShuffle(s);
	    //System.out.println(deck);
	}
	
	if (size == 10) {
	    return deck.toString();
	}
	return String.valueOf(deck.findCard(2019));
    }

    private String solvePartTwo() {
	return "";
    }

}

class SpaceDeck {

    private final List<Integer> cards;
    private final List<Integer> swap;

    public SpaceDeck(int size) {
	cards = new ArrayList<>();
	swap = new ArrayList<>();
	for (int i = 0; i < size; i++) {
	    cards.add(i);
	    swap.add(0);
	}
    }
    
    public int findCard(int value) {
	return cards.indexOf(value);
    }
    
    public void doShuffle(Shuffle s) {
	if (s.process.equals(Shuffle.CUT)) {
	    cut(s.size);
	}
	else if (s.process.equals(Shuffle.DEAL)) {
	    deal(s.size);
	}
	else {
	    dealIntoNew();
	}
    }
    
    private void cut(int size) {
	Collections.rotate(cards, -1 * size);
    }
    
    private void dealIntoNew() {
	Collections.reverse(cards);
    }
    
    private void deal(int size) {
	Collections.copy(swap, cards);
	int count = 0;
	int ptr = 0;
	while (count < cards.size()) {
	    cards.set(ptr, swap.get(count));
	    ptr += size;
	    ptr = ptr % cards.size();
	    count++;
	}
    }
    
    @Override
    public String toString() {
	return String.join(" ", cards.stream().map(String::valueOf).toList());
    }
}

class Shuffle {

    static final String CUT = "cut";
    static final String NEW = "new stack";
    static final String DEAL = "deal";

    int size;
    String process;

    public Shuffle(String defn) {
	if (defn.startsWith("deal into")) {
	    size = 0;
	    process = NEW;
	    return;
	}
	var words = defn.split(" ");
	if (words[0].equals("deal")) {
	    process = DEAL;
	} else {
	    process = CUT;
	}
	size = Integer.parseInt(words[words.length - 1]);
    }
    
    @Override
    public String toString() {
	return "Shuffle " + process + " " + size;
    }
}
