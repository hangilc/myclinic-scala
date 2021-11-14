package dev.myclinic.scala.db

object Db extends Mysql with DbAppoint with DbEvent with DbPatient 
  with DbHotline with DbWqueue with DbVisit
