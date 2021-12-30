package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.model.Patient

case class ScanBoxState(
    patient: Option[Patient],
    kind: String,
    device: Option[String]
)
