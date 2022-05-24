package dev.myclinic.scala.util

object FunUtil:
  extension [T](t: T)
    def |>[U](f: T => U): U = f(t)

  extension [T](list: List[T])
    def applyOption(i: Int): Option[T] =
      if i >= 0 && i < list.size then Some(list(i))
      else None
