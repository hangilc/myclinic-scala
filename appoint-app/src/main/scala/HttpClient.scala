package dev.myclinic.scala.web

import java.time.{LocalDate, LocalTime}
import java.time.format.DateTimeFormatter
import scala.scalajs.js.{Dictionary}
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.experimental.URLSearchParams
import org.scalajs.dom.ext.Ajax
import io.circe.parser.decode
import io.circe.Decoder
import scala.concurrent.{Future}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

trait ParamValue {
  def encode(): String
}

object ParamValue {
  implicit def strParam(s: String): ParamValue = () => s

  implicit def intParam(i: Int): ParamValue = () => i.toString

  implicit def dateParam(d: LocalDate): ParamValue = () => d.toString

  private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  implicit def timeParam(t: LocalTime): ParamValue = {
    t.format(timeFormatter)
  }
}

class Params(){
  private val items = ListBuffer.empty[(String, String)]

  def add(k: String, p: ParamValue){
    val pair = (k, p.encode())
    items += pair
  }

  def isEmpty: Boolean = items.isEmpty

  def encode(): String = {
    val d = Dictionary(items.toList: _*)
    val p = new URLSearchParams(d)
    p.toString()
  }
}

object Params {
  def apply(items: (String, ParamValue)*) : Params = {
    val params = new Params()
    for((k, p) <- items){
      params.add(k, p)
    }
    params
  }
}

object HttpClient {

  val pre = "/api/"

  def url(service: String): String = pre + service

  def url(service: String, params: Params): String = {
    if( params.isEmpty ){
      url(service)
    } else {
      url(service) + "?" + params.encode()
    }
  }
  
  def hello(){
    println(url("get-patient"))
    println( url("get-patient", Params("patient-id" -> 123)))
    println(url("list-appoing", Params("from" -> LocalDate.of(2021, 8, 1), 
      "upto" -> LocalDate.of(2021, 8, 30))))
  }

  def get[A](service: String, params: Params)(implicit dec: Decoder[A]) = {
    Ajax.get(url(service, params)).flatMap(
      xhr => decode[A](xhr.responseText)
    )
  }
}