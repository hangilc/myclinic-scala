package dev.myclinic.scala.web.practiceapp.practice

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
object Twilio extends js.Object:
  @js.native
  class Device(val token: String, options: js.Dynamic)
      extends js.Object:
    def state: String = js.native
    def isBusy: Boolean = js.native
    def disconnectAll(): Unit = js.native
