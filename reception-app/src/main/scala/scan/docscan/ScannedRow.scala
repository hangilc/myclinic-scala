package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.LocalDataSource

class ScannedRow(using ds: DataSources):
  val row = new Row
  row.title("スキャン文書")
  val docElementsWrapper = div
  row.content(docElementsWrapper)
  val ele = row.ele

  ds.newlyScannedFile.onUpdate(scannedFile => 
    val docs = ds.scannedDocs.data
    val index = docs.size + 1
    val item = new ScannedDoc(scannedFile, LocalDataSource[Int](index))
    docElementsWrapper(item.ele)
    ds.scannedDocs.update(docs :+ item)
  )

