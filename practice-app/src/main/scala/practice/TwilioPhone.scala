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
  object PhoneNumberPattern:
    val canonicalPat: String = raw"(\+81\d{9})"
    val domesticPat: String = raw"()"
    val all: Regex = raw"(\+81\d{9})|[1-9]([0-9-]{9})|0([0-9-]{11,12})".r

  def probePhoneNumber(s: String): List[(Int, Int)] =
    PhoneNumberPattern.all.findAllMatchIn(s).map(m => (m.start, m.end)).toList

  def canonicalPhoneNumber(s: String): Option[String] =
    val canonical: Regex = raw"(\+81\d{9})".r
    val tokyo: Regex = raw"(\d{8})".r
    val local: Regex = raw"0(\d{9,10})".r
    s.replaceAll("[- ]", "") match {
      case canonical(ss) => Some(ss)
      case tokyo(ss) => Some(s"+813${ss}")
      case local(ss) => Some(s"+81${ss}")
      case _ => None
    }

