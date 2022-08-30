package dev.myclinic.scala.web.practiceapp.devservice

import dev.myclinic.scala.web.appbase.SideMenuService
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.document
import scala.scalajs.js
import scala.language.implicitConversions

import dev.fujiwara.domq.all.{*, given}

case class DevGeometryService() extends SideMenuService:
  val d = div
  val b = button("Button")
  override def getElement: HTMLElement =
    d(
      button("calc", onclick := (doCalc _))

    )

  def doCalc(): Unit =
    val ww = document.documentElement.clientWidth
    println(("window clientWidth", document.documentElement.clientWidth))
    println(("window client height", document.documentElement.clientHeight))
    b(css(style =>
      style.position = "absolute"
      style.left = "0px"
      style.top = "0px"
      println(style.right)
    ))
    document.body(b)
    println(("b", b.getBoundingClientRect().left))
    println(("b", b.getBoundingClientRect().width))
    b.style.left = s"${ww - b.getBoundingClientRect().width}px"


