package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import dev.myclinic.scala.model.FileInfo

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
    val img = Html.img
    img.src = url
    display(clear, img)


