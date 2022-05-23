package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.db.DoobieMapping.{given}

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

object DbVisitExPrim:
  private def frag(s: String): Fragment = Fragment.const(s)
  private def validAt(what: Fragment, span: Fragment): Fragment =
    fr"""
      (${what} >= ${span}.valid_from and (
        ${what} <= ${span}.valid_upto || ${span}.valid_upto = '0000-00-00'
      )) 
    """

  def getDrugEx(drugId: Int): Query0[DrugEx] =
    (sql"""
      select d.*, m.* from visit_drug as d inner join visit as v 
        inner join iyakuhin_master_arch as m 
        where d.drug_id = ${drugId} and d.visit_id = v.visit_id 
        and d.d_iyakuhincode = m.iyakuhincode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(Drug, IyakuhinMaster)]
      .map({ case (d, m) =>
        DrugEx(d, m)
      })

  def listDrugEx(drugIds: List[Int]): ConnectionIO[List[DrugEx]] =
    drugIds.map(getDrugEx(_).unique).sequence

  def getShinryouEx(shinryouId: Int): Query0[ShinryouEx] =
    (sql"""
      select s.*, m.* from visit_shinryou as s inner join visit as v 
        inner join shinryoukoui_master_arch as m 
        where s.shinryou_id = ${shinryouId} and s.visit_id = v.visit_id 
        and s.shinryoucode = m.shinryoucode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(Shinryou, ShinryouMaster)]
      .map({ case (s, m) =>
        ShinryouEx(s, m)
      })

  def listShinryouEx(shinryouIds: List[Int]): ConnectionIO[List[ShinryouEx]] =
    shinryouIds.map(getShinryouEx(_).unique).sequence

  def getConductDrugEx(conductDrugId: Int): Query0[ConductDrugEx] =
    (sql"""
      select d.*, m.* from visit_conduct_drug as d inner join visit_conduct as c 
        inner join visit as v 
        inner join iyakuhin_master_arch as m 
        where d.id = ${conductDrugId} and d.visit_conduct_id = c.id 
        and c.visit_id = v.visit_id 
        and d.iyakuhincode = m.iyakuhincode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(ConductDrug, IyakuhinMaster)]
      .map({ case (d, m) =>
        ConductDrugEx(d, m)
      })

  def listConductDrugEx(
      conductDrugIds: List[Int]
  ): ConnectionIO[List[ConductDrugEx]] =
    conductDrugIds.map(getConductDrugEx(_).unique).sequence

  def getConductShinryouEx(conductShinryouId: Int): Query0[ConductShinryouEx] =
    (sql"""
      select s.*, m.* from visit_conduct_shinryou as s 
        inner join visit_conduct as c on s.visit_conduct_id = c.id 
        inner join visit as v on c.visit_id = v.visit_id
        inner join shinryoukoui_master_arch as m 
        where s.id = ${conductShinryouId}  
        and s.shinryoucode = m.shinryoucode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(ConductShinryou, ShinryouMaster)]
      .map({ case (s, m) =>
        ConductShinryouEx(s, m)
      })

  def listConductShinryouEx(
      conductShinryouIds: List[Int]
  ): ConnectionIO[List[ConductShinryouEx]] =
    conductShinryouIds.map(getConductShinryouEx(_).unique).sequence

  def getConductKizaiEx(conductKizaiId: Int): Query0[ConductKizaiEx] =
    (sql"""
      select k.*, m.* from visit_conduct_kizai as k inner join visit_conduct as c 
        inner join visit as v 
        inner join tokuteikizai_master_arch as m 
        where k.id = ${conductKizaiId} and k.visit_conduct_id = c.id 
        and c.visit_id = v.visit_id 
        and k.kizaicode = m.kizaicode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(ConductKizai, KizaiMaster)]
      .map({ case (k, m) =>
        ConductKizaiEx(k, m)
      })

  def listConductKizaiEx(
      conductKiaiIds: List[Int]
  ): ConnectionIO[List[ConductKizaiEx]] =
    conductKiaiIds.map(getConductKizaiEx(_).unique).sequence

  def getConductEx(conductId: Int): ConnectionIO[ConductEx] =
    for
      conduct <- DbConductPrim.getConduct(conductId).unique
      drugIds <- DbConductDrugPrim.listConductDrugIdForConduct(conductId)
      gazouLabel <- DbGazouLabelPrim.findGazouLabel(conductId).option
      drugs <- listConductDrugEx(drugIds)
      shinryouIds <- DbConductShinryouPrim.listConductShinryouIdForConduct(
        conductId
      )
      shinryouList <- listConductShinryouEx(shinryouIds)
      kizaiIds <- DbConductKizaiPrim.listConductKizaiIdForConduct(conductId)
      kizaiList <- listConductKizaiEx(kizaiIds)
    yield ConductEx(conduct, gazouLabel.map(_.label), drugs, shinryouList, kizaiList)

  def listConductEx(conductIds: List[Int]): ConnectionIO[List[ConductEx]] =
    conductIds.map(getConductEx(_)).sequence

  def optShahokokuho(shahokokuhoId: Int): ConnectionIO[Option[Shahokokuho]] =
    if shahokokuhoId > 0 then
      DbShahokokuhoPrim.getShahokokuho(shahokokuhoId).unique.map(Some(_))
    else None.pure[ConnectionIO]

  def optRoujin(roujinId: Int): ConnectionIO[Option[Roujin]] =
    if roujinId > 0 then
      DbRoujinPrim.getRoujin(roujinId).unique.map(Some(_))
    else None.pure[ConnectionIO]

  def optKoukikourei(koukikoureiId: Int): ConnectionIO[Option[Koukikourei]] =
    if koukikoureiId > 0 then
      DbKoukikoureiPrim.getKoukikourei(koukikoureiId).unique.map(Some(_))
    else None.pure[ConnectionIO]

  def optKouhi(kouhiId: Int): ConnectionIO[Option[Kouhi]] =
    if kouhiId > 0 then
      DbKouhiPrim.getKouhi(kouhiId).unique.map(Some(_))
    else None.pure[ConnectionIO]

  def kouhiList(kouhiIds: List[Int]): ConnectionIO[List[Kouhi]] =
    kouhiIds.map(DbKouhiPrim.getKouhi(_).unique).sequence

  def getVisitEx(visitId: Int): ConnectionIO[VisitEx] =
    for
      visit <- DbVisitPrim.getVisit(visitId).unique
      patient <- DbPatientPrim.getPatient(visit.patientId).unique
      shahokokuho <- optShahokokuho(visit.shahokokuhoId)
      roujin <- optRoujin(visit.roujinId)
      koukikourei <- optKoukikourei(visit.koukikoureiId)
      kouhiList <- kouhiList(visit.kouhiIds)
      texts <- DbTextPrim.listTextForVisit(visitId)
      drugIds <- DbDrugPrim.listDrugIdForVisit(visitId)
      drugs <- listDrugEx(drugIds)
      shinryouIds <- DbShinryouPrim.listShinryouIdForVisit(visitId)
      shinryouList <- listShinryouEx(shinryouIds)
      conductIds <- DbConductPrim.listConductIdForVisit(visitId)
      conducts <- listConductEx(conductIds)
      charge <- DbChargePrim.getCharge(visitId).option
      payment <- DbPaymentPrim.findLastPayment(visitId).option
    yield VisitEx(visit, patient, shahokokuho, roujin, koukikourei, kouhiList,
    texts, drugs, shinryouList, conducts, charge, payment)
