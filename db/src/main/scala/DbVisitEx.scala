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
    val op = (sql"""
      select d.*, m.* from visit_conduct_drug as d inner join visit_conduct as c 
        inner join visit as v 
        inner join iyakuhin_master_arch as m 
        where d.id = ${conductDrugId} and d.visit_conduct_id = c.id 
        and c.visit_id = v.visit_id 
        and d.iyakuhincode = m.iyakuhincode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(ConductDrug, IyakuhinMaster)]
      .unique
      .map({ case (d, m) =>
        ConductDrugEx(d, m)
      })
    mysql(op)

  def getConductShinryouEx(conductShinryouId: Int): IO[ConductShinryouEx] =
    val op = (sql"""
      select s.*, m.* from visit_conduct_shinryou as s 
        inner join visit_conduct as c on s.visit_conduct_id = c.id 
        inner join visit as v on c.visit_id = v.visit_id
        inner join shinryoukoui_master_arch as m 
        where s.id = ${conductShinryouId}  
        and s.shinryoucode = m.shinryoucode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(ConductShinryou, ShinryouMaster)]
      .unique
      .map({ case (s, m) =>
        ConductShinryouEx(s, m)
      })
    mysql(op)

  def getConductKizaiEx(conductKizaiId: Int): IO[ConductKizaiEx] =
    val op = (sql"""
      select k.*, m.* from visit_conduct_kizai as k inner join visit_conduct as c 
        inner join visit as v 
        inner join tokuteikizai_master_arch as m 
        where k.id = ${conductKizaiId} and k.visit_conduct_id = c.id 
        and c.visit_id = v.visit_id 
        and k.kizaicode = m.kizaicode 
        and """ ++ validAt(frag("date(v.v_datetime)"), frag("m")))
      .query[(ConductKizai, KizaiMaster)]
      .unique
      .map({ case (d, m) =>
        ConductKizaiEx(d, m)
      })
    mysql(op)

