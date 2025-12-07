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

    var index = 0
    val maths = ArrayList<List<Long>>()

    // Read all the numbers into a 2D array.
    // Loop until a non-numeric line read.
    while (!lineList[index].contains("+")) {
        maths.add(lineList[index].trim().split(Regex(" +")).map { s -> s.toLong() })
        index++
    }

    // Get the operation and apply to the column.
    val operations = lineList[index].trim().split(Regex(" +"))
    for (column in 0..<operations.size) {
        var subTotal = 0L
        for (row in maths) {
            if (subTotal == 0L) {
                subTotal = row[column]
            }
            else {
                when (operations[column]) {
                    "+" -> subTotal += row[column]
                    "*" -> subTotal *= row[column]
                }
            }
        }
        total += subTotal
    }
    return total
}

fun main(args: Array<String>) {
    readInput(args[0])
    println(part1()) //4878670269096
//    println(part2()) //
}