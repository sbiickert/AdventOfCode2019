package ca.biickert.aoc2019;

import ca.biickert.aoc2019.util.InputReader;
import ca.biickert.aoc2019.util.Result;
import ca.biickert.aoc2019.util.Solution;
import static java.lang.String.valueOf;
import static java.lang.System.lineSeparator;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static java.math.BigInteger.ZERO;
import java.math.BigInteger;
import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toCollection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
	var part2Solution = solvePartTwo(String.join("\n", input),
		119315717514047L,
		101741582076661L);

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

    private String solvePartTwo(String input, long nCards, long nShuffles) {
	var sas = new SaidAspenSolution(input, BigInteger.valueOf(nCards), BigInteger.valueOf(nShuffles));
	
	return sas.cardAt(BigInteger.valueOf(2020L)).toString();
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
	} else if (s.process.equals(Shuffle.DEAL)) {
	    deal(s.size);
	} else {
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

/*
I don't claim to understand the math involved, and barely understand the code.
But I learned some new things from it.
https://github.com/saidaspen/aoc2019/blob/master/java/src/main/java/se/saidaspen/aoc2019/day22/Day22.java
*/
class SaidAspenSolution {

    private static final String ERR_OP = "Unsupported shuffle operation '%s'";

    private static final String DEAL_INTO = "deal into new stack";
    private static final String CUT = "cut";
    private static final String DEAL_W_INC = "deal with increment";

    private final String input;
    private final BigInteger nCards, nShuffles;

    public SaidAspenSolution(String input, BigInteger nCards, BigInteger nShuffles) {
	this.input = input;
	this.nCards = nCards;
	this.nShuffles = nShuffles;
    }

    /**
     * Represents any linear function that can be written f(x)=kx+m
     */
    public static class LinFunc {

	BigInteger k, m;

	public LinFunc(BigInteger k, BigInteger m) {
	    this.k = k;
	    this.m = m;
	}

	BigInteger apply(BigInteger x) {
	    return x.multiply(k).add(m);
	}
    }

    /* The identity function: f(x)=x */
    private static final LinFunc ID = new LinFunc(ONE, ZERO);

    /**
     * Aggregate two functions f(x) and g(x) to create a new function
     * h(x)=g(f(x))
     */
    private LinFunc agg(LinFunc f, LinFunc g) {
	// Let f(x)=k*x+m and g(x)=j*x+n, then h(x) = g(f(x)) = Ax+B = j*(k*x+m)+n = j*k*x + (j*m+n) => A=j*k, B=j*m+n
	return new LinFunc(g.k.multiply(f.k), g.k.multiply(f.m).add(g.m));
    }

    BigInteger positionOf(BigInteger in) {
	LinFunc shuffle = stream(input.split(lineSeparator()))
		.filter(s -> !"".equalsIgnoreCase(s))
		.map(s -> {
		    if (s.startsWith(DEAL_INTO)) {
			return new LinFunc(ONE.negate(), ONE.negate());
		    } else if (s.startsWith(CUT)) {
			return new LinFunc(ONE, argOf(s).mod(nCards).negate());
		    } else if (s.startsWith(DEAL_W_INC)) {
			return new LinFunc(argOf(s), ZERO);
		    }
		    throw new RuntimeException(String.format(ERR_OP, s));
		})
		.reduce(ID, this::agg); // Create one func of all shuffle operations, i.e. like: f(x)=f1((f2(f3(x)))
	return executeTimes(shuffle.k, shuffle.m, nShuffles).apply(in).mod(nCards);
    }

    BigInteger cardAt(BigInteger in) {
	LinFunc shuffle = reverse(stream(input.split(lineSeparator()))
		.filter(s -> !"".equalsIgnoreCase(s))
		.map(s -> {
		    if (s.startsWith(DEAL_INTO)) {
			return new LinFunc(ONE.negate(), ONE.negate().subtract(nCards));
		    } else if (s.startsWith(CUT)) {
			return new LinFunc(ONE, argOf(s).mod(nCards));
		    } else if (s.startsWith(DEAL_W_INC)) {
			BigInteger z = argOf(s).modInverse(nCards);
			return new LinFunc(ONE.multiply(z).mod(nCards), ZERO);
		    }
		    throw new RuntimeException(String.format(ERR_OP, s));
		}))
		.reduce(ID, this::agg); // Create one func of all shuffle operations, i.e. like: f(x)=f1((f2(f3(x)))
	return executeTimes(shuffle.k, shuffle.m, nShuffles).apply(in).mod(nCards);
    }

    /**
     * Strips everything out of a string except for a number and then creates a
     * BigInteger out of it
     */
    private BigInteger argOf(String s) {
	return new BigInteger(s.replaceAll("[^-?0-9]+", ""));
    }

    public static <T> Stream<T> reverse(Stream<T> stream) {
	Iterable<T> iterable = () -> stream.collect(toCollection(LinkedList::new)).descendingIterator();
	return StreamSupport.stream(iterable.spliterator(), false);
    }

    private LinFunc executeTimes(BigInteger k, BigInteger m, BigInteger nShuffles) {
	if (nShuffles.equals(ZERO)) {
	    return ID;
	} else if (nShuffles.mod(TWO).equals(ZERO)) {
	    return executeTimes(k.multiply(k).mod(nCards), k.multiply(m).add(m).mod(nCards), nShuffles.divide(TWO));
	} else {
	    LinFunc cd = executeTimes(k, m, nShuffles.subtract(ONE));
	    return new LinFunc(k.multiply(cd.k).mod(nCards), k.multiply(cd.m).add(m).mod(nCards));
	}
    }

}
