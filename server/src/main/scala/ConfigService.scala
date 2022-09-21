package dev.myclinic.scala.server

import cats.effect._
import cats.syntax.all._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import io.circe._
import io.circe.syntax._
  val clinicInfo = Config.getClinicInfo
import dev.myclinic.scala.config.Config
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}

object ConfigService:
  val clinicInfo = Config.getClinicInfo
  var shohouSamples: List[String] = Config.getShohouSamples
  lazy val shinryouRegular: Map[String, List[String]] = Config.getShinryouRegular
  lazy val shinryouKensa: Map[String, List[String]] = Config.getShinryouKensa
  lazy val masterNameMap = Config.getMasterNameMap
  lazy val masterTransition = Config.getMasterTransition
  object stringText extends QueryParamDecoderMatcher[String]("text")

  def routes = HttpRoutes.of[IO] {
    case GET -> Root / "get-clinic-info" => Ok(clinicInfo)
    case GET -> Root / "reload-shohou-samples" => 
      shohouSamples = Config.getShohouSamples
      Ok(true)
    case GET -> Root / "search-shohou-sample" :? stringText(text) =>
      Ok(shohouSamples.filter(_.contains(text)))
    case GET -> Root / "get-shinryou-regular" => Ok(shinryouRegular)
    case GET -> Root / "get-shinryou-kensa" => Ok(shinryouKensa)
    case GET -> Root / "list-disease-example" => Ok(Config.getDiseaseExample)
    case GET -> Root / "default-koukikourei-hokensha-bangou" => Ok(Config.defaultKoukikoureiHokenshaBangou)
    case GET -> Root / "get-phonebook" => Ok(Config.phonebook)

  }


  