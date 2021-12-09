package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.{EventSubscriber, Selection}
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.*
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.util.{HokenRep, RcptUtil}
import dev.myclinic.scala.apputil.FutanWari

class NewShahokokuhoSubblock(patientId: Int):
  val form = ShahokokuhoForm()
  val errBox = ErrorBox()
  val block: Subblock = Subblock(
    "新規社保国保",
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
              Api.enterShahokokuho(h).onComplete {
                  case Success(_) => close()
                  case Failure(ex) => errBox.show(ex.getMessage)
              }
          }
          case Left(msg) => errBox.show(msg)
      }
