package dev.myclinic.scala.model

object Events {

  case class FromTo[T](from: T, to: T)

}
