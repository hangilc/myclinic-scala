package dev.myclinic.scala.config

case class MasterNameMap(
  map: Map[String, List[(String, Int)]]
):
  val shinryou: Map[String, Int] = Map(map.applyOrElse("s", _ => List.empty): _*)
  val kizai: Map[String, Int] = Map(map.applyOrElse("k", _ => List.empty): _*)
  val byoumei: Map[String, Int] = Map(map.applyOrElse("d", _ => List.empty): _*)
  val shuushokugo: Map[String, Int] = Map(map.applyOrElse("a", _ => List.empty): _*)
  val yakuzai: Map[String, Int] = Map(map.applyOrElse("y", _ => List.empty): _*)

object MasterNameMap:
  val linePattern = raw"([skday]),(.+),(\d+)\s*".r