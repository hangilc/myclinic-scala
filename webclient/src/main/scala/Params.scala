package dev.myclinic.scala.webclient

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import scala.collection.mutable.ListBuffer
import dev.myclinic.scala.util.DateUtil
import java.time.LocalDateTime
import scala.scalajs.js.URIUtils

trait ParamValue:
  def encode(): String

object ParamsImplicits:
  given strParam: Conversion[String, ParamValue] = s =>
    new ParamValue {
      def encode(): String = s
    }

  given intParam: Conversion[Int, ParamValue] = (i: Int) =>
    new ParamValue {
      def encode(): String = i.toString()
    }

  given dateParam: Conversion[LocalDate, ParamValue] = (d: LocalDate) =>
    new ParamValue {
      def encode(): String = DateUtil.dateToString(d)
    }

  given timeParam: Conversion[LocalTime, ParamValue] = (t: LocalTime) =>
    new ParamValue {
      def encode(): String = DateUtil.timeToString(t)
    }

  given dateTimeParam: Conversion[LocalDateTime, ParamValue] =
    (dt: LocalDateTime) =>
      new ParamValue {
        def encode(): String = DateUtil.dateTimeToString(dt)
      }

trait URIComponentEncoder:
  def encode(src: String): String

class Params():
  private val items = ListBuffer.empty[(String, String)]

  def add(k: String, p: ParamValue): Unit =
    val pair = (k, URIEncoder.encode(p.encode()))
    items += pair

  def isEmpty: Boolean = items.isEmpty

  def encode(): String =
    items
      .map(_ match {
        case (k, "") => k
        case (k, v)  => s"$k=$v"
      })
      .mkString("&")

object Params:
  def apply(
      items: (String, ParamValue)*
  ): Params =
    val params = new Params()
    for (k, p) <- items do
      params.add(k, p)
    params

object URIEncoder:
  def encode(src: String): String =
    URIUtils.encodeURIComponent(src)
