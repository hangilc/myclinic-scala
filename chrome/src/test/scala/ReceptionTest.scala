package test

import dev.myclinic.scala.chrome.reception.ReceptionLandingPage.apply
import dev.myclinic.scala.chrome.reception.ReceptionLandingPage

class ReceptionTest extends TestBase:
  val page = ReceptionLandingPage(factory)

  test("one"){
    assert(1 == 1)
  }