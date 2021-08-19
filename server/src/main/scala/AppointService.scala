package dev.myclinic.scala.server

import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import cats.effect.IO
import dev.myclinic.scala.db.Db
import java.time.{LocalDate, LocalTime}
import io.circe.syntax._

object AppointService {
  val service = HttpRoutes.of[IO] {
    case GET -> Root / "api" / "get-appoint" =>
      Ok(Db.listAppoint(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 8, 30))
        .map(_.asJson))
  }
}