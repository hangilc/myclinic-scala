package dev.myclinic.scala.web.appbase

import org.scalajs.dom.{HTMLElement, document}
import dev.fujiwara.domq.all.{*, given}

object DomUtil:
  private lazy val store: HTMLElement = 
    val e = document.createElement("div").asInstanceOf[HTMLElement]
    e.style.display = "none"
    document.body.appendChild(e)
    e
  
  def hook(e: HTMLElement): Unit = store(e)
