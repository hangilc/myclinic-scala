package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

class Row:
  val title = div
  val content = div
  val ele = div(
    title(cls := "doc-scan-subtitle"),
    content(cls := "doc-scan-subcontent")
  )