package dev.myclinic.scala.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object PatientImageUtil:
  def composeFileName(patientId: Int, tag: String, at: LocalDateTime, index: Int, ext: String): String =
    val formatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss")
    val stamp = formatter.format(at)
     s"${patientId}-${tag}-${stamp}-${index}.${ext}"