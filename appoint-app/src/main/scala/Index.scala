package dev.myclinic.web

import org.scalajs.dom
import org.scalajs.dom.document
import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.model._

object JsMain {
  def main(args: Array[String]): Unit = {
    val body = document.body
    body.classList.add("pb-5")
    body.appendChild(banner)
    val app = Appoint(LocalDate.of(2021, 9, 22), LocalTime.of(9, 40, 0),
      "田中太郎", None, "")
    println(app)
  }

  val banner = {
    val html = """
    <div class="row pt-3 pb-2 ml-5 mr-5">
        <h1 class="bg-dark text-white p-3 col-md-12">診察予約</h1>
    </div>
    """
    val e = document.createElement("div")
    e.classList.add("container-fluid")
    e.innerHTML = html
    e
  }
}
