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
import dev.myclinic.scala.chrome.PrintDialog
import dev.myclinic.scala.model.Shahokokuho
import dev.myclinic.scala.model.Koukikourei
import dev.myclinic.scala.model.Kouhi

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

  test("cashier"){
    val visit = client.startVisit(1, LocalDateTime.now())
    TestUtil.endVisit(client, visit.visitId, 1000)
    val table: WqueueTable = page.wqueueTable
    val row = table.waitForRowWithCashierButton(visit.visitId)
    row.cashierButton.click()
    val cashierDialog = CashierDialog(driver)
    cashierDialog.printReceiptButton.click()
    val printDialog = PrintDialog(driver)
    printDialog.cancel()
    cashierDialog.finishCashierButton.click()
    ElementUtil.waitForDisappear(driver, row.e)
  }

  test("new shahokokuho"){
    val patient = client.enterPatient(TestUtil.mockPatient())
    page.searchTextInput.sendKeys(s"${patient.patientId}\n")
    val dlog = PatientDialog(driver)
    var disp = dlog.asPatientDisp
    disp.newShahokokuhoLink.click()
    val shahoDlog = ShahokokuhoDialog(driver)
    val shaho = TestUtil.mockShahokokuho(patient.patientId)
    shahoDlog.set(shaho)
    shahoDlog.enter()
    disp = dlog.asPatientDisp
    val hokenList: List[Shahokokuho] = client.listShahokokuho(patient.patientId)
    val entered = hokenList(0)
    assert(shaho.copy(entered.shahokokuhoId) == entered)
    disp.close()
  }

  test("new koukikourei"){
    val patient = client.enterPatient(TestUtil.mockPatient())
    val koukikourei = TestUtil.mockKoukikourei(patient.patientId)
    page.searchTextInput.sendKeys(s"${patient.patientId}\n")
    val dlog = PatientDialog(driver)
    var disp = dlog.asPatientDisp
    disp.newKoukikoureiLink.click()
    val formDialog = KoukikoureiDialog(driver)
    formDialog.set(koukikourei)
    formDialog.enter()
    disp = dlog.asPatientDisp
    val hokenList: List[Koukikourei] = client.listKoukikourei(patient.patientId)
    val entered = hokenList(0)
    assert(koukikourei.copy(entered.koukikoureiId) == entered)
    disp.close()
  }

  test("new kouhi"){
    val patient = client.enterPatient(TestUtil.mockPatient())
    val kouhi = TestUtil.mockKouhi(patient.patientId)
    page.searchTextInput.sendKeys(s"${patient.patientId}\n")
    val dlog = PatientDialog(driver)
    var disp = dlog.asPatientDisp
    disp.newKouhiLink.click()
    val formDialog = KouhiDialog(driver)
    formDialog.set(kouhi)
    formDialog.enter()
    disp = dlog.asPatientDisp
    val hokenList: List[Kouhi] = client.listKouhi(patient.patientId)
    val entered = hokenList(0)
    assert(kouhi.copy(entered.kouhiId) == entered)
    Thread.sleep(10000)
    disp.close()
  }


