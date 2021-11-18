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

trait DbVisitEx extends Mysql:
  private def frag(s: String): Fragment = Fragment.const(s)
  private def validAt(what: Fragment, span: Fragment): Fragment =
    fr"""
      (${what} >= ${span}.valid_from and (
        ${what} <= ${span}.valid_upto || ${span}.valid_upto = '0000-00-00'
      )) 
    """

  def getDrugEx(drugId: Int): IO[DrugEx] =
    val op = (sql"""
      select d.*, m.* from visit_drug as d inner join visit as v 
        inner join iyakuhin_master_arch as m 
        where d.drug_id = ${drugId} and d.visit_id = v.visit_id 
        and d.d_iyakuhincode = m.iyakuhincode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(Drug, IyakuhinMaster)]
      .unique
      .map({ case (d, m) =>
        DrugEx(d, m)
      })
    mysql(op)

  def getShinryouEx(shinryouId: Int): IO[ShinryouEx] =
    val op = (sql"""
      select s.*, m.* from visit_shinryou as s inner join visit as v 
        inner join shinryoukoui_master_arch as m 
        where s.shinryou_id = ${shinryouId} and s.visit_id = v.visit_id 
        and s.shinryoucode = m.shinryoucode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(Shinryou, ShinryouMaster)]
      .unique
      .map({ case (s, m) =>
        ShinryouEx(s, m)
      })
    mysql(op)

  def getConductDrugEx(conductDrugId: Int): IO[ConductDrugEx] =
    mysql(DbVisitExPrim.getConductDrugEx(conductDrugId).unique)

  def getConductShinryouEx(conductShinryouId: Int): IO[ConductShinryouEx] =
    mysql(DbVisitExPrim.getConductShinryouEx(conductShinryouId).unique)

  def getConductKizaiEx(conductKizaiId: Int): IO[ConductKizaiEx] =
    mysql(DbVisitExPrim.getConductKizaiEx(conductKizaiId).unique)

  def getConductEx(conductId: Int): IO[ConductEx] =
    mysql(DbVisitExPrim.getConductEx(conductId))

  def listConductEx(conductIds: List[Int]): IO[List[ConductEx]] =
    mysql(DbVisitExPrim.listConductEx(conductIds))
