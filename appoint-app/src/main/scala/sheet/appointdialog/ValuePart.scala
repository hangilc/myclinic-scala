package dev.myclinic.scala.web.appoint.sheet.appointdialog

import org.scalajs.dom.{HTMLElement}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ErrorBox}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._


abstract class ValuePart:
  val main: HTMLElement
  def updateUI(): Unit

  private val workarea: HTMLElement = div()
  private val errBox: ErrorBox = ErrorBox()
  def ele: HTMLElement = 
    div(css(style => {
      style.maxHeight = "300px"
      style.overflowY = "auto"
    }))(
      main,
      errBox.ele,
      workarea
    )

  def initWorkarea(): Unit = 
    workarea.clear()
    errBox.hide()

  def addToWorkarea(e: HTMLElement): Unit = workarea(e)

  def workareaIsEmpty: Boolean = workarea.isEmpty

  def showError(msg: String): Unit =
    errBox.show(msg)

  def hideError(): Unit =
    errBox.hide()

  extension [T] (f: Future[T])
    def catchErr: Unit =
      f.onComplete {
        case Success(_) => ()
        case Failure(ex) => errBox.show(ex.getMessage)
      }

class ValuePartManager(var part: ValuePart):
  val ele = div(part.ele)

  def changeValuePartTo(nextPart: ValuePart): Unit =
    ele.clear()
    ele(nextPart.ele)
    part = nextPart

  def updateUI(): Unit =
    part.updateUI()