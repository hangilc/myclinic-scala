package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, ShowMessage, CustomEvent}
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

class KouhiSubblock(kouhi: Kouhi):
  val eContent = div()
  val eCommands = div()
  val block: Subblock = Subblock(
    "公費",
    eContent,
    eCommands
  )
  block.ele(
    cls := s"kouhi-${kouhi.kouhiId}",
    oncustomevent[KouhiUpdated](
      "kouhi-updated"
    ) := (onUpdated _)
  )
  disp()

  def disp(): Unit =
    eContent.clear()
    eContent(KouhiDisp(kouhi).ele)
    eCommands.clear()
    eCommands(
      button("削除", onclick := (onDelete _)),
      button("編集", onclick := (onEdit _)),
      button("閉じる", onclick := (() => block.ele.remove()))
    )

  def edit(): Unit =
    val form = KouhiForm()
    val errBox = ErrorBox()
    form.setData(kouhi)
    eContent.clear()
    eContent(errBox.ele, form.ele)
    eCommands.clear()
    eCommands(
      button("入力", onclick := (() => onEnter(form, errBox))),
      button("キャンセル", onclick := (() => disp()))
    )

  def onEdit(): Unit =
    edit()

  private def onEnter(form: KouhiForm, errBox: ErrorBox): Unit =
    form
      .validateForUpdate(kouhi.kouhiId, kouhi.patientId)
      .asEither match {
      case Right(h) => {
        Api.updateKouhi(h).onComplete {
          case Success(_)  => disp()
          case Failure(ex) => errBox.show(ex.getMessage)
        }
      }
      case Left(msg) => errBox.show(msg)
    }

  private def onDelete(): Unit =
    ShowMessage.confirm("この保険を削除していいですか？")(() => doDelete(kouhi.kouhiId))

  private def doDelete(kouhiId: Int): Unit =
    Api.deleteKouhi(kouhiId).onComplete {
      case Success(_) => block.close()
      case Failure(ex) => ShowMessage.showError(ex.getMessage)
    }

  private def onUpdated(event: CustomEvent[KouhiUpdated]): Unit =
    val updated = event.detail.updated
    val newSub = KouhiSubblock(updated)
    block.ele.replaceBy(newSub.block.ele)

