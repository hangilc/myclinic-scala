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
    mysql(DbVisitExPrim.getDrugEx(drugId).unique)

  def getShinryouEx(shinryouId: Int): IO[ShinryouEx] =
    mysql(DbVisitExPrim.getShinryouEx(shinryouId).unique)

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
    
  def getVisitEx(visitId: Int): IO[VisitEx] =
    mysql(DbVisitExPrim.getVisitEx(visitId))
