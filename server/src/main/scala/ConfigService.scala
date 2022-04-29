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

  def routes = HttpRoutes.of[IO] {
    case GET -> Root / "get-clinic-info" => Ok(clinicInfo)
  }
