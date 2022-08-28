package dev.myclinic.scala.web.practiceapp.practice.twilio

import scala.concurrent.Future
import dev.myclinic.scala.webclient.global

class TwilioPhone(getToken: () => Future[String]):
  def call(phoneNumber: String): Future[Call] =
    for
      token <- getToken()
      device = new Device(token, new DeviceOptions(edge = Some("tokyo")))
      call <- device.connect(ConnectOptions(params = Map("phone" -> phoneNumber)))
    yield call
