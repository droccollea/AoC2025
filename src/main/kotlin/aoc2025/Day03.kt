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

    for (line in lineList) {
        var tens = line[0]
        var tensPos = 0

        // Get the highest 10s and record its position.
        for (x in 1..<line.length-1) {
            if (line[x] > tens) {
                tens = line[x]
                tensPos = x
            }
        }
        var units = '0'
        // From the tens pos, search the remainder for a higher units value.
        for (x in tensPos+1..<line.length) {
            if (line[x] > units) {
                units = line[x]
            }
        }
        total += "$tens$units".toLong()
    }
    return total
}

private fun part2(): Long {
    var total = 0L

    // 12 highest numbers in order skipping lowest.
    for (line in lineList) {
        total += orderLine(line).toLong()
    }
    return total
}

private fun orderLine(line: String) : String {
    var twelve = ""
    var lastPos = 0

    for (i in 1..12) {
        // Get the next highest and record its position.
        var currentHigh = line[lastPos]

        // Searching the substring of start/highest found but leaving enough remaining chars to get all 12
        // highest char 0 index to length-12
        // highest char 1 index to length-11
        // highest char 2 index to length-10 ...
        for (x in lastPos..<line.length -12 +i) {
            if (line[x] > currentHigh) {
                currentHigh = line[x]
                lastPos = x
            }
        }
        // Increment for next iteration
        twelve += currentHigh
        lastPos++
    }
    return twelve
}

fun main(args: Array<String>) {
    readInput(args[0])
//    println(part1()) //17359
    println(part2()) //172787336861064
}