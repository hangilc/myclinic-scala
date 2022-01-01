package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, Selection}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.{EventSubscriber}
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.*
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.util.{HokenRep, RcptUtil}
import dev.myclinic.scala.apputil.FutanWari

class NewKoukikoureiSubblock(patientId: Int):
  val form = KoukikoureiForm()
  val errBox = ErrorBox()
  val block: Subblock = Subblock(
    "新規後期高齢",
    div(
        errBox.ele,
        form.ele,
    ),
    div(
        button("入力", onclick := (onEnter _)),
        button("閉じる", onclick := (close _))
    )
  )

  def close(): Unit = block.ele.remove()

  private def onEnter(): Unit =
      form.validateForEnter(patientId).asEither match {
          case Right(h) => {
              Api.enterKoukikourei(h).onComplete {
                  case Success(_) => close()
                  case Failure(ex) => errBox.show(ex.getMessage)
              }
          }
          case Left(msg) => errBox.show(msg)
      }
