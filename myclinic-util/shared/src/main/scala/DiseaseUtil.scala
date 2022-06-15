package dev.myclinic.scala.myclinicutil

import dev.myclinic.scala.model.*

object DiseaseUtil:
  type DiseaseFull =
    (Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])

  def diseaseNameOf(d: DiseaseFull): String =
    d match {
      case (disease, bMaster, adjList) =>
        diseaseNameOf(bMaster, adjList.map(_._2))
    }

  def diseaseNameOf(
      b: ByoumeiMaster,
      adjList: List[ShuushokugoMaster]
  ): String =
    diseaseNameOf(Some(b), adjList)

  def diseaseNameOf(bOpt: Option[ByoumeiMaster], adjList: List[ShuushokugoMaster]): String =
    val (pre: List[String], post: List[String]) =
      adjList.foldLeft((List.empty[String], List.empty[String])) {
        case ((pre, post), master) =>
          if master.isPrefix then (pre :+ master.name, post)
          else (pre, post :+ master.name)
      }
    ((pre :+ bOpt.map(_.name).getOrElse("")) ++ post).mkString("")

