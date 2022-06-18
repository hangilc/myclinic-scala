package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

class PageLayout1(hotlineSendAs: String, hotlineSendTo: String)(using EventFetcher):
  val banner = div
  val workarea = div
  val sideMenu = SideMenu(workarea)
  val hotline = new HotlineBlock(hotlineSendAs, hotlineSendTo)
  val ele = div(id := "page-layout1-content")(
    banner(id := "page-layout1-banner"),
    workarea(id := "page-layout1-workarea")(
      div(id := "page-layout1-side-bar")(
        sideMenu.ele(id := "page-layout1-side-menu"),
        hotline.ele
      )
    )
  )
