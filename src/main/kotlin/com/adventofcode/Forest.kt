package com.adventofcode

import com.adventofcode.Forest.addTrees
import com.adventofcode.Forest.crosswalks


class Crosswalk(
  private val tree: Int,
  private val left: List<Int>,
  private val right: List<Int>,
  private val top: List<Int>,
  private val bottom: List<Int>
) {

  fun scenicScore(): Int {
    val leftScore = countVisibleTreesOn(left)
    val rightScore = countVisibleTreesOn(right)
    val topScore = countVisibleTreesOn(top)
    val bottomScore = countVisibleTreesOn(bottom)
    return leftScore * rightScore * topScore * bottomScore
  }

  private fun countVisibleTreesOn(side: List<Int>): Int {
    return when (val visibleTrees = side.takeWhile { it < tree }.size) {
      side.size -> visibleTrees
      else -> visibleTrees + 1
    }
  }
}

object Forest {

  private val forest = mutableListOf<List<Int>>()

  private val columns get() = forest.first().size

  private val rows get() = forest.size

  private fun tree(row: Int, column: Int): Int {
    return forest[row][column]
  }

  private fun column(n: Int): List<Int> {
    return (0 until rows).map { tree(it, n) }
  }

  private fun row(n: Int): List<Int> {
    return forest[n]
  }

  private fun crosswalk(row: Int, column: Int): Crosswalk {
    val me = tree(row, column)
    val leftAndRight = row(row)
    val left = leftAndRight.slice(0 until column).asReversed()
    val right = leftAndRight.slice(column + 1 until columns)
    val topAndBottom = column(column)
    val top = topAndBottom.slice(0 until row).asReversed()
    val bottom = topAndBottom.slice(row + 1 until rows)
    return Crosswalk(me, left, right, top, bottom)
  }

  fun addTrees(encodedRow: String) {
    val row = encodedRow.map { it.toString().toInt() }
    forest.add(row)
  }

  fun crosswalks() = sequence {
    for (r in 0 until rows) {
      for (c in 0 until columns) {
        yield(crosswalk(r, c))
      }
    }
  }
}

fun solution(): Int {
  return crosswalks().maxOf(Crosswalk::scenicScore)
}

fun main() {
  ::main.javaClass
    .getResourceAsStream("/input")!!
    .bufferedReader()
    .forEachLine(::addTrees)
  println(solution())
}
