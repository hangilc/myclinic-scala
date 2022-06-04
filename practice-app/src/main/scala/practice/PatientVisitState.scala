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

object PatientVisitState:
  type T = PatientVisitState
  private var hooks: Map[(T, T), () => Unit] = Map()

  def setHook(from: T, to: T, h: () => Unit): Unit =
    hooks += ((from, to), h) 

  def hook(from: T, to: T): () => Unit = hooks((from, to))
