package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{
  Icons,
  Form,
  ErrorBox,
  Modifier,
  ShowMessage,
  CustomEvent
}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.*
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.util.{HokenRep, RcptUtil}
import dev.myclinic.scala.apputil.FutanWari
import dev.myclinic.scala.web.appbase.SyncedDataSource

class ShahokokuhoSubblock(ds: => SyncedDataSource[Shahokokuho]):
  val eContent = div()
  val eCommands = div()
  val block: Subblock = Subblock(
    "社保国保",
    eContent,
    eCommands
  )
  disp()
  ds.onDelete(() => ele.remove())
  ds.startSync(ele)

  def ele = block.ele
  def gen: Int = ds.gen
  def shahokokuho: Shahokokuho = ds.data

  def disp(): Unit =
    val d = ShahokokuhoDisp(ds)
    eContent(clear, d.ele)
    eCommands(
      clear,
      button("削除", onclick := (onDelete _)),
      button("編集", onclick := (onEdit _)),
      button("閉じる", onclick := (() => block.ele.remove()))
    )

  def edit(): Unit =
    val form = ShahokokuhoForm()
    val errBox = ErrorBox()
    form.setData(shahokokuho)
    eContent(clear, errBox.ele, form.ele)
    eCommands(
      clear,
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
          case Success(_gen)  => disp()
          case Failure(ex) => errBox.show(ex.getMessage)
        }
      }
      case Left(msg) => errBox.show(msg)
    }

  private def onDelete(): Unit =
    ShowMessage.confirm("この保険を削除していいですか？")(() => doDelete(shahokokuho.shahokokuhoId))

  private def doDelete(shahokokuhoId: Int): Unit =
    Api.deleteShahokokuho(shahokokuhoId).onComplete {
      case Success(_)  => ()
      case Failure(ex) => ShowMessage.showError(ex.getMessage)
    }


