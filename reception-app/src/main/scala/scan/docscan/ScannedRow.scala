package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.LocalDataSource
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.webclient.global
import cats.*
import cats.syntax.all.*

class ScannedRow(using ds: DataSources):
  val row = new Row
  row.title("スキャン文書")
  val docElementsWrapper = div
  row.content(docElementsWrapper)
  val ele = row.ele

  ds.newlyScannedFile.onUpdate(scannedFile => 
    val docs = ds.scannedDocs.data
    val index = docs.size + 1
    val item = new ScannedDoc(scannedFile, index)
    docElementsWrapper(item.ele)
    ds.scannedDocs.update(docs :+ item)
  )

  ds.reqDelete.onUpdate(index => {
    println(("enter reqDelete", index))
    val busy = ds.scannedDocs.data.filter(doc => doc.getState != ScannedDoc.State.Scanned).size > 0
    if !busy then
      val f: Future[List[ScannedDoc]] = 
        val (pre, post) = ds.scannedDocs.data.span(doc => doc.getIndex != index)
        post match {
          case Nil => Future.successful(Nil)
          case doc :: Nil => doc.dispose().map(_ => pre)
          case doc :: rest =>
            val docApi = ScannedDoc.DocApi(ds)
            val f = ds.scannedDocs.data.sliding(2, 1).toList.map(_ match {
              case a :: b :: Nil => a.swapWith(b)
              case _ => throw new Exception("Cannot happen.")
            }).sequence.void
            for
              _ <- f
              _ <- doc.dispose()
            yield pre ++ rest
        }
      f.onComplete {
        case Success(docs) => ds.scannedDocs.update(docs)
        case Failure(ex) => System.err.println(ex.getMessage)
      }
    else System.err.println("Cannot delete doc: BUSY")
  })


