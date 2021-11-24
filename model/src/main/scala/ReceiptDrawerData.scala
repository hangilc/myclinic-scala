package dev.myclinic.scala.model

import io.circe.*
import io.circe.syntax.*
import io.circe.parser.*
import io.circe.generic.semiauto._

case class ReceiptDrawerData(
  patientName: String = "",
  charge: String = "",
  visitDate: String = "",
  issueDate: String = "",
  patientId: String = "",
  hoken: String = "",
  futanWari: String = "",
  shoshin: String = "",
  kanri: String = "",
  zaitaku: String = "",
  kensa: String = "",
  gazou: String = "",
  touyaku: String = "",
  chuusha: String = "",
  shochi: String = "",
  sonota: String = "",
  souten: String = "",
  hokengai: List[String] = List("", "", "", ""),
  clinicName: String = "",
  addressLines: List[String] = List.empty
)

object ReceiptDrawerData:
  given Encoder[ReceiptDrawerData] = deriveEncoder[ReceiptDrawerData]
  given Decoder[ReceiptDrawerData] = deriveDecoder[ReceiptDrawerData]


