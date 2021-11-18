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
  def getShinryouEx(shinryouId: Int): IO[ShinryouEx] =
    val op = sql"""
      select s.*, m.* from visit_shinryou as s 
        inner join visit as v inner join shinryoukoui_master_arch as m 
        where s.shinryou_id = ${shinryouId} and s.visit_id = v.visit_id 
        and s.shinryoucode = m.shinryoucode 
        and v.v_datetime >= m.valid_from and (
          v.v_datetime <= m.valid_upto || m.valid_upto = '0000-00-00'
        )
    """.query[(Shinryou, ShinryouMaster)].unique.map({
      case (s, m) => ShinryouEx(s, m)
    })
    mysql(op)