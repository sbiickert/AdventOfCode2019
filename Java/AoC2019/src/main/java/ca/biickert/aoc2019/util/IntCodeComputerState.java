
package ca.biickert.aoc2019.util;

import java.util.List;

public record IntCodeComputerState(List<Long> program, int ptr, int relativeBase, List<Long> input, Long output, boolean isHalted) {}