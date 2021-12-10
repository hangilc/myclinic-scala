package dev.fujiwara.domq

object ZIndexManager:
  var stack: List[Int] = List(2000)
  
  def alloc(): Int =
    val z = stack.head + 1
    stack = z :: stack
    z
  
  def release(z: Int): Unit =
    stack = remove(stack, z)

  private def remove(list: List[Int], n: Int): List[Int] =
    val head = list.head
    if head == n then list.tail
    else if head < n then head :: remove(list.tail, n)
    else list



