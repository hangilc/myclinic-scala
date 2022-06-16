package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}

case class Modify(disease: Disease, byoumeiMaster: ByoumeiMaster, adjList: List[(DiseaseAdj, ShuushokugoMaster)]):
  val ele = div(
    
  )

