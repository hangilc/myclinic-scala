package dev.myclinic.scala.util

object FunUtil:
  extension [T](t: T)
    def |>[U](f: T => U): U = f(t)
