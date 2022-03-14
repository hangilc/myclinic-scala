package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Modifier
import dev.fujiwara.domq.{Modal, ShowMessage}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import dev.fujiwara.scala.drawer.{Op, PrintRequest}
import io.circe.*
import io.circe.syntax.*
import dev.myclinic.scala.webclient.Api
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Success
import scala.util.Failure
import org.scalajs.dom.HTMLSelectElement

class PrintDialog(
    title: String,
    ops: List[Op],
    width: Double,
    height: Double,
    viewBox: String,
    prefKind: String = "手動"
):
  val svg =
    DrawerSvg.drawerJsonToSvg(ops.asJson.toString, width, height, viewBox)
  val eDisplay: HTMLElement = div()
  val eSetting: HTMLElement = div()
  val eSelect: HTMLSelectElement = select()
  val eDefaultCheck: HTMLInputElement = checkbox()
  val dlog: Modal = Modal(
    title,
    div(
      eDisplay(svg),
      eSetting(
        "設定",
        eSelect(ml := "0.3rem"),
        eDefaultCheck(attr("checked") := "checked"),
        span("既定に"),
        a(
          "管理画面表示",
          href := "http://127.0.0.1:48080",
          attr("target") := "blank",
          ml := "0.3rem"
        )
      )
    ),
    div(
      button("印刷", onclick := (doPrint _)),
      button("キャンセル", onclick := (() => dlog.close()))
    )
  )
  dlog.dialog(cls := "pring-dialog")

  def initSetting(): Future[Unit] =
    for
      settings <- Api.listPrintSetting()
      pref <- Api.getPrintPref(prefKind)
    yield {
      val settingOptions: List[Modifier[HTMLElement]] =
        ("手動" :: settings).map(name => option(name, value := name))
      eSelect(settingOptions: _*)
      eSelect.setValue(pref.getOrElse("手動"))
    }

  def open(): Unit = 
    initSetting().onComplete {
      case Success(_) => dlog.open()
      case Failure(ex) => 
        System.err.println(ex.getMessage)
        dlog.open()
    }

  def doPrint(): Unit =
    val req = PrintRequest(List.empty, List(ops))
    val setting: Option[String] = 
      val s = eSelect.getValue
      if s == null || s == "" || s == "手動" then None
      else Some(s)
    val f = 
      for
        _ <- Api.printDrawer(req, setting)
        _ <- handlePrefUpdate(setting)
      yield ()
    f.onComplete {
      case Success(_) => dlog.close()
      case Failure(ex) => ShowMessage.showError(ex.getMessage)
    }

  def handlePrefUpdate(setting: Option[String]): Future[Boolean] = 
    val asDefaultChecked = eDefaultCheck.checked
    if asDefaultChecked then
      val currentDefaultOpt = eSelect.qSelector("option[selected]").map(_.getAttribute("value"))
      if currentDefaultOpt != setting then
        Api.setPrintPref(prefKind, setting.getOrElse("手動")).map(_ => true)
      else
        Future.successful(false)
    else
      Future.successful(false)

