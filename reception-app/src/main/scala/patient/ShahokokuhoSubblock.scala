package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, ShowMessage, CustomEvent}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.{EventSubscriber, Selection}
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.web.appbase.DateInput
import dev.myclinic.scala.model.*
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.util.{HokenRep, RcptUtil}
import dev.myclinic.scala.apputil.FutanWari

class ShahokokuhoSubblock(shahokokuho: Shahokokuho):
  val eContent = div()
  val eCommands = div()
  val block: Subblock = Subblock(
    "社保国保",
    eContent,
    eCommands
  )
  block.ele(
    cls := s"shahokokuho-${shahokokuho.shahokokuhoId}",
    oncustomevent[ShahokokuhoUpdated]("shahokokuho-updated") := (onUpdated _),
  )
  disp()

  def disp(): Unit =
    eContent.clear()
    eContent(ShahokokuhoDisp(shahokokuho).ele)
    eCommands.clear()
    eCommands(
      button("削除", onclick := (onDelete _)),
      button("編集", onclick := (onEdit _)),
      button("閉じる", onclick := (() => block.ele.remove()))
    )

  def edit(): Unit =
    val form = ShahokokuhoForm()
    val errBox = ErrorBox()
    form.setData(shahokokuho)
    eContent.clear()
    eContent(errBox.ele, form.ele)
    eCommands.clear()
    eCommands(
      button("入力", onclick := (() => onEnter(form, errBox))),
      button("キャンセル", onclick := (() => disp()))
    )

  def onEdit(): Unit =
    edit()

  private def onEnter(form: ShahokokuhoForm, errBox: ErrorBox): Unit =
    form
      .validateForUpdate(shahokokuho.shahokokuhoId, shahokokuho.patientId)
      .asEither match {
      case Right(h) => {
        Api.updateShahokokuho(h).onComplete {
          case Success(_)  => ()
          case Failure(ex) => errBox.show(ex.getMessage)
        }
      }
      case Left(msg) => errBox.show(msg)
    }

  private def onDelete(): Unit =
    ShowMessage.confirm(
      "この保険を削除していいですか？",
      ok => if ok then doDelete(shahokokuho.shahokokuhoId)
    )

  private def doDelete(shahokokuhoId: Int): Unit =
    Api.deleteShahokokuho(shahokokuhoId).onComplete {
      case Success(_) => block.close()
      case Failure(ex) => ShowMessage.showError(ex.getMessage)
    }

  private def onUpdated(event: CustomEvent[ShahokokuhoUpdated]): Unit =
    val updated = event.detail.updated
    val newSub = ShahokokuhoSubblock(updated)
    block.ele.replaceBy(newSub.block.ele)

