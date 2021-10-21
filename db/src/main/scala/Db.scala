package dev.myclinic.scala.db

object Db extends Sqlite with DbAppoint with DbEvent with DbPatient {}
