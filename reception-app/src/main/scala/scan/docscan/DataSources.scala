package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.LocalDataSource
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.ScannerDevice

class DataSources:
  val patient = new LocalDataSource[Option[Patient]](None)
  val docType = new LocalDataSource[Option[String]](None)
  val scanner = new LocalDataSource[Option[ScannerDevice]](None)
  val resolution = new LocalDataSource[Int](100)
  val scannedDoc =  new LocalDataSource[String]("")

