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

object DbHokenPrim:
  def getHokenInfo(visit: Visit): ConnectionIO[HokenInfo] =
    for
      shahokokuho <- DbVisitExPrim.optShahokokuho(visit.shahokokuhoId)
      roujin <- DbVisitExPrim.optRoujin(visit.roujinId)
      koukikourei <- DbVisitExPrim.optKoukikourei(visit.koukikoureiId)
      kouhiList <- DbVisitExPrim.kouhiList(visit.kouhiIds)
    yield HokenInfo(shahokokuho, roujin, koukikourei, kouhiList)

  def getHokenInfo(visitId: Int): ConnectionIO[HokenInfo] =
    for
      visit <- DbVisitPrim.getVisit(visitId).unique
      hokenInfo <- getHokenInfo(visit)
    yield hokenInfo
