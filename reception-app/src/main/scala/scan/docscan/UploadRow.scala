package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import scala.concurrent.Future

class UploadRow(using ds: DataSources):
  import UploadRow.*
  val ele = div(
    button("アップロード", onclick := (doUpload _))
  )

  def doUpload(): Unit =
    ds.patient.data.map(_.patientId).foreach(patientId =>
      for doc <- ds.scannedDocs.data do
        doc.upload()
    )

object UploadRow
