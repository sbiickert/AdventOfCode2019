
package ca.biickert.aoc2019.util;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author sjb
 */
public class InputReader {
    public static List<String> readInputFile(String filename) {
        List<String> input = new ArrayList<>();
        try {
            input = Files.readAllLines(Paths.get(filename));
        }
        catch (java.io.IOException ioe) {
	    System.out.println(String.format("Could not read input from %s: %s", filename, ioe));
        }
	return input;
    }
    
    public static List<String> readGroupedInputFile(String filename, int groupIndex) {
	var groups = readGroupedInputFile(filename);
	if (groupIndex < 0 || groupIndex >= groups.size()) {
	    System.out.println(String.format("readGroupedInputFile: Valid groups are 0 to %d", groups.size()-1));
	    //throw new IndexOutOfBoundsException(String.format("Valid groups are 0 to %d", groups.size()-1));
	    return new ArrayList<String>();
	}
	return groups.get(groupIndex);
    }
    
    public static List< List<String> > readGroupedInputFile(String filename) {
        var input = readInputFile(filename);
        List<List<String>> groups = new ArrayList<>();
	
	List<String> group = new ArrayList<>();
	for (String line : input) {
	    if (line.length() == 0) {
		groups.add(group);
		group = new ArrayList<>();
	    }
	    else {
		group.add(line);
	    }
	}
	
	if (!group.isEmpty()) {
	    groups.add(group);
	}
	
	return groups;
    }
}
