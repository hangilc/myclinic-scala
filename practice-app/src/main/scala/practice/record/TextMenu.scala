package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

class TextMenu:
  val newText = new LocalEventPublisher[Unit]
  val ele = div(
    a("新規文章", onclick := (() => newText.publish(())))
  )

object TextMenu:
  def apply(): TextMenu = new TextMenu()