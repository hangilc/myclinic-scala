package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.model.Patient

sealed trait PatientVisitState:
  def patientOption: Option[Patient]
  def visitIdOption: Option[Int]

object NoSelection extends PatientVisitState:
  def patientOption: Option[Patient] = None
  def visitIdOption: Option[Int] = None

case class Browsing(patient: Patient) extends PatientVisitState:
  def patientOption: Option[Patient] = Some(patient)
  def visitIdOption: Option[Int] = None

case class Practicing(patient: Patient, visitId: Int) extends PatientVisitState:
  def patientOption: Option[Patient] = Some(patient)
  def visitIdOption: Option[Int] = Some(visitId)

case class PracticeDone(patient: Patient, visitId: Int) extends PatientVisitState:
  def patientOption: Option[Patient] = Some(patient)
  def visitIdOption: Option[Int] = Some(visitId)


