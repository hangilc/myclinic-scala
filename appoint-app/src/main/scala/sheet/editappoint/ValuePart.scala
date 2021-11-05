package dev.myclinic.scala.web.appoint.sheet.editappoint

import org.scalajs.dom.raw.{HTMLElement}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ErrorBox}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

abstract class ValuePart:
  val main: HTMLElement
  def updateUI(): Unit

  val workarea: HTMLElement = div()
  val errBox: ErrorBox = ErrorBox()
  def ele: HTMLElement = 
    div(
      main,
      errBox.ele,
      workarea
    )

  def clearWorkarea(): Unit = workarea.clear()

  def addToWorkarea(e: HTMLElement): Unit = workarea(e)

  def showError(msg: String): Unit =
    errBox.show(msg)

  extension [T] (f: Future[T])
    def catchErr: Future[T] =
      f.transform(t => t match {
        case Success(_) => t
        case Failure(ex) => {
          errBox.show(ex.toString)
          t
        }
      })

class ValuePartManager(var part: ValuePart):
  val ele = div(part.ele)

  def changeValuePartTo(nextPart: ValuePart): Unit =
    ele.clear()
    ele(part.ele)
    part = nextPart

  def updateUI(): Unit =
    part.updateUI()