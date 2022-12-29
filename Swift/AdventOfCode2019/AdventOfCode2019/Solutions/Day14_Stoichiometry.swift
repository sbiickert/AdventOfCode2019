//
//  Day14_Stoichiometry.swift
//  AdventOfCode2019
//
//  Created by Simon Biickert on 2022-01-08.
//

import Foundation

struct Stoichiometry: AoCSolution {
	static func solve(filename: String) {
		print("\nDay 14 (Space Stoichiometry) -> \(filename)")
		let input = AoCUtil.readInputFile(named: filename, removingEmptyLines: true)
		
		let rxns = Reaction.parse(contentsOf: input)
		var result = generateFuel(reactions: rxns, requiredAmount: 1, oreBudget: Int.max)
		
		print("Part One")
		print("The amount of ORE needed for 1 FUEL is: \(result.ore)")
		
		result = generateFuel(reactions: rxns, requiredAmount: Int.max, oreBudget: 1000000000000)
		
		print("Part Two")
		print("Created \(result.fuel) FUEL from \(result.ore) ORE.")

	}
	
	static func runTests(filename: String) {
		print("\nDay 14 TEST (Space Stoichiometry) -> \(filename)")
		let groupedInput = AoCUtil.readGroupedInputFile(named: filename)
		
		print("Part One testing")
		for (i, group) in groupedInput.enumerated() {
			let rxns = Reaction.parse(contentsOf: group)
			let result = generateFuel(reactions: rxns, requiredAmount: 1, oreBudget: Int.max)
			print("\(i): Created \(result.fuel) FUEL from \(result.ore) ORE.")
		}
		
		print("Part Two testing")
		let i = 3
		//for i in 2..<groupedInput.count {
			let rxns = Reaction.parse(contentsOf: groupedInput[i])
			let result = generateFuel(reactions: rxns, requiredAmount: Int.max, oreBudget: 1000000000000)
			print("\(i): Created \(result.fuel) FUEL from \(result.ore) ORE.")
			//break
		//}
	}
	
	static func generateFuel(reactions: [Reaction], requiredAmount: Int, oreBudget: Int) -> (fuel:Int, ore:Int) {
		var lookup = Dictionary<String, Reaction>()
		for reaction in reactions {
			lookup[reaction.product.chem] = reaction
		}
		
		var needed = [ChemicalAmount]()
		var inventory = Dictionary<String, Int>()
		var oreCount = 0
		var fuelCount = 0
		
		while fuelCount < requiredAmount && oreCount < oreBudget {
			needed.append(ChemicalAmount(amt: 1, chem: "FUEL"))
			while needed.count > 0 {
				let need = needed.popLast()!
				if need.chem == "ORE" {
					oreCount += need.amt
					continue
				}
				var available = inventory[need.chem] ?? 0
				inventory[need.chem] = 0
				let provider = lookup[need.chem]!
				var nReactions = 0
				while available < need.amt {
					nReactions += 1
					available += provider.product.amt
				}
				if need.chem == "FUEL" {
					fuelCount += nReactions * provider.product.amt
				}
				inventory[need.chem] = available - need.amt // Used
				for reactant in provider.reactants {
					needed.append(ChemicalAmount(amt: reactant.amt * nReactions, chem: reactant.chem))
				}
			}
		}
		
		if oreCount > oreBudget {
			print("Went over budget for ore. Subtracting one fuel.")
			fuelCount -= 1
		}
		
		return (fuelCount, oreCount)
	}
	
	struct Reaction {
		static func parse(contentsOf input: [String]) -> [Reaction] {
			var result = [Reaction]()
			for line in input {
				result.append(Reaction.parse(line))
			}
			return result
		}
		
		static func parse(_ input: String) -> Reaction {
			// turn into space-delimited
			var modified = input.replacingOccurrences(of: " =>", with: "")
			modified = modified.replacingOccurrences(of: ",", with: "")
			let bits = modified.split(separator: " ")
			var chemAmts = [ChemicalAmount]()
			for i in stride(from: 0, to: bits.count, by: 2) {
				let ca = ChemicalAmount(amt: Int(bits[i])!, chem: String(bits[i+1]))
				chemAmts.append(ca)
			}
			let r = Reaction(product: chemAmts.last!, reactants: chemAmts.dropLast())
			return r
		}
		
 		let product: ChemicalAmount
		let reactants: [ChemicalAmount]
	}
	
	struct ChemicalAmount {
		let amt: Int
		let chem: String
	}
}
