package aoc2025

import java.io.File
import java.io.InputStream

private val lineList = mutableListOf<CharArray>()

private fun readInput(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    inputStream.bufferedReader().forEachLine { lineList.add(it.toCharArray()) }
}

//private var total = 0L

private val nodePathMap = mutableMapOf<Pair<Int,Int>, Long>()

private fun part1(): Int {
    // Work down row at a time. Starting with S and for each ^, record the X index of the splits.
    var beams = mutableSetOf<Int>()
    var total = 0
    beams.add(lineList[0].indexOf('S'))

    for (y in 1..<lineList.size) {
        // Draw existing beams to visualize. 
        for (x in beams) {
            if (lineList[y][x] == '.') {
                lineList[y][x] = '|'
            }
        }
        // Split the beam.
        for (x in 0..<lineList[y].size) {
            if (lineList[y][x] == '^' && x in beams) {
                beams = newBeams(x, y, beams)
                total++
            }
        }
    }

    return total
}

private fun newBeams(x: Int, y: Int, set: Set<Int>): MutableSet<Int> {
    val s = mutableSetOf<Int>()
    s.addAll(set)
    
    // New split left.
    if (inbounds(x - 1) && lineList[y][x - 1] in "!.") {
        lineList[y][x - 1] = '!'
        s.add(x - 1)
    }
    // New split right.
    if (inbounds(x + 1) && lineList[y][x + 1] in "!.") {
        lineList[y][x + 1] = '!'
        s.add(x + 1)
    }
    // Remove this element as we have split.
    s.remove(x)
    
    return s
}

private fun inbounds(x: Int): Boolean {
    // Pointless check really as input cant go OOB.
    return (x in 0..lineList[0].size)
}

private fun part2(): Long {

    // Starting from the S, recurse paths to the bottom row (list size).
    return rescurseBeamTree(lineList[0].indexOf('S'), 1, 0)
}

private fun rescurseBeamTree(x: Int, y: Int, total: Int) : Long {
    
    // Short circuit if possible.
    if (nodePathMap.containsKey(Pair(x,y))) {
        return nodePathMap[Pair(x,y)]!!.toLong()
    }
    
    // Reached the bottom, increment this as a new path. 
    if (y == lineList.size) {
        return total+1L
    }

    // Attempt to go down.
    if (lineList[y][x] in "|!") {
        return rescurseBeamTree(x, y + 1, total)
    }
    
    // Go left then right and record this recursion total to the map to save future deep recursive cycles.
    val pathsFromHere = rescurseBeamTree(x - 1, y+1, total) + rescurseBeamTree(x + 1, y+1, total)
    nodePathMap[Pair(x,y)] = pathsFromHere
    
    return pathsFromHere
}

fun main(args: Array<String>) {
    readInput(args[0])
    println(part1()) //1504
    println(part2()) // 5137133207830
}