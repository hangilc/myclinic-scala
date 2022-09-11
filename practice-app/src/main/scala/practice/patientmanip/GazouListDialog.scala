package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import dev.myclinic.scala.model.FileInfo
import dev.myclinic.scala.util.FileUtil
import scala.language.implicitConversions

case class GazouListDialog(patientId: Int, files: List[FileInfo]):
  val display = div
  val selection = Selection[String]()
  selection.addAll(files.map(_.name).map(name => (div(name), name)))
  selection.addSelectEventHandler(fname => doSelect(fname))
  val dlog = new ModalDialog3()
  dlog.title("画像一覧")
  dlog.body(
    selection.ele,
    display
  )
  dlog.commands(
    button("閉じる", onclick := (() => dlog.close()))
  )

  def open(): Unit =
    dlog.open()

  def doSelect(fname: String): Unit =
    val url = s"/api/patient-image?patient-id=${patientId}&file-name=${fname}"
    if FileUtil.findFileExtension(fname) == Some("pdf") then
      val link = a("別のタブで開く", href := url, attr("target") := "_blank")
      display(clear, link)
    else
      val img = Html.img(cls := "practice-patient-image")
      img.src = url
      display(clear, img)


