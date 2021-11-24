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
import dev.myclinic.scala.db.Db
import java.time.LocalDate
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.clinicop.ClinicOperation
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import dev.myclinic.java.{Config, HoukatsuKensa}
import dev.myclinic.scala.rcpt.RcptVisit

object MiscService extends DateTimeQueryParam with Publisher:
  object dateDate extends QueryParamDecoderMatcher[LocalDate]("date")
  object intVisitId extends QueryParamDecoderMatcher[Int]("visit-id")

  given houkatsuKensa: HoukatsuKensa = (new Config).getHoukatsuKensa

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case GET -> Root / "resolve-clinic-operation" :? dateDate(date) =>
      Ok(ClinicOperation.getClinicOperationAt(date))

    case req @ POST -> Root / "batch-resolve-clinic-operations" =>
      val op =
        for dates <- req.as[List[LocalDate]]
        yield {
          val items = dates.map(date =>
            (date, ClinicOperation.getClinicOperationAt(date))
          )
          Map(items: _*)
        }
      Ok(op)

    case GET -> Root / "get-meisai" :? intVisitId(visitId) =>
      Ok({
        for
          visit <- Db.getVisitEx(visitId)
        yield RcptVisit.getMeisai(visit)
      })

    case req @ POST -> Root / "finish-cashier" =>
      Ok {
        for 
          payment <- req.as[Payment]
          events <- Db.finishCashier(payment)
          _ <- publishAll(events)
        yield true
      }

    case req @ GET -> Root / "draw-receipt" =>
      val d = new dev.fujiwara.drawer.forms.receipt.ReceiptDrawerData()
      val c = new dev.fujiwara.drawer.forms.receipt.ReceiptDrawer(d)
      val m = dev.fujiwara.drawer.op.JsonCodec.createMapper()
      val s = m.writeValueAsString(c.getOps())
      Ok(s)
      // val s = m.writeValueAsBytes(c.getOps())
      // import org.http4s.headers.`Content-Type`
      // import org.http4s.MediaType
      // given EntityEncoder[IO, Array[Byte]] = org.http4s.EntityEncoder.byteArrayEncoder
      // Ok(s).map(r => r.withContentType(`Content-Type`(MediaType("application", "json"))))
  }
