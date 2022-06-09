package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html

case class PatientImageUploadIdalog():
  val form = Html.form
  val tagInput = input
  val tagPullDown = PullDown.pullDownLink("例", List(
    "画像" -> (() => tagExample("image")),
    "保険証" -> (() => tagExample("hokensho")),
    "健診結果" -> (() => tagExample("checkup")),
    "在宅報告" -> (() => tagExample("zaitaku")),
    "同意書" -> (() => tagExample("douisho"))
  ))
  val fileInput = input
  val dlog = new ModalDialog3()
  dlog.title("画像保存")
  dlog.body(
    div(
      span("Tag:"),
      tagInput,
      tagPullDown
    ),
    form(
      fileInput(attr("type") := "file")
    )
  )
  dlog.commands(
    button("アップロード"),
    button("キャンセル", onclick := (() => dlog.close()))
  )

  def open(): Unit =
    dlog.open()

  def tagExample(tag: String): Unit =
    ???