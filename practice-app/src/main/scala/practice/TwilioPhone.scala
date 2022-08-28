package dev.myclinic.scala.web.practiceapp.practice.twilio

import scala.concurrent.Future
import dev.myclinic.scala.webclient.global
import scala.util.matching.Regex

class TwilioPhone(getToken: () => Future[String]):
  private var deviceOpt: Option[Device] = None

  private def setDevice(device: Device): Unit =
    deviceOpt = Some(device)
  private def clearDevice(): Unit =
    deviceOpt = None

  def call(phoneNumber: String): Boolean =
    deviceOpt.fold({
      println(("calling to: ", phoneNumber))
      for
        token <- getToken()
        device = new Device(token, new DeviceOptions(edge = Some("tokyo")))
        _ = setDevice(device)
        call <- device.connect(ConnectOptions(params = Map("phone" -> phoneNumber)))
      yield
        call.onDisconnect(_ => clearDevice())
      true
    })(_ => false)

  def hangup(): Unit =
    deviceOpt.foreach(_.disconnectAll())

object TwilioPhone:
  def canonicalPhoneNumber(s: String): Option[String] =
    val canonical: Regex = raw"\+81\d{9}".r
    val tokyo: Regex = raw"\d{8}".r
    val local: Regex = raw"0(\d{9,10})".r
    s.replace("-", "") match {
      case canonical(s) => Some(s)
      case tokyo(s) => Some(s"+813${s}")
      case local(s) => Some(s"+81${s}")
      case _ => None
    }

