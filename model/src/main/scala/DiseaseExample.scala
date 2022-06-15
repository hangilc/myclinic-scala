package dev.myclinic.scala.model

import io.circe.generic.semiauto._
import io.circe.Encoder
import io.circe.Decoder

case class DiseaseExample(
  byoumei: Option[String] = None,
  preAdjList: List[String] = List.empty,
  postAdjList: List[String] = List.empty
):
  def label: String = 
    (preAdjList ++ byoumei.toList ++ postAdjList).mkString("")

object DiseaseExample:
  def fromMap(map: Map[String, String | List[String]]): DiseaseExample =
    def getString(key: String): Option[String] =
      map.get(key) match {
        case Some(s: String) => Some(s)
        case _ => None
      }
    def getStringList(key: String): List[String] =
      map.get(key) match {
        case Some(a: List[String]) => a
        case _ => List.empty
      }
    DiseaseExample(
      getString("byoumei"),
      getStringList("pre-adj-list"),
      getStringList("adj-list")
    )

  given Encoder[DiseaseExample] = deriveEncoder[DiseaseExample]
  given Decoder[DiseaseExample] = deriveDecoder[DiseaseExample]
