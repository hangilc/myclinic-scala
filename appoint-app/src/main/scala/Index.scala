package dev.myclinic.web

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.model._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.{Dictionary}
import scala.concurrent._
import org.scalajs.dom.experimental.{URL, URLSearchParams}
import scala.util.{Success, Failure}
import io.circe.parser.decode
import dev.myclinic.scala.web.Api
import dev.myclinic.scala.util.DateUtil

object JsMain {
  def main(args: Array[String]): Unit = {
    val body = document.body
    body.classList.add("pb-5")
    body.appendChild(banner)
    val api = Api
    for(
      apps <- api.listAppoint(LocalDate.of(2021,8,1), LocalDate.of(2021,8,30))
    ) yield {
      println(apps)
    }
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
