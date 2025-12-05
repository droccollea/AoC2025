package aoc2025

import java.io.File
import java.io.InputStream

private val lineList = mutableListOf<String>()

private fun readInput(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
}

private fun part1(): Long {
    var total = 0L
    val ranges = extractRanges()
    val currentLinePos = ranges.size

    // Tally remaining items of the list are within a range.
    for (l in currentLinePos + 1..<lineList.size) {
        for (r in ranges) {
            if (lineList[l].toLong() in r.first..r.second) {
                total++
                break // Don't check further ranges.
            }
        }
    }

    return total
}

private fun extractRanges(): MutableList<Pair<Long, Long>> {
    val ranges = mutableListOf<Pair<Long, Long>>()
    var currentLinePos = 0

    // Extract ranges to a list of range start and end pairs.
    var currentLine = lineList[currentLinePos]
    do {
        val splits = currentLine.split("-")
        ranges.add(Pair(splits[0].toLong(), splits[1].toLong()))

        currentLinePos++
        currentLine = lineList[currentLinePos]
    } while (currentLine != "")

    return ranges

}
private fun part2(): Long {
    var total = 0L
    val ranges = extractRanges()
    val consolidatedRanges = mutableSetOf<Pair<Long, Long>>()

    // Consolidate the ranges and count the inclusive items within.
    for (r in ranges) {
        // Create a new range with the lowest start and highest end.
        // A range may be within yet another range so
        // recursively check for a lower range start/end and build a new range.
        // Add to a set for uniqueness.
        val lowestStart = recursivelyFindLowestStart(ranges,r.first)
        val highestEnd = recursivelyFindHighestEnd(ranges, r.second)
        consolidatedRanges.add(Pair(lowestStart, highestEnd))
    }

    // Count the new ranges' spans.
    for (r in consolidatedRanges) {
        total += r.second+1 - r.first
    }
    return total
}

private fun recursivelyFindLowestStart(ranges: MutableList<Pair<Long, Long>>, start: Long) : Long {
    val lowestStart  = ((ranges.filter { r -> start in r.first..r.second }).sortedBy { (lng, lng1) -> lng }).first().first
    return if (lowestStart == start) {
        lowestStart
    }
    else {
        recursivelyFindLowestStart(ranges, lowestStart)
    }
}

private fun recursivelyFindHighestEnd(ranges: MutableList<Pair<Long, Long>>, end: Long) : Long {
    val highestEnd = ((ranges.filter { r -> end in r.first..r.second }).sortedBy { (lng, lng1) -> lng1 }).last().second
    return if (highestEnd == end) {
        highestEnd
    }
    else {
        recursivelyFindHighestEnd(ranges, highestEnd)
    }
}

fun main(args: Array<String>) {
    readInput(args[0])
//    println(part1()) //737
    println(part2()) //357485433193284
}