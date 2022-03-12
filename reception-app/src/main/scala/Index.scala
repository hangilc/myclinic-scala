package dev.myclinic.scala.web.reception

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document
import dev.fujiwara.domq.ElementQ.*
import dev.fujiwara.domq.Html.*
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.model.{Hotline, AppModelEvent}
import dev.myclinic.scala.web.appbase.{EventFetcher, EventPublishers}
import scala.concurrent.Future
import dev.myclinic.scala.model.AppEvent
import dev.myclinic.scala.web.appbase.HotlineUI
import dev.myclinic.scala.web.appbase.HotlineHandler

@JSExportTopLevel("JsMain")
object JsMain:
  import ReceptionEvent.given
  val ui = new MainUI

  @JSExport
  def main(isAdmin: Boolean): Unit =
    (for 
      _ <- ui.hotline.init()
      _ <- fetcher.start()
    yield
      document.body(ui.ele)
      ui.invoke("メイン")).onComplete {
        case Success(_) => ()
        case Failure(ex) => System.err.println(ex.getMessage)
      }

