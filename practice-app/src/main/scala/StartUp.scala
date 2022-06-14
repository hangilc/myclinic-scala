package dev.myclinic.scala.web.practiceapp

import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import dev.fujiwara.domq.all.{*, given}

object StartUp:
  def run(main: JsMain): Unit =
    main.ui.sideMenu.invokeByLabel("診察")