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

    public static List<List<String>> getCombinations(String arr[], int n, int r) {
	String data[] = new String[r];
	List<List<String>> result = new ArrayList<>();
	combinationUtil(arr, n, r, 0, data, 0, result);
	return result;
    }
    
    /*	arr[]  ---> Input Array
	data[] ---> Temporary array to store current combination
	start & end ---> Starting and Ending indexes in arr[]
	index  ---> Current index in data[]
	r ---> Size of a combination to be printed 
     */
    private static void combinationUtil(String arr[], int n, int r, int index, String data[], int i, List<List<String>> out) {
	// Current combination is ready to be printed, print it
	if (index == r) {
	    List<String> combo = new ArrayList<>();
	    for (int j = 0; j < r; j++) {
		combo.add(data[j]);
	    }
	    out.add(combo);
	    return;
	}

	// When no more elements are there to put in data[]
	if (i >= n) {
	    return;
	}

	// current is included, put next at next location
	data[index] = arr[i];
	combinationUtil(arr, n, r, index + 1, data, i + 1, out);

	// current is excluded, replace it with next (Note that
	// i+1 is passed, but index is not changed)
	combinationUtil(arr, n, r, index, data, i + 1, out);
    }
}
