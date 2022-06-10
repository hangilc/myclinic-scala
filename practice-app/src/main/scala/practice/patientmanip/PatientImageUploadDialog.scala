package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import org.scalajs.dom.FormData

case class PatientImageUploadIdalog():
  val form = Html.form
  form.target = "/api/upload-patient-image"
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
      fileInput(attr("type") := "file", name := "uploadfile")
    )
  )
  dlog.commands(
    button("アップロード", onclick := (doSubmit _)),
    button("キャンセル", onclick := (() => dlog.close()))
  )

  def open(): Unit =
    dlog.open()

  def doSubmit(): Unit =
    if fileInput.files.length == 1 then
      val file = fileInput.files.item(0)
      val formData = new FormData()
      formData.append("uploadfile", file)
      val init = new org.scalajs.dom.RequestInit{}
      init.method = org.scalajs.dom.HttpMethod.POST
      init.body = formData
      org.scalajs.dom.fetch("/api/upload-patient-image?patient-id=4593&file-name=test.jpg", init)

  def tagExample(tag: String): Unit =
    ???