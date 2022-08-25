package dev.myclinic.scala.web.practiceapp.practice

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.Promise

@js.native
@JSGlobal
object Twilio extends js.Object:
  @js.native
  class Device(val token: String, options: js.Any)
      extends js.Object:
    def state: String = js.native
    def isBusy: Boolean = js.native
    def connect(options: js.Any): Promise[Call] = js.native
    def disconnectAll(): Unit = js.native

  @js.native
  class Call extends js.Object
