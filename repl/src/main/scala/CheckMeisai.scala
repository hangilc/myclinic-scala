package repl

import cats.effect.*
import cats.syntax.all.*
import org.http4s.client.*
import org.http4s.blaze.client.*
import cats.effect.unsafe.implicits.global
import org.http4s.circe.CirceEntityDecoder.*
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import io.circe.Json

object CheckMeisai:
  def check(): Unit =
    BlazeClientBuilder[IO].resource
      .use { client =>
        for
          lastVisitId <- client.expect[Int](
            "http://localhost:8080/api/get-last-visit-id"
          )
          _ <- (1 to lastVisitId).toList
            .map(visitId =>
              for
                visitOpt <- findVisit(client, visitId)
                meisaiOpt <- visitOpt.map(visit => getMeisai(client, visit.visitId)).sequence
                origMeisaiOpt <- visitOpt.map(visit => getOrigMeisai(client, visit.visitId)).sequence
              yield 
                (visitOpt, meisaiOpt, origMeisaiOpt).tupled.foreach {
                  (visit, meisai, origTotalTen) => 
                    val visitId = visit.visitId
                    val totalTen = meisai.totalTen
                    if totalTen != origTotalTen then
                      println((visit.patientId, visitId, visit.visitedAt, totalTen, origTotalTen))
                      throw new RuntimeException("different total ten")
                }
            )
            .sequence_
        yield print(lastVisitId)
      }
      .unsafeRunSync()

  def api(service: String): String =
    s"http://localhost:8080/api/${service}"

  def origApi(service: String): String =
    s"http://localhost:28080/json/${service}"

  def findVisit(client: Client[IO], visitId: Int): IO[Option[Visit]] =
    client.expect[Option[Visit]](api(s"find-visit?visit-id=${visitId}"))

  def getMeisai(client: Client[IO], visitId: Int): IO[Meisai] =
    client.expect[Meisai](api(s"get-meisai?visit-id=${visitId}"))

  def getOrigMeisai(client: Client[IO], visitId: Int): IO[Int] =
    client.expect[Map[String, Json]](origApi(s"get-visit-meisai?visit-id=${visitId}"))
      .map(m => m("totalTen").as[Int].getOrElse(throw new RuntimeException("cannot get int")))
