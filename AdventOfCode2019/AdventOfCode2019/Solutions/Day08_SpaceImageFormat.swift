//
//  Day08_SpaceImageFormat.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-03.
//

import Foundation
import Algorithms

struct SpaceImageFormat: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 08 (Space Image Format) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let image = SpaceImage(width: 25, height: 6)
		input[0].forEach({image.addData(pixel: Int(String($0))!)})
		var leastZeros: [Int]?
		for l in 0..<image._data[0].count {
			let h = image.histogram(layer: l)
			if leastZeros == nil || h[0] < leastZeros![0] {
				leastZeros = h
			}
		}
		
		print("Part One")
		print("The number of ones times twos in the layer with least zeros is: \(leastZeros![1] * leastZeros![2])")
		
		print("Part Two")
		image.draw()
	}
	
	static func runTests(filename: String) {
		print("\nDay 08 TEST (Space Image Format) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		var image = SpaceImage(width: 3, height: 2)
		input[0].forEach({image.addData(pixel: Int(String($0))!)})
		
		image = SpaceImage(width: 2, height: 2)
		input[1].forEach({image.addData(pixel: Int(String($0))!)})
		image.draw()
	}
	
	class SpaceImage {
		let width: Int
		let height: Int
		
		private var ptr: Int
		var _data: [[Int]]
		var max: Int = 0
		
		init(width w:Int, height h: Int) {
			width = w
			height = h
			_data = [[Int]](repeating: [Int](), count: width * height)
			ptr = 0
		}
		
		func addData(pixel: Int) {
			_data[ptr].append(pixel)
			ptr += 1
			if ptr >= _data.count {
				ptr = 0
			}
			if pixel > self.max {
				self.max = pixel
			}
		}
		
		func histogram(layer: Int) -> [Int] {
			var h = [Int](repeating: 0, count: self.max + 1)
			for pixel in self._data {
				let value = pixel[layer]
				h[value] += 1
			}
			return h
		}
		
		func draw() {
			for r in 0..<height {
				var line = ""
				for c in 0..<width {
					let color = getColor(row: r, col: c)
					line += (color == 0) ? " " : "#"
				}
				print(line)
			}
		}
		
		func getColor(row:Int, col: Int) -> Int {
			let pixel = _data[row * width + col]
			var color = 2 // transparent
			for layer in pixel {
				color = layer
				if color != 2 {break}
			}
			return color
		}
	}
}
