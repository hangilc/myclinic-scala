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
import java.time.{LocalDate, LocalDateTime}
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.clinicop.ClinicOperation
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import org.http4s.headers.`Content-Type`
import org.http4s.MediaType
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import dev.myclinic.java.{Config => ConfigJava, HoukatsuKensa}
import dev.myclinic.scala.rcpt.RcptVisit
import dev.myclinic.scala.config.Config

object MiscService extends DateTimeQueryParam with Publisher:
  object dateDate extends QueryParamDecoderMatcher[LocalDate]("date")
  object atDate extends QueryParamDecoderMatcher[LocalDate]("at")
  object atDateTime extends QueryParamDecoderMatcher[LocalDateTime]("at")
  object intVisitId extends QueryParamDecoderMatcher[Int]("visit-id")
  object intPatientId extends QueryParamDecoderMatcher[Int]("patient-id")
  object intShahokokuhoId extends QueryParamDecoderMatcher[Int]("shahokokuho-id")
  object intRoujinId extends QueryParamDecoderMatcher[Int]("roujin-id")
  object intKoukikoureiId extends QueryParamDecoderMatcher[Int]("koukikourei-id")
  object intKouhiId extends QueryParamDecoderMatcher[Int]("kouhi-id")

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
      given EntityEncoder[IO, Array[Byte]] = EntityEncoder.byteArrayEncoder[IO]
      val resp = {
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
        mapper.writeValueAsBytes(compiler.getOps())
      }
      Ok(resp).map(r =>
        r.withContentType(`Content-Type`(new MediaType("application", "json")))
      )

    case req @ POST -> Root / "draw-receipt" =>
      given EntityEncoder[IO, Array[Byte]] = EntityEncoder.byteArrayEncoder[IO]
      val resp = {
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
          mapper.writeValueAsBytes(compiler.getOps())
        }
      }
      Ok(resp).map(r =>
        r.withContentType(`Content-Type`(new MediaType("application", "json")))
      )

    case GET -> Root / "start-visit" :? intPatientId(patientId) +& atDateTime(
          at
        ) =>
      Ok(
        for
          events <- Db.startVisit(patientId, at)
          _ <- publishAll(events)
        yield true
      )

    case GET -> Root / "find-available-shahokokuho" :? intPatientId(
          patientId
        ) +& atDate(at) =>
      Ok(Db.findAvailableShahokokuho(patientId, at))

    case GET -> Root / "list-available-shahokokuho" :? intPatientId(
          patientId
        ) +& atDate(at) =>
      Ok(Db.listAvailableShahokokuho(patientId, at))

    case GET -> Root / "find-available-roujin" :? intPatientId(
          patientId
        ) +& atDate(at) =>
      Ok(Db.findAvailableRoujin(patientId, at))

    case GET -> Root / "list-available-roujin" :? intPatientId(
          patientId
        ) +& atDate(at) =>
      Ok(Db.listAvailableRoujin(patientId, at))

    case GET -> Root / "find-available-koukikourei" :? intPatientId(
          patientId
        ) +& atDate(at) =>
      Ok(Db.findAvailableKoukikourei(patientId, at))

    case GET -> Root / "list-available-koukikourei" :? intPatientId(
          patientId
        ) +& atDate(at) =>
      Ok(Db.listAvailableKoukikourei(patientId, at))

    case GET -> Root / "list-available-kouhi" :? intPatientId(
          patientId
        ) +& atDate(at) =>
      Ok(Db.listAvailableKouhi(patientId, at))

    case GET -> Root / "list-shahokokuho" :? intPatientId(patientId) =>
      Ok(Db.listShahokokuho(patientId))

    case GET -> Root / "list-roujin" :? intPatientId(patientId) =>
      Ok(Db.listRoujin(patientId))

    case GET -> Root / "list-koukikourei" :? intPatientId(patientId) =>
      Ok(Db.listKoukikourei(patientId))

    case GET -> Root / "list-kouhi" :? intPatientId(patientId) =>
      Ok(Db.listKouhi(patientId))

    case req @ POST -> Root / "enter-shahokokuho" =>
      Ok(
        for
          shahokokuho <- req.as[Shahokokuho]
          event <- Db.enterShahokokuho(shahokokuho)
          _ <- publish(event)
        yield true
      )

    case req @ POST -> Root / "update-shahokokuho" =>
      Ok(
        for
          shahokokuho <- req.as[Shahokokuho]
          event <- Db.updateShahokokuho(shahokokuho)
          _ <- publish(event)
        yield true
      )

    case GET -> Root / "delete-shahokokuho" :? intShahokokuhoId(shahokokuhoId) =>
      Ok(
        for 
          event <- Db.deleteShahokokuho(shahokokuhoId)
          _ <- publish(event)
        yield true
      )

  }
