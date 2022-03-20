package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}

class ScannedRow(using ds: DataSources):
  val row = new Row
  row.title("スキャン文書")
  val list = div
  row.content(list)
  val ele = row.ele

  ds.scannedDoc.onUpdate(scannedFile => 
    val item = new ScannedDoc(scannedFile)
    list(item.ele)
  )


