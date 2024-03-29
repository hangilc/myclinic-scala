package dev.myclinic.scala.db

import java.time._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.db.DoobieMapping._

object DbEventPrim:

  val CREATED: String = "created"
  val UPDATED: String = "updated"
  val DELETED: String = "deleted"

  def getAppEvent(id: Int): Query0[AppEvent] =
    sql"select * from app_event where app_event_id = ${id}".query[AppEvent]

  def enterAppEvent(
      model: String,
      kind: String,
      data: String
  ): ConnectionIO[AppEvent] =
    val createdAt = LocalDateTime.now()
    for
      id <- sql"""
          insert into app_event (created_at, model, kind, data) values (
            ${createdAt}, ${model}, ${kind}, ${data}
          )
        """.update.withUniqueGeneratedKeys[Int]("app_event_id")
      entered <- getAppEvent(id).unique
    yield entered

  def listGlobalEventSince(appEventId: Int): Query0[AppEvent] =
    val sql = sql""" 
      select * from app_event where app_event_id >= ${appEventId}
    """
    sql.query[AppEvent]

  def listGlobalEventInRange(
      fromEventId: Int,
      untilEventId: Int
  ): Query0[AppEvent] =
    val sql = sql""" 
      select * from app_event where app_event_id >= ${fromEventId} 
        and app_event_id < ${untilEventId}
    """
    sql.query[AppEvent]

  def nextGlobalEventId(): ConnectionIO[Int] =
    currentEventId().map(_ + 1)

  def currentEventId(): ConnectionIO[Int] =
    sql"""
      select app_event_id from app_event order by app_event_id desc limit 1
    """.query[Int].option.map(intOpt => intOpt match {
      case Some(id) => id
      case None => 0
    })

  def listHotlineByDate(date: LocalDate): Query0[AppEvent] =
    sql"""
      select * from app_event where model = 'hotline' and kind = 'created'
        and date(created_at) = ${date}
        order by app_event_id
    """.query[AppEvent]

  def logAppointTimeCreated(a: AppointTime): ConnectionIO[AppEvent] =
    enterAppEvent("appoint-time", CREATED, a.asJson.toString)

  def logAppointTimeUpdated(a: AppointTime): ConnectionIO[AppEvent] =
    enterAppEvent("appoint-time", UPDATED, a.asJson.toString)

  def logAppointTimeDeleted(a: AppointTime): ConnectionIO[AppEvent] =
    enterAppEvent("appoint-time", DELETED, a.asJson.toString)

  def logAppointCreated(a: Appoint): ConnectionIO[AppEvent] =
    enterAppEvent("appoint", CREATED, a.asJson.toString)

  def logAppointUpdated(a: Appoint): ConnectionIO[AppEvent] =
    enterAppEvent("appoint", UPDATED, a.asJson.toString)

  def logAppointDeleted(a: Appoint): ConnectionIO[AppEvent] =
    enterAppEvent("appoint", DELETED, a.asJson.toString)

  def logHotlineCreated(hotline: Hotline): ConnectionIO[AppEvent] =
    enterAppEvent("hotline", CREATED, hotline.asJson.toString)

  def logVisitCreated(a: Visit): ConnectionIO[AppEvent] =
    enterAppEvent("visit", CREATED, a.asJson.toString)

  def logVisitUpdated(a: Visit): ConnectionIO[AppEvent] =
    enterAppEvent("visit", UPDATED, a.asJson.toString)

  def logVisitDeleted(a: Visit): ConnectionIO[AppEvent] =
    enterAppEvent("visit", DELETED, a.asJson.toString)

  def logWqueueCreated(a: Wqueue): ConnectionIO[AppEvent] =
    enterAppEvent("wqueue", CREATED, a.asJson.toString)

  def logWqueueUpdated(a: Wqueue): ConnectionIO[AppEvent] =
    enterAppEvent("wqueue", UPDATED, a.asJson.toString)

  def logWqueueDeleted(a: Wqueue): ConnectionIO[AppEvent] =
    enterAppEvent("wqueue", DELETED, a.asJson.toString)

  def logPaymentCreated(a: Payment): ConnectionIO[AppEvent] =
    enterAppEvent(Payment.modelSymbol, CREATED, a.asJson.toString)

  def logPaymentUpdated(a: Payment): ConnectionIO[AppEvent] =
    enterAppEvent(Payment.modelSymbol, UPDATED, a.asJson.toString)

  def logPaymentDeleted(a: Payment): ConnectionIO[AppEvent] =
    enterAppEvent(Payment.modelSymbol, DELETED, a.asJson.toString)

  def logPatientCreated(a: Patient): ConnectionIO[AppEvent] =
    enterAppEvent("patient", CREATED, a.asJson.toString)

  def logPatientUpdated(a: Patient): ConnectionIO[AppEvent] =
    enterAppEvent("patient", UPDATED, a.asJson.toString)

  def logPatientDeleted(a: Patient): ConnectionIO[AppEvent] =
    enterAppEvent("patient", DELETED, a.asJson.toString)

  def logShahokokuhoCreated(a: Shahokokuho): ConnectionIO[AppEvent] =
    enterAppEvent("shahokokuho", CREATED, a.asJson.toString)

  def logShahokokuhoUpdated(a: Shahokokuho): ConnectionIO[AppEvent] =
    enterAppEvent("shahokokuho", UPDATED, a.asJson.toString)

  def logShahokokuhoDeleted(a: Shahokokuho): ConnectionIO[AppEvent] =
    enterAppEvent("shahokokuho", DELETED, a.asJson.toString)

  def logRoujinCreated(a: Roujin): ConnectionIO[AppEvent] =
    enterAppEvent("roujin", CREATED, a.asJson.toString)

  def logRoujinUpdated(a: Roujin): ConnectionIO[AppEvent] =
    enterAppEvent("roujin", UPDATED, a.asJson.toString)

  def logRoujinDeleted(a: Roujin): ConnectionIO[AppEvent] =
    enterAppEvent("roujin", DELETED, a.asJson.toString)

  def logKoukikoureiCreated(a: Koukikourei): ConnectionIO[AppEvent] =
    enterAppEvent("koukikourei", CREATED, a.asJson.toString)

  def logKoukikoureiUpdated(a: Koukikourei): ConnectionIO[AppEvent] =
    enterAppEvent("koukikourei", UPDATED, a.asJson.toString)

  def logKoukikoureiDeleted(a: Koukikourei): ConnectionIO[AppEvent] =
    enterAppEvent("koukikourei", DELETED, a.asJson.toString)

  def logKouhiCreated(a: Kouhi): ConnectionIO[AppEvent] =
    enterAppEvent("kouhi", CREATED, a.asJson.toString)

  def logKouhiUpdated(a: Kouhi): ConnectionIO[AppEvent] =
    enterAppEvent("kouhi", UPDATED, a.asJson.toString)

  def logKouhiDeleted(a: Kouhi): ConnectionIO[AppEvent] =
    enterAppEvent("kouhi", DELETED, a.asJson.toString)

  def logTextCreated(a: Text): ConnectionIO[AppEvent] =
    enterAppEvent("text", CREATED, a.asJson.toString)

  def logTextUpdated(a: Text): ConnectionIO[AppEvent] =
    enterAppEvent("text", UPDATED, a.asJson.toString)

  def logTextDeleted(a: Text): ConnectionIO[AppEvent] =
    enterAppEvent("text", DELETED, a.asJson.toString)

  def logShinryouCreated(a: Shinryou): ConnectionIO[AppEvent] =
    enterAppEvent("shinryou", CREATED, a.asJson.toString)

  def logShinryouUpdated(a: Shinryou): ConnectionIO[AppEvent] =
    enterAppEvent("shinryou", UPDATED, a.asJson.toString)

  def logShinryouDeleted(a: Shinryou): ConnectionIO[AppEvent] =
    enterAppEvent("shinryou", DELETED, a.asJson.toString)

  def logConductShinryouCreated(a: ConductShinryou): ConnectionIO[AppEvent] =
    enterAppEvent("conductShinryou", CREATED, a.asJson.toString)

  def logConductShinryouUpdated(a: ConductShinryou): ConnectionIO[AppEvent] =
    enterAppEvent("conductShinryou", UPDATED, a.asJson.toString)

  def logConductShinryouDeleted(a: ConductShinryou): ConnectionIO[AppEvent] =
    enterAppEvent("conductShinryou", DELETED, a.asJson.toString)

  def logConductCreated(a: Conduct): ConnectionIO[AppEvent] =
    enterAppEvent("conduct", CREATED, a.asJson.toString)

  def logConductUpdated(a: Conduct): ConnectionIO[AppEvent] =
    enterAppEvent("conduct", UPDATED, a.asJson.toString)

  def logConductDeleted(a: Conduct): ConnectionIO[AppEvent] =
    enterAppEvent("conduct", DELETED, a.asJson.toString)

  def logConductDrugCreated(a: ConductDrug): ConnectionIO[AppEvent] =
    enterAppEvent("conductDrug", CREATED, a.asJson.toString)

  def logConductDrugUpdated(a: ConductDrug): ConnectionIO[AppEvent] =
    enterAppEvent("conductDrug", UPDATED, a.asJson.toString)

  def logConductDrugDeleted(a: ConductDrug): ConnectionIO[AppEvent] =
    enterAppEvent("conductDrug", DELETED, a.asJson.toString)

  def logConductKizaiCreated(a: ConductKizai): ConnectionIO[AppEvent] =
    enterAppEvent("conductKizai", CREATED, a.asJson.toString)

  def logConductKizaiUpdated(a: ConductKizai): ConnectionIO[AppEvent] =
    enterAppEvent("conductKizai", UPDATED, a.asJson.toString)

  def logConductKizaiDeleted(a: ConductKizai): ConnectionIO[AppEvent] =
    enterAppEvent("conductKizai", DELETED, a.asJson.toString)

  def logGazouLabelCreated(a: GazouLabel): ConnectionIO[AppEvent] =
    enterAppEvent("gazouLabel", CREATED, a.asJson.toString)

  def logGazouLabelUpdated(a: GazouLabel): ConnectionIO[AppEvent] =
    enterAppEvent("gazouLabel", UPDATED, a.asJson.toString)

  def logGazouLabelDeleted(a: GazouLabel): ConnectionIO[AppEvent] =
    enterAppEvent("gazouLabel", DELETED, a.asJson.toString)

  def logChargeCreated(a: Charge): ConnectionIO[AppEvent] =
    enterAppEvent("charge", CREATED, a.asJson.toString)

  def logChargeUpdated(a: Charge): ConnectionIO[AppEvent] =
    enterAppEvent("charge", UPDATED, a.asJson.toString)

  def logChargeDeleted(a: Charge): ConnectionIO[AppEvent] =
    enterAppEvent("charge", DELETED, a.asJson.toString)

  def logDiseaseCreated(a: Disease): ConnectionIO[AppEvent] =
    enterAppEvent("disease", CREATED, a.asJson.toString)

  def logDiseaseUpdated(a: Disease): ConnectionIO[AppEvent] =
    enterAppEvent("disease", UPDATED, a.asJson.toString)

  def logDiseaseDeleted(a: Disease): ConnectionIO[AppEvent] =
    enterAppEvent("disease", DELETED, a.asJson.toString)

  def logDiseaseAdjCreated(a: DiseaseAdj): ConnectionIO[AppEvent] =
    enterAppEvent("diseaseAdj", CREATED, a.asJson.toString)

  def logDiseaseAdjUpdated(a: DiseaseAdj): ConnectionIO[AppEvent] =
    enterAppEvent("diseaseAdj", UPDATED, a.asJson.toString)

  def logDiseaseAdjDeleted(a: DiseaseAdj): ConnectionIO[AppEvent] =
    enterAppEvent("diseaseAdj", DELETED, a.asJson.toString)

  def logOnshiCreated(a: Onshi): ConnectionIO[AppEvent] =
    enterAppEvent("onshi", CREATED, a.asJson.toString)

  def logOnshiUpdated(a: Onshi): ConnectionIO[AppEvent] =
    enterAppEvent("onshi", UPDATED, a.asJson.toString)

  def logOnshiDeleted(a: Onshi): ConnectionIO[AppEvent] =
    enterAppEvent("onshi", DELETED, a.asJson.toString)

