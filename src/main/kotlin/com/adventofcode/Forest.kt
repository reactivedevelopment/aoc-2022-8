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
  private fun isLeftVisible() = isVisibleOn(left)
  private fun isRightVisible() = isVisibleOn(right)

  private fun isTopVisible() = isVisibleOn(top)

  private fun isBottomVisible() = isVisibleOn(bottom)

  fun isFullVisible() =
    isLeftVisible()
      && isRightVisible()
      && isTopVisible()
      && isBottomVisible()

  fun isPartialVisible() =
    isLeftVisible()
      || isRightVisible()
      || isTopVisible()
      || isBottomVisible()

  fun isVisibleOn(side: List<Int>) = side.all { it < tree }
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
    val left = leftAndRight.slice(0 until column)
    val right = leftAndRight.slice(column + 1 until columns)
    val topAndBottom = column(column)
    val top = topAndBottom.slice(0 until row)
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
  return crosswalks().count(Crosswalk::isPartialVisible)
}

fun main() {
  ::main.javaClass
    .getResourceAsStream("/input")!!
    .bufferedReader()
    .forEachLine(::addTrees)
  println(solution())
}
