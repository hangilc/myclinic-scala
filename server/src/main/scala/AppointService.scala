package dev.myclinic.scala.server

import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import org.http4s.QueryParamDecoder._
import cats.effect.IO
import dev.myclinic.scala.db.Db
import java.time.{LocalDate, LocalTime}
import java.time.format.DateTimeFormatter
import io.circe.syntax._

object AppointService {
  implicit val dateDecoder: QueryParamDecoder[LocalDate] = 
    QueryParamDecoder[String].map(LocalDate.parse(_))

  private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  implicit val timeDecoder: QueryParamDecoder[LocalTime] = 
    QueryParamDecoder[String].map(LocalTime.parse(_))

  object DateParam extends QueryParamDecoderMatcher[LocalDate]("date")
  object FromDateParam extends QueryParamDecoderMatcher[LocalDate]("from")
  object UptoDateParam extends QueryParamDecoderMatcher[LocalDate]("upto")
  object TimeParam extends QueryParamDecoderMatcher[LocalTime]("time")

  val service = HttpRoutes.of[IO] {
    case GET -> Root / "get-appoint" :? DateParam(date) +& TimeParam(time)
      => Ok(Db.getAppoint(date, time).map(_.asJson))
    
    case GET -> Root / "list-appoint" :? FromDateParam(from) +& UptoDateParam(upto)
      => Ok(Db.listAppoint(from, upto).map(_.asJson))
  }
}