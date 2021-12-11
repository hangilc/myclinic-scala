package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.EventSubscriber
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.Patient

class Records() extends SideMenuService:
  def getElement: HTMLElement =
    div(display := "flex", alignItems := "center")(
      h1("診療記録")(margin := "0", padding := "0"),
      // pullDown("患者選択", List(
      //   "受付患者" -> (() => ()),
      // ))(ml := "1rem")
    )
