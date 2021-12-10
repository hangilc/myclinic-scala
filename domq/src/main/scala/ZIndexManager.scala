package dev.fujiwara.domq

import scala.collection.mutable

object ZIndexManager:
  var stack: mutable.Stack[Int] = mutable.Stack[Int](2000)
  
  def zIndexNext(inc: Int): Int =
    val zIndex = stack.top + inc
    stack.push(zIndex)
    zIndex

  def zIndexRelease(): Unit =
    stack.pop()


