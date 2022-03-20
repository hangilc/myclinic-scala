package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import scala.concurrent.Future

class UploadRow(reqClose: () => Unit)(using ds: DataSources):
  import UploadRow.*
  val ele = div(
    button("アップロード", onclick := (doUpload _)),
    button("閉じる", onclick := (doClose _))
  )

  def doUpload(): Unit =
    ds.patient.data.map(_.patientId).foreach(patientId =>
      for doc <- ds.scannedDocs.data do
        doc.upload()
    )

  def doClose(): Unit =
    reqClose()

object UploadRow
