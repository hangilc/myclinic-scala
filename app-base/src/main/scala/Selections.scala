package dev.myclinic.scala.appbase

import dev.myclinic.scala.web.appbase.LocalEventPublisher
import dev.myclinic.scala.model.{*, given}

class Selection[T]:
  var formatter: T => String = _.toString
  val onSelect = LocalEventPublisher[T]
  private val sel =
    dev.fujiwara.domq.Selection(onSelect = t => onSelect.publish(t))
  def ele = sel.ele
  def addItems(ts: List[T]): Unit =
    sel.addItems(ts, formatter)
  def addItem(t: T): Unit = addItems(List(t))

object Selections:
  def patientFormatter: Patient => String = patient =>
      String.format(
        "[%04d] %s",
        patient.patientId,
        patient.fullName()
      )

  def patientSelection(): Selection[Patient] =
    val s = new Selection[Patient]
    s.formatter = patientFormatter
    s

  def patientSelectionWithData[D](): Selection[(Patient, D)] =
    val s = new Selection[(Patient, D)]
    s.formatter = arg => patientFormatter(arg._1)
    s
