/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        List<String> input = new ArrayList<String>();
        try {
            input = Files.readAllLines(Paths.get(filename));
        }
        catch (java.io.IOException ioe) {
	    System.out.println(String.format("Could not read input from %s: %s", filename, ioe));
        }
	return input;
    }
}
