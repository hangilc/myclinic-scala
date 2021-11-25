package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Modifier
import dev.fujiwara.domq.{Modal}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}
import dev.fujiwara.scala.drawer.Op

class PrintDialog(
    title: String,
    ops: List[Op],
    width: Double,
    height: Double,
    viewBox: String,
    settingNames: List[String] = List.empty,
    prefSetting: String = "手動",
    zIndex: Int
):
  val svg = DrawerSvg.drawerToSvg(ops, width, height, viewBox)
  val eDisplay: HTMLElement = div()
  val eSetting: HTMLElement = div()
  val eSelect: HTMLElement = select()
  val settingOptions: List[Modifier] =
    ("手動" :: settingNames).map(name => option(name, value := name))
  val dlog: Modal = Modal(
    title,
    div(
      eDisplay(svg),
      eSetting(
        "設定",
        eSelect(settingOptions: _*)(ml := "0.3rem"),
        a(
          "管理画面表示",
          href := "http://127.0.0.1:48080",
          attr("target") := "blank",
          ml := "0.3rem"
        )
      )
    ),
    div(
      button("印刷"),
      button("キャンセル", onclick := (() => dlog.close()))
    )
  )
  dlog.dialog(cls := "pring-dialog")
  eSelect.selectOptionByValue(prefSetting)

  def open(): Unit = dlog.open()
