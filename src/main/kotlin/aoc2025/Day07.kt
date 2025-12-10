package aoc2025
 import java.io.File
 import java.io.InputStream

private val lineList = mutableListOf<CharArray>()

private fun readInput(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    inputStream.bufferedReader().forEachLine { lineList.add(it.toCharArray()) }
}

private fun part1(): Int {
    // Work down row at a time. Starting with S and for each ^, record the X index of the splits.

    var beams = mutableSetOf<Int>()
    var total = 0
    beams.add(lineList[0].indexOf('S'))
    
    for (y in 1..<lineList.size) {
        for (x in 0..<lineList[y].size) {
            val before = String(lineList[y])
            if(lineList[y][x] == '^' && x in beams) {
                beams = newBeams(x, y, beams)
            }
            if (before != String(lineList[y])) {
                total++
            }
        }
    }
    
    return total
}

private fun newBeams(x: Int, y: Int, set: Set<Int>) : MutableSet<Int> {
    val s = mutableSetOf<Int>()
    s.addAll(set)
    if (inbounds(x-1) && lineList[y][x-1] != '^' ) {
        lineList[y][x-1] = '|'
        s.add(x-1)
        s.remove(x)
    }
    if (inbounds(x+1) && lineList[y][x+1] != '^' ) {
        lineList[y][x+1] = '|'
        s.add(x+1)
        s.remove(x)
    }
    return s
}

private fun inbounds(x: Int) : Boolean {
    return (x >= 0) && (x < lineList[0].size)
}

fun main(args: Array<String>) {
    readInput(args[0])
    println(part1()) //1504
//    println(part2()) //
}