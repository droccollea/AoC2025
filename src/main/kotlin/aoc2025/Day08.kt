package aoc2025

import java.io.File
import java.io.InputStream
import kotlin.math.pow
import kotlin.math.sqrt

private val lineList = mutableListOf<String>()

private fun readInput(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
}

private fun part1(loops: Int): Long {

    // Add all as single "circuits".
    var circuits = mutableSetOf<MutableSet<String>>()
    lineList.forEach { l -> circuits.add(mutableSetOf(l)) }
    
    // Build the map of pairs with distance.
    var closestPairs = mutableListOf<Pair<String, Double>>()
    
    for (i in 0..<lineList.size) {
        for (j in i + 1..<lineList.size) {
            val distance = compareDistances(lineList[i], lineList[j])
            closestPairs.add(Pair(lineList[i] + "|" + lineList[j], distance))
        }
    }
    println("Distances calculated for ${closestPairs.size} pairs.")
    
    // Sort on distance.
    closestPairs = closestPairs.sortedBy { (_,v) -> v }.toMutableList()
    println("Distances sorted for ${closestPairs.size} pairs.")

    var count = 0
    while (count < loops) {
        val (pointA,pointB) = closestPairs[count].first.split('|')
        
        // Fold circuits.
        val dups = circuits.filter { it.contains(pointA) || it.contains(pointB) }.sortedBy { it.size }.reversed()
        for (d in 1..<dups.size) {
            dups[0].addAll(dups[d])
            dups[d].clear()
        }
        // Discard the empty sets.
        circuits = circuits.filter { it.size >0 }.toMutableSet()
        count++
    }
    
    // Sort circuits by size and multiply top 3 for answer.
    var total = 1L
    val t = circuits.sortedBy { l -> l.size }.reversed()
    for (i in 0..<3) {
        total *= t[i].size
    }

    return total
}

private fun compareDistances(a: String, b: String): Double {
    val (aX, aY, aZ) = a.split(',').map { t -> t.toInt() }
    val (bX, bY, bZ) = b.split(',').map { t -> t.toInt() }

    return sqrt(
        ((bX - aX).toDouble().pow(2))
                + ((bY - aY).toDouble().pow(2))
                + ((bZ - aZ).toDouble().pow(2))
    )
}

fun main(args: Array<String>) {
    readInput(args[0])
//    println(part1(10)) // 10 for example.
    println(part1(1000)) // 75680
//    println(part2()) //
}