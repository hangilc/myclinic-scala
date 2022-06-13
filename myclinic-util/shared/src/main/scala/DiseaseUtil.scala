package dev.myclinic.scala.myclinicutil

import dev.myclinic.scala.model.*

object DiseaseUtil:
  type DiseaseFull =
    (Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])

  def diseaseNameOf(d: DiseaseFull): String =
    d match {
      case (disease, bMaster, adjList) =>
        val (pre: List[String], post: List[String]) =
          adjList.foldLeft((List.empty[String], List.empty[String])) {
            case ((pre, post), (_, master)) =>
              if master.isPrefix then (pre :+ master.name, post)
              else (pre, post :+ master.name)
          }
        ((pre :+ bMaster.name) ++ post).mkString("")
    }
