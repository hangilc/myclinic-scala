package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.LocalEventPublisher
import org.scalajs.dom.HTMLElement

object PracticeBus:
  val addRightWidgetRequest = LocalEventPublisher[HTMLElement]

