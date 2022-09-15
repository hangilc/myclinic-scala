package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Meisai
import org.scalajs.dom.HTMLFormElement
import dev.fujiwara.domq.Html
import dev.myclinic.scala.webclient.{Api, global}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Payment
import java.time.LocalDateTime
import dev.myclinic.scala.model.WaitState

case class CashierDialog(meisai: Meisai, visitId: Int, onDone: Option[WaitState] => Unit):
  private var chargeValue: Int = meisai.charge
  val formWrapper = div
  val chargeWrapper = span
  val mishuuCheckLabel = CheckLabel[Boolean](false, "未収扱")
  val enterButton = button
  val cancelButton = button
  val dlog = new ModalDialog3()
  dlog.content(cls := "practice-cashier-dialog")
  dlog.title(innerText := "会計")
  dlog.body(
    div(innerText := detail),
    div(innerText := summary),
    div(
      chargeWrapper(chargeValueText),
      a("変更", onclick := (doChange _))
    ),
    formWrapper(displayNone)
  )
  dlog.commands(
    mishuuCheckLabel.wrap(span(cls := "mishuu-check-label-wrapper")),
    enterButton(
      "入力",
      onclick := (() => {
        if mishuuCheckLabel.isChecked then doMishuuEnter()
        else doEnter()
      })
    ),
    cancelButton("キャンセル", onclick := (() => dlog.close()))
  )

  def updateUI(): Unit =
    chargeWrapper(clear, chargeValueText)

  def detail: String =
    meisai.items
      .map(item => s"${item.section.label}：${item.subtotal}点")
      .mkString("\n")

  def summary: String =
    s"総点：${meisai.totalTen}点、負担割：${meisai.futanWari}割"

  def chargeValueText: String =
    s"請求額：${chargeValue}円"

  def doChange(): Unit =
    val form = CashierDialog.ChangeForm()
    form.enter(onclick := (() =>
      form.getValue match {
        case Left(msg) => ShowMessage.showError(msg)
        case Right(value) =>
          chargeValue = value
          updateUI()
          formWrapper(displayNone, clear)
          enterButton(enabled := true)
      }
      ()
    ))
    form.cancel(onclick := (() =>
      formWrapper(displayNone, clear)
      enterButton(enabled := true)
      ()
    ))
    formWrapper(clear, form.ele(cls := "practice-cashier-dialog-change-form"))
    formWrapper(displayDefault)
    enterButton(disabled := true)
    form.initFocus()

  def doEnter(): Unit =
    for 
      _ <- Api.enterChargeValue(visitId, chargeValue)
      _ <- Api.changeWqueueState(visitId, WaitState.WaitCashier)
    yield
      dlog.close()
      onDone(Some(WaitState.WaitCashier))

  def doMishuuEnter(): Unit =
    for 
      _ <- Api.enterChargeValue(visitId, chargeValue)
      _ <- {
        val pay = Payment(visitId, 0, LocalDateTime.now())
        Api.finishCashier(pay)
      }
    yield
      dlog.close()
      onDone(None)

  def open(): Unit =
    dlog.open()

object CashierDialog:
  case class ChangeForm():
    val input = Html.inputText
    val enter = button
    val cancel = button
    val ele = form(
      cls := "change-charge-form",
      span("変更金額："),
      input,
      span("円", cls := "en-label"),
      enter("入力"),
      cancel("キャンセル")
    )

    def getValue: Either[String, Int] =
      input.value.toIntOption match {
        case None    => Left("変更金額の入力が不適切です。")
        case Some(n) => Right(n)
      }

    def initFocus(): Unit =
      input.focus()
