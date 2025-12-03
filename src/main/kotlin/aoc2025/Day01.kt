package net.tintreach.aoc2025
 import java.io.File
 import java.io.InputStream

private val lineList = mutableListOf<String>()

private fun readInput(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
}

private fun part1() {

    // Start in middle position 50.
    var position = 50L
    var atZero = 0

    for (item in lineList) {
        val dir = item[0]
        val turns = item.slice(1..<item.length).toLong()
        if (dir == 'R') {
            position += turns%100
        } else {
            position -= turns%100
        }

        // Reset to 0-99 pos.
        position %= 100

        if (position == 0L) {
            atZero++
        }
    }
    println("At zero $atZero")
    //1165 - correct.
}

private fun part2() {

    var position = 50L
    var atZero = 0L

    for (item in lineList) {
        val dir = item[0]
        var turns = item.slice(1..<item.length).toLong()

        // Count full rotations.
        atZero += turns/100L
        // Turns is now what's left.
        turns %= 100

        val lastPos = position
        if (dir == 'R') {
            position += turns
        } else {
            position -= turns
        }

        // Zero or full circle.
        if (position%100 == 0L) {
            atZero++
        }
        // Crossed 0 since last time?
        else if (lastPos < 0) {
            if (position >= 0 || position <= -100)
                atZero++
        }
        else if (lastPos > 0) {
            if (position <0 || position >100)
                atZero++
        }

        position %= 100
    }
    println("At zero $atZero")
    //6496 - correct
}


fun main(args: Array<String>) {
    readInput(args[0])
    //    part1()
    part2()
}