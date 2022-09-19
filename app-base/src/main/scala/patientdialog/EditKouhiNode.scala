package dev.myclinic.scala.web.appbase.patientdialog

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{TransNode, TransNodeRuntime}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.PatientReps
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appbase.reception.PatientImagesDialog
import java.time.LocalDateTime
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.web.appbase.PatientInputs
import Common.*
import dev.myclinic.scala.web.appbase.KouhiInputs

case class EditKouhiNode(hoken: Kouhi, state: State)
    extends TransNode[State](state):
  override def init(): Unit =
    ???
