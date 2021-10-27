package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Colors, ErrorBox}
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import dev.myclinic.scala.webclient.Api
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.{Patient}

class MemoPart(var memo: String, appointId: Int):
  val keyPart: HTMLElement = span("メモ：")
  val valuePart: HTMLElement = div()
  var valuePartHandler: ValuePartHandler = Disp()
  valuePartHandler.populate()

  def onMemoChanged(newMemo: String): Unit =
    memo = newMemo
    valuePartHandler.onMemoChanged()

  def setValuePartHandler(handler: ValuePartHandler): Unit =
    valuePartHandler = handler
    valuePartHandler.populate()

  trait ValuePartHandler:
    def populate(): Unit
    def onMemoChanged(): Unit

  class Disp() extends ValuePartHandler:
    val wrapper = valuePart
    val editIcon = Icons.pencilAlt(color = "gray")
    def populate(): Unit =
      val ele = div(
        span(text),
        editIcon(displayNone, ml := "0.5rem", Icons.defaultStyle)(
          onclick := (onEditClick _)
        )
      )
      ele(onmouseenter := (() => {
        editIcon(displayDefault)
        ()
      }))
      ele(onmouseleave := (() => {
        editIcon(displayNone)
        ()
      }))
      wrapper.innerHTML = ""
      wrapper(ele)
    def onMemoChanged(): Unit = populate()
    def onEditClick(): Unit =
      setValuePartHandler(Edit())
    def text: String = 
      if memo.isEmpty then "（設定なし）"
      else memo

  class Edit() extends ValuePartHandler:
    val wrapper = valuePart
    val input = inputText()
    val enterIcon = Icons.checkCircle(color = Colors.primary)
    val discardIcon = Icons.xCircle(color = Colors.danger)
    enterIcon(onclick := (onEnter _))
    discardIcon(onclick := (() => {
      setValuePartHandler(Disp())
    }))
    def populate(): Unit =
      wrapper.innerHTML = ""
      wrapper(
        input(value := memo),
        enterIcon(Icons.defaultStyle, ml := "0.5rem"),
        discardIcon(Icons.defaultStyle)
      )
    def onMemoChanged(): Unit = 
      input.value = memo
    def onEnter(): Unit = 
      for
        appoint <- Api.getAppoint(appointId)
        newAppoint = appoint.copy(memo = input.value)
        _ <- Api.updateAppoint(newAppoint)
      yield {
        setValuePartHandler(Disp())
      }

