package aoc2025
 import java.io.File
 import java.io.InputStream

private val lineList = mutableListOf<CharArray>()

private fun readInput(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    inputStream.bufferedReader().forEachLine { lineList.add(it.toCharArray()) }
}

private fun part1(): Int {
    return listOfUnblocked().size
}

private fun part2(): Long {
    var total = 0L
    var prevTotal: Long

    // Keep counting and removing the unblocked coords until we get no further.
    do {
        val l = listOfUnblocked()
        prevTotal = total
        total += l.size

        // Remove items.
        for ((x,y) in l) {
            lineList[y][x] = 'o'
        }

    } while (total != prevTotal)

    return total
}

private fun listOfUnblocked(): MutableList<Pair<Int, Int>> {
    val l = mutableListOf<Pair<Int,Int>>()
    for (y in 0..<lineList.count()) {
        for (x in 0..<lineList[y].size) {
            if (lineList[y][x] == '@' && blockers(x, y) < 4) {
                l.add(Pair(x,y))
            }
        }
    }
    return l
}

private fun blockers(x: Int, y: Int): Int {
    // Check all 8 sides for blockers.
    // N, NE, E, SE, S, SW, W, NW
    return isBlocked(x,y-1) +
            isBlocked(x+1,y-1) +
            isBlocked(x+1,y) +
            isBlocked(x+1,y+1) +
            isBlocked(x,y+1) +
            isBlocked(x-1,y+1) +
            isBlocked(x-1, y) +
            isBlocked(x-1,y-1)
}

private fun isBlocked(x: Int, y: Int) : Int {
    // Ignore edges.
    if (x <0 || x> lineList[0].size-1 || y <0 || y> lineList.count()-1 ) {
        return 0
    }

    if (lineList[y][x] == '@') {
        return 1
    }

    return 0
}

fun main(args: Array<String>) {
    readInput(args[0])
//    println(part1()) //1349
    println(part2()) //8277
}