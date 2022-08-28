package dev.myclinic.scala.web.practiceapp.practice.twilio

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.Promise
import js.JSConverters.*
import scala.collection.mutable
import scala.concurrent.Future
import dev.myclinic.scala.webclient.global

object Native:
  @js.native
  @JSGlobal("Twilio.Device")
  class Device(val token: String, opts: js.Dynamic) extends js.Object:
    def connect(opts: js.Dynamic): js.Promise[Call] = js.native
    def edge: js.Any = js.native
    def disconnectAll(): Unit = js.native
    def on(event: String, handler: js.Any): Unit = js.native
    def updateToken(newToken: String): Unit = js.native

  object Device:
    def apply(token: String, opts: DeviceOptions): Device =
      new Device(token, opts.toJsObject)

  @js.native
  @JSGlobal("Twilio.Call")
  class Call extends js.Object:
    def disconnect(): Unit = js.native
    def on(event: String, handler: js.Any): Unit = js.native
    def status(): String = js.native

class Device(token: String, opts: DeviceOptions):
  val dev: Native.Device = new Native.Device(token, opts.toJsObject)
  def connect(opts: ConnectOptions): Future[Call] = 
    dev.connect(opts.toJsObject).toFuture.map(c => new Call(c))
  def disconnectAll(): Unit = dev.disconnectAll()
  def onError(handler: TwilioError => Unit): Unit =
    val f: js.Function1[js.Dynamic, Unit] = e => handler(new TwilioError(e))
    dev.on("error", f)
  def updateToken(newToken: String): Unit = dev.updateToken(newToken)

class Call(native: Native.Call):
  def disconnect(): Unit = native.disconnect()
  def onDisconnect(handler: Call => Unit): Unit =
    val f: js.Function1[Native.Call, Unit] = c => handler(Call(c))
    native.on("disconnect", f)
  def onError(handler: TwilioError => Unit): Unit =
    val f: js.Function1[js.Dynamic, Unit] = e => handler(new TwilioError(e))
    native.on("error", f)
  def status: String = native.status()

case class DeviceOptions(
  edge: Option[String | Seq[String]] = None
):
  def toJsObject: js.Dynamic = 
    val obj: js.Dynamic = js.Dynamic.literal()
    edge match {
      case Some(s: String) => obj.edge = s
      case Some(ss: Seq[String]) => obj.edge = ss.toJSArray
      case None => ()
    }
    obj

case class ConnectOptions(
  params: Map[String, String]
):
  def toJsObject: js.Dynamic =
    val obj: js.Dynamic = js.Dynamic.literal()
    val objParams: js.Dynamic = js.Dynamic.literal()
    params.foreach {
      (k, v) => objParams.updateDynamic(k)(v)
    }
    obj.params = objParams
    obj

class TwilioError(native: js.Dynamic):
  def causes: List[String] = 
    val cs: mutable.Seq[String] = native.causes.asInstanceOf[js.Array[String]]
    cs.toList

