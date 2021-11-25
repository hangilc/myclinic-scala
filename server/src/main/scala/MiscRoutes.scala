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
import dev.myclinic.java.{Config => ConfigJava, HoukatsuKensa}
import dev.myclinic.scala.rcpt.RcptVisit
import dev.myclinic.scala.config.Config

object MiscService extends DateTimeQueryParam with Publisher:
  object dateDate extends QueryParamDecoderMatcher[LocalDate]("date")
  object intVisitId extends QueryParamDecoderMatcher[Int]("visit-id")

  given houkatsuKensa: HoukatsuKensa = (new ConfigJava).getHoukatsuKensa

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
        for visit <- Db.getVisitEx(visitId)
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

    case GET -> Root / "draw-blank-receipt" =>
      Ok {
        val d = new dev.fujiwara.drawer.forms.receipt.ReceiptDrawerData()
        val clinicInfo = Config.getClinicInfo
        d.setClinicName(clinicInfo.name)
          d.setAddressLines(
            Array(
              clinicInfo.postalCode,
              clinicInfo.address,
              clinicInfo.tel,
              clinicInfo.fax,
              clinicInfo.homepage
            )
          )
        val compiler = new dev.fujiwara.drawer.forms.receipt.ReceiptDrawer(d)
        val mapper = dev.fujiwara.drawer.op.JsonCodec.createMapper()
        mapper.writeValueAsString(compiler.getOps())
      }

    case req @ POST -> Root / "draw-receipt" =>
      Ok {
        for data <- req.as[ReceiptDrawerData]
        yield {
          val d = new dev.fujiwara.drawer.forms.receipt.ReceiptDrawerData()
          val clinicInfo = Config.getClinicInfo
          d.setClinicName(clinicInfo.name)
          d.setAddressLines(
            Array(
              clinicInfo.postalCode,
              clinicInfo.address,
              clinicInfo.tel,
              clinicInfo.fax,
              clinicInfo.homepage
            )
          )
          d.setPatientName(data.patientName)
          d.setPatientId(data.patientId)
          d.setChargeByInt(data.charge)
          d.setVisitDate(data.visitDate)
          d.setIssueDate(data.issueDate)
          d.setHoken(data.hoken)
          d.setFutanWari(data.futanWari)
          d.setShoshin(data.shoshin)
          d.setKanri(data.kanri)
          d.setZaitaku(data.zaitaku)
          d.setKensa(data.kensa)
          d.setGazou(data.gazou)
          d.setTouyaku(data.touyaku)
          d.setChuusha(data.chuusha)
          d.setShochi(data.shochi)
          d.setSonota(data.sonota)
          d.setSouten(data.souten)
          val compiler = new dev.fujiwara.drawer.forms.receipt.ReceiptDrawer(d)
          val mapper = dev.fujiwara.drawer.op.JsonCodec.createMapper()
          mapper.writeValueAsString(compiler.getOps())
        }
      }
  }
