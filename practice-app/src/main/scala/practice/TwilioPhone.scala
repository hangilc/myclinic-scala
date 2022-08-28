package dev.myclinic.scala.web.practiceapp.practice.twilio

import scala.concurrent.Future
import dev.myclinic.scala.webclient.global

class TwilioPhone(getToken: () => Future[String]):
  val device = new Device("", new DeviceOptions(edge = Some("tokyo")))
  def hangup(): Unit = device.disconnectAll()
  def call(phoneNumber: String): Future[Call] =
    for
      token <- getToken()
       _ = device.updateToken(token)
      call <- device.connect(ConnectOptions(params = Map("phone" -> phoneNumber)))
    yield call
