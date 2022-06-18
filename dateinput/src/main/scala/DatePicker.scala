package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.Screened
import dev.fujiwara.domq.FloatingElement

case class DatePicker():
  val ele = div(
    width := "200px",
    height := "160px",
    background := "#999",
    "PICKER"
  )

  def open(): Unit =
    Screened(ele, locator _, () => ()).open()

  def locator(e: FloatingElement): Unit =
    import dev.fujiwara.domq.{Geometry, Position}
    import dev.fujiwara.domq.Geometry.*
    import dev.fujiwara.domq.Position
    val w = Geometry.getRect(e.ele)
    println(Position.windowWidthWithoutScrollbar)
    println(w)
    ()