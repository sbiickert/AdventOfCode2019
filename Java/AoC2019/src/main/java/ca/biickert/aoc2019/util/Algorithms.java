package ca.biickert.aoc2019.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sbiickert
 */
public class Algorithms {

    public static <E> List<List<E>> getAllPermutations(List<E> original) {
        if (original.isEmpty()) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = getAllPermutations(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    public static String reverseString(String s) {
	String r = "";
	for (int i = 0; i < s.length(); i++) {
	    r = s.charAt(0) + r;
	}
	return r;
    }
}
