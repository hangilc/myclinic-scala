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

object JsMain {
  def main(args: Array[String]): Unit = {
    val body = document.body
    body.classList.add("pb-5")
    body.appendChild(banner)
    Api.hello()
    val d = Dictionary("from" -> "2021-08-01", "upto" -> "2021-08-30")
    val p = new URLSearchParams(d)
    val url = "/api/list-appoint?" + p
    println(url)
    Ajax.get(url).onComplete{
      case Success(xhr) => {
        val txt = xhr.responseText
        val apps: List[Appoint] = decode[List[Appoint]](txt).getOrElse(List())
        println(apps)
      }
      case Failure(e) => throw e
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
