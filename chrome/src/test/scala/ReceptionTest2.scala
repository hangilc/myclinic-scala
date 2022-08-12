package test

import dev.myclinic.scala.chrome.reception.*
import dev.myclinic.scala.chrome.reception.ReceptionLandingPage.*
import dev.myclinic.scala.chrome.TestUtil
import dev.myclinic.scala.model.Patient
import java.time.LocalDateTime
import dev.myclinic.scala.model.Payment
import dev.myclinic.scala.model.Visit
import dev.myclinic.scala.model.Charge
import dev.myclinic.scala.chrome.ElementUtil

class ReceptionTest2 extends TestBase:
  val page = ReceptionLandingPage(factory)

  test("new visit"){
    val visit = client.startVisit(1, LocalDateTime.now())
    val table: WqueueTable = page.wqueueTable
    val row = table.waitForRow(visit.visitId)
    TestUtil.endVisit(client, visit.visitId, 0)
    client.finishCashier(Payment(visit.visitId, 0, LocalDateTime.now()))
    ElementUtil.waitForDisappear(driver, row.e)
  }