package dev.myclinic.scala.drawerform.receipt

import io.circe.*
import io.circe.syntax.*
import io.circe.parser.*
import io.circe.generic.semiauto._
import dev.myclinic.scala.model.Patient

case class ReceiptDrawerData(
  var patientName: String = "",
  var charge: Int = 0,
  var visitDate: String = "",
  var issueDate: String = "",
  var patientId: String = "",
  var hoken: String = "",
  var futanWari: String = "",
  var shoshin: String = "",
  var kanri: String = "",
  var zaitaku: String = "",
  var kensa: String = "",
  var gazou: String = "",
  var touyaku: String = "",
  var chuusha: String = "",
  var shochi: String = "",
  var sonota: String = "",
  var souten: String = "",
  var hokengai: List[String] = List("", "", "", "")
):
  def setPatient(patient: Patient): Unit =
    patientName = patient.fullName()
    patientId = patient.patientId.toString


object ReceiptDrawerData:
  given Encoder[ReceiptDrawerData] = deriveEncoder[ReceiptDrawerData]
  given Decoder[ReceiptDrawerData] = deriveDecoder[ReceiptDrawerData]


