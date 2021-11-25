package dev.myclinic.scala.web.appbase

import scala.scalajs.js
import scala.scalajs.js.annotation.*
import org.scalajs.dom.raw.HTMLElement

import dev.fujiwara.scala.drawer.Op

@js.native
@JSGlobalScope
object DrawerSvg extends js.Object:
  def drawerJsonToSvg(
      opsJson: String,
      width: Double,
      height: Double,
      viewBox: String
  ): HTMLElement = js.native

  def drawerToSvg(
      ops: List[Op],
      width: Double,
      height: Double,
      viewBox: String
  ): HTMLElement = js.native

