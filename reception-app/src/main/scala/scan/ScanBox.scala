package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

class ScanBox:
  val title = div
  val content = div
  val ele = div(cls := "scan-box")(
    title(cls := "scan-box-title"), 
    content(cls := "scan-box-content")
  )
