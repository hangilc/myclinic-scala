package dev.fujiwara.domq

object ZIndexManager:
  private var stack: List[Int] = List(2000)
  
  def alloc(): Int =
    val z = stack.head + 1
    stack = z :: stack
    println(("alloc", z))
    z
  
  def release(z: Int): Unit =
    println(("releasing", z, stack))
    stack = remove(stack, z)
    println(("released", stack))

  private def remove(list: List[Int], n: Int): List[Int] =
    val head = list.head
    if head == n then list.tail
    else if head < n then head :: remove(list.tail, n)
    else list



