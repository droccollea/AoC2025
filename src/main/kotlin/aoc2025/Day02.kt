package aoc2025

import java.io.File
import java.io.InputStream
import kotlin.math.pow

private var line = ""

private fun readInput(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    line = inputStream.bufferedReader().readLine()
}

private fun part1() {

    var total = 0L

    // Break line into ranges, separating on the comma.
    val ranges = line.split(",")

    for (range in ranges) {
        val start = range.split("-")[0]
        val end = range.split("-")[1]
        println("Starting at $start and ending at $end")

        total += tallyTwoInvalidsInRange(start, end)
    }

    println("Total $total")
}

private fun part2() {

    var total = 0L

    // Break line into ranges, separating on the comma.
    val ranges = line.split(",")

    for (range in ranges) {
        val start = range.split("-")[0]
        val end = range.split("-")[1]
        println("Starting at $start and ending at $end")

        total += tallyAllInvalidsInRange(start, end)
    }

    println("Total $total")
}

private fun tallyTwoInvalidsInRange(start: String, end: String): Long {
    var current = start.toLong()
    val last = end.toLong()

    var invalidTotal = 0L

    while (current <= last) {
        // Comparisons need to be on even lengths between start and ending with half the end number.
        // if value's length is odd round up e.g. 100 -> 1000
        val len = current.toString().length
        if (len % 2 != 0) {
            println("$current is odd, buming up")
            current = (10.0.pow(len)).toLong()
            continue
        }

        // Split current in half and check if there is a match.
        val partA = current.toString().substring(0, (len / 2))
        val partB = current.toString().substring((len / 2))

        if (partA == partB) {
            invalidTotal += current
            println("Match on $current : $partA")
        }

        current++
    }

    return invalidTotal
}

private fun tallyAllInvalidsInRange(start: String, end: String): Long {
    var current = start.toLong()
    val last = end.toLong()

    var invalidTotal = 0L

    while (current <= last) {
        val len = current.toString().length

        // Comparisons need to be of divisible lengths between start and ending with half the end number.
        for (i in 1..len / 2) {

            // if this value's length is not divisible into the full length, move on.
            if (len % i != 0) {
                continue
            }

            // We have 1 so take it off the match count.
            val matchesNeeded = (len / i) - 1

            // Build the pattern to match.
            val part = current.toString().substring(0, i)
            val r = Regex(part)

            // Search the rest of the current number for complete matches.
            if (r.findAll(current.toString().substring(i)).count() == matchesNeeded) {
                invalidTotal += current
                println("Match on $current : $part")
                // Done with this number, move on.
                break
            }
        }

        current++
    }

    return invalidTotal
}

fun main(args: Array<String>) {
    readInput(args[0])
//    part1() //12586854255
    part2() //17298174201
}