package aoc2025

import java.io.File
import java.io.InputStream

private val lineList = mutableListOf<String>()

private fun readInput(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
}

private var farthestPairs = mutableListOf<Pair<String, Long>>()

private fun part1(): Long {

    // Build the list of pairs with distance.
    for (i in 0..<lineList.size) {
        for (j in i + 1..<lineList.size) {
            val area = calculateArea(lineList[i], lineList[j])
            farthestPairs.add(Pair(lineList[i] + "|" + lineList[j], area))
        }
    }

    // Sort on distance.
    farthestPairs = farthestPairs.sortedBy { (_, v) -> v }.reversed().toMutableList()

    return farthestPairs[0].second
}

private fun calculateArea(a: String, b: String): Long {
    val (aX, aY) = a.split(',').map { t -> t.toInt() }
    val (bX, bY) = b.split(',').map { t -> t.toInt() }

    // Subtract the largest from smallest.
    val x = if (aX > bX) {
        aX - bX
    } else {
        bX - aX
    }
    val y = if (aY > bY) {
        aY - bY
    } else {
        bY - aY
    }
    // Add 1 as places are inclusive.
    return (x + 1) * (y + 1).toLong()
}

private var ceiling = 0L
private var floor = Long.MAX_VALUE
private var leftEdge = 0L
private var rightEdge = Long.MAX_VALUE
private var perimeter = mutableSetOf<Pair<Long, Long>>()
private var outOfBounds = mutableSetOf<Pair<Long, Long>>()

private fun part2(): Long {
    // Probably a simpler solution...
    // Track the perimeter demarked by joining tiles.
    // Also track the outside edge - tiles track clockwise so anything a quarter turn anti-clockwise is out of bounds.
    // Ensure each corner-to-corner box has no out-of-bounds within it.  
    
    // First reduce the puzzle space.
    val xs = lineList.map { s -> s.split(',').first().toLong() }.sorted()
    val ys = lineList.map { s -> s.split(',').component2().toLong() }.sorted()

    leftEdge = xs.first()
    rightEdge = xs.last()
    floor = ys.last()
    ceiling = ys.first()

    trackPerimeter()
//    drawPerimeter() // testing only!

    var count = 0
    while (count < farthestPairs.size) {
        if (withinBoundary(farthestPairs[count].first)) {
            break
        }
        count++
    }

    println("Box at ${farthestPairs[count].first} is largest.")
    return farthestPairs[count].second
}

private fun drawPerimeter() {
    for (y in 0..floor + 1) {
        for (x in 0..rightEdge + 1) {
            if (lineList.contains("$x,$y"))
                print('#')
            else if (perimeter.contains(Pair(x, y))) {
                print('X')
            } else if (outOfBounds.contains(Pair(x, y))) {
                print('-')
            } else {
                print('.')
            }
        }
        println()
    }
}

private fun trackPerimeter() {

    val (startX, startY) = lineList[0].split(',').map { it.toLong() }
    val start = Pair(startX, startY)
    perimeter.add(start)

    var current = start.copy()
    (1..<lineList.size).forEach { i ->
        val (nextX, nextY) = lineList[i].split(',').map { it.toLong() }
        val next = Pair(nextX, nextY)
        linkCurrentToNext(current, next)
        current = next
        perimeter.add(current)
    }

    // Link back to start to close the loop.
    linkCurrentToNext(current, start)

    // Clean up any inbounds from OOB e.g. where a list doublebacks next to itself.
    outOfBounds.filter { perimeter.contains(it) }.forEach { outOfBounds.remove(it) }

}

private fun linkCurrentToNext(current: Pair<Long, Long>, next: Pair<Long, Long>) {
    // Determine direction then add the points to the perimeter accordingly.
    // Left/right
    var from: Long
    var to: Long
    if (current.first != next.first) {
        from = current.first
        to = next.first
        // right
        if (to > from) {
            for (p in from..to) {
                perimeter.add(Pair(p, current.second))
                outOfBounds.add(Pair(p, current.second - 1))
            }
        }
        // left
        else {
            for (p in from downTo to) {
                perimeter.add(Pair(p, current.second))
                outOfBounds.add(Pair(p, current.second + 1))
            }
        }
    }
    // Up/down
    else {
        from = current.second
        to = next.second
        // down
        if (to > from) {
            for (p in from..to) {
                perimeter.add(Pair(current.first, p))
                outOfBounds.add(Pair(current.first + 1, p))
            }
        }
        // up
        else {
            for (p in from downTo to) {
                perimeter.add(Pair(current.first, p))
                outOfBounds.add(Pair(current.first - 1, p))
            }
        }
    }
}

private fun withinBoundary(diagonal: String): Boolean {
    // Get all corners.
    val (aX, aY) = diagonal.split('|').first().split(',').map { it.toLong() }
    val (bX, bY) = diagonal.split('|').component2().split(',').map { it.toLong() }

    val left = if (aX < bX) aX else bX
    val right = if (aX > bX) aX else bX
    val top = if (aY < bY) aY else bY
    val bottom = if (aY > bY) aY else bY

    // Check this box is within the overall edges.
    if (left < leftEdge || right > rightEdge || top < ceiling || bottom > floor) {
        return false
    }

    return outOfBounds.none { (x, y) -> x in left..right && y in top..bottom }
}

fun main(args: Array<String>) {
    readInput(args[0])
    println(part1()) // 4749838800
    println(part2()) // 1624057680
}