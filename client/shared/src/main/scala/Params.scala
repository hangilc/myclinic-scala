package dev.myclinic.scala.client

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import scala.collection.mutable.ListBuffer

trait ParamValue {
  def encode(): String
}

object ParamValue {
  implicit def strParam(s: String): ParamValue = () => s

  implicit def intParam(i: Int): ParamValue = () => i.toString

  implicit def dateParam(d: LocalDate): ParamValue = () => d.toString

  private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  implicit def timeParam(t: LocalTime): ParamValue = {
    t.format(timeFormatter)
  }
}

trait URIComponentEncoder {
  def encode(src: String): String
}

class Params()(implicit compEncoder: URIComponentEncoder){
  private val items = ListBuffer.empty[(String, String)]

  def add(k: String, p: ParamValue): Unit = {
    val pair = (k, compEncoder.encode(p.encode()))
    items += pair
  }

  def isEmpty: Boolean = items.isEmpty

  def encode(): String = {
    items.map(_ match {
      case (k, "") => k
      case (k, v) => s"$k=$v"
    }).mkString("&")
  }
}

object Params {
  def apply(items: (String, ParamValue)*)(implicit compEncoder: URIComponentEncoder) : Params = {
    val params = new Params()
    for((k, p) <- items){
      params.add(k, p)
    }
    params
  }
}
