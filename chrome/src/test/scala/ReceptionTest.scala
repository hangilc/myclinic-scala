package test

import dev.myclinic.scala.chrome.reception.*
import dev.myclinic.scala.chrome.reception.ReceptionLandingPage.*
import dev.myclinic.scala.chrome.TestUtil
import dev.myclinic.scala.model.Patient

class ReceptionTest extends TestBase:
  val page = ReceptionLandingPage(factory)

  test("search patient"){
    page.searchTextInput.sendKeys("1\n")
    val patientDisp = PatientDialog(driver).asPatientDisp
    confirm(patientDisp.patientId == "1")
    patientDisp.close()
  }

  test("search patient multi"){
    ensureSearchPatients(client)
    page.searchTextInput.sendKeys("Test Number")
    page.searchButton.click()
    val mode = PatientDialog(driver).asSearchResult
    val texts: List[String] = mode.searchResultTexts
    confirm(Range(2, 5).toList.forall(i =>
      val t = s"Test Number${i}"
      texts.find(_.contains(t)).isDefined
    ))
    mode.close()
  }

  test("new patient"){
    page.newPatientButton.click()
    val dlog = NewPatientDialog(driver)
    val p = TestUtil.mockPatient()
    dlog.setInputs(p)
    dlog.enter()
    val searched: List[Patient] = client.searchPatient(
      s"${p.lastName} ${p.firstName}"
    )
    confirm(!searched.isEmpty)
    val searchedLastPatientId: Int = searched.map(_.patientId).max
    val searchedLast: Patient = searched.find(_.patientId == searchedLastPatientId).get
    confirm(p.copy(patientId = searchedLast.patientId) == searchedLast)
    val disp = PatientDialog(driver).asPatientDisp
    disp.close()
  }