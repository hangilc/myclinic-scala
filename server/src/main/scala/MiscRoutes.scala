package dev.myclinic.scala.server

import cats.syntax.all._
import cats.effect._
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
import dev.fujiwara.kanjidate.DateUtil
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
import dev.myclinic.scala.drawerform.receipt.ReceiptDrawerData

object MiscService extends DateTimeQueryParam with Publisher:
  object dateDate extends QueryParamDecoderMatcher[LocalDate]("date")
  object dateAt extends QueryParamDecoderMatcher[LocalDate]("at")
  object dateEnd extends QueryParamDecoderMatcher[LocalDate]("end-date")
  object atDateTime extends QueryParamDecoderMatcher[LocalDateTime]("at")
  object intVisitId extends QueryParamDecoderMatcher[Int]("visit-id")
  object intOffset extends QueryParamDecoderMatcher[Int]("offset")
  object intCount extends QueryParamDecoderMatcher[Int]("count")
  object intPatientId extends QueryParamDecoderMatcher[Int]("patient-id")
  object intShahokokuhoId
      extends QueryParamDecoderMatcher[Int]("shahokokuho-id")
  object intRoujinId extends QueryParamDecoderMatcher[Int]("roujin-id")
  object intKoukikoureiId
      extends QueryParamDecoderMatcher[Int]("koukikourei-id")
  object intKouhiId extends QueryParamDecoderMatcher[Int]("kouhi-id")
  object intTextId extends QueryParamDecoderMatcher[Int]("text-id")
  object intDiseaseId extends QueryParamDecoderMatcher[Int]("disease-id")
  object intNVisits extends QueryParamDecoderMatcher[Int]("n-visits")
  object intConductShinryouId
      extends QueryParamDecoderMatcher[Int]("conduct-shinryou-id")
  object intConductDrugId
      extends QueryParamDecoderMatcher[Int]("conduct-drug-id")
  object intConductKizaiId
      extends QueryParamDecoderMatcher[Int]("conduct-kizai-id")
  object intConductId extends QueryParamDecoderMatcher[Int]("conduct-id")
  object intYear extends QueryParamDecoderMatcher[Int]("year")
  object intMonth extends QueryParamDecoderMatcher[Int]("month")
  object strText extends QueryParamDecoderMatcher[String]("text")
  object strEndReason extends QueryParamDecoderMatcher[String]("end-reason")

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
      val op =
        for
          result <- Db.startVisit(patientId, at)
          (visit, events) = result
          _ <- publishAll(events)
        yield visit
      Ok(op)

    case req @ POST -> Root / "start-visit-with-hoken" :? intPatientId(
          patientId
        ) +& atDateTime(
          at
        ) =>
      val op =
        for
          hokenIdSet <- req.as[HokenIdSet]
          kouhiIds = hokenIdSet.kouhiIds()
          result <- Db.startVisitWithHoken(
            patientId,
            at,
            hokenIdSet.shahokokuhoId,
            hokenIdSet.koukikoureiId,
            kouhiIds
          )
          (visit, events) = result
          _ <- publishAll(events)
        yield visit
      Ok(op)

    case GET -> Root / "find-available-shahokokuho" :? intPatientId(
          patientId
        ) +& dateAt(at) =>
      Ok(Db.findAvailableShahokokuho(patientId, at))

    case GET -> Root / "list-available-shahokokuho" :? intPatientId(
          patientId
        ) +& dateAt(at) =>
      Ok(Db.listAvailableShahokokuho(patientId, at))

    case GET -> Root / "find-available-roujin" :? intPatientId(
          patientId
        ) +& dateAt(at) =>
      Ok(Db.findAvailableRoujin(patientId, at))

    case GET -> Root / "list-available-roujin" :? intPatientId(
          patientId
        ) +& dateAt(at) =>
      Ok(Db.listAvailableRoujin(patientId, at))

    case GET -> Root / "find-available-koukikourei" :? intPatientId(
          patientId
        ) +& dateAt(at) =>
      Ok(Db.findAvailableKoukikourei(patientId, at))

    case GET -> Root / "list-available-koukikourei" :? intPatientId(
          patientId
        ) +& dateAt(at) =>
      Ok(Db.listAvailableKoukikourei(patientId, at))

    case GET -> Root / "list-available-kouhi" :? intPatientId(
          patientId
        ) +& dateAt(at) =>
      Ok(Db.listAvailableKouhi(patientId, at))

    case GET -> Root / "list-shahokokuho" :? intPatientId(patientId) =>
      Ok(Db.listShahokokuho(patientId))

    case GET -> Root / "list-roujin" :? intPatientId(patientId) =>
      Ok(Db.listRoujin(patientId))

    case GET -> Root / "list-koukikourei" :? intPatientId(patientId) =>
      Ok(Db.listKoukikourei(patientId))

    case GET -> Root / "list-kouhi" :? intPatientId(patientId) =>
      Ok(Db.listKouhi(patientId))

    case GET -> Root / "count-shahokokuho-usage" :? intShahokokuhoId(
          shahokokuhoId
        ) =>
      Ok(Db.countShahokokuhoUsage(shahokokuhoId))

    case GET -> Root / "count-koukikourei-usage" :? intKoukikoureiId(
          koukikoureiId
        ) =>
      Ok(Db.countKoukikoureiUsage(koukikoureiId))

    case GET -> Root / "count-roujin-usage" :? intRoujinId(roujinId) =>
      Ok(Db.countRoujinUsage(roujinId))

    case GET -> Root / "count-kouhi-usage" :? intKouhiId(kouhiId) =>
      Ok(Db.countKouhiUsage(kouhiId))

    case req @ POST -> Root / "batch-count-hoken-usage" =>
      val op =
        for
          tuple <- req.as[(List[Int], List[Int], List[Int], List[Int])]
          maps <- Db.batchCountHokenUsage.tupled(tuple)
        yield maps
      Ok(op)

    case GET -> Root / "get-shahokokuho" :? intShahokokuhoId(shahokokuhoId) =>
      Ok(Db.getShahokokuho(shahokokuhoId))

    case GET -> Root / "get-koukikourei" :? intKoukikoureiId(koukikoureiId) =>
      Ok(Db.getKoukikourei(koukikoureiId))

    case GET -> Root / "get-roujin" :? intRoujinId(roujinId) =>
      Ok(Db.getRoujin(roujinId))

    case GET -> Root / "get-kouhi" :? intKouhiId(kouhiId) =>
      Ok(Db.getKouhi(kouhiId))

    case req @ POST -> Root / "enter-shahokokuho" =>
      Ok(
        for
          shahokokuho <- req.as[Shahokokuho]
          result <- Db.enterShahokokuho(shahokokuho)
          (entered, event) = result
          _ <- publish(event)
        yield entered
      )

    case req @ POST -> Root / "update-shahokokuho" =>
      Ok(
        for
          shahokokuho <- req.as[Shahokokuho]
          event <- Db.updateShahokokuho(shahokokuho)
          _ <- publish(event)
        yield event.appEventId
      )

    case GET -> Root / "delete-shahokokuho" :? intShahokokuhoId(
          shahokokuhoId
        ) =>
      Ok(
        for
          event <- Db.deleteShahokokuho(shahokokuhoId)
          _ <- publish(event)
        yield true
      )

    case req @ POST -> Root / "enter-roujin" =>
      Ok(
        for
          roujin <- req.as[Roujin]
          event <- Db.enterRoujin(roujin)
          _ <- publish(event)
        yield true
      )

    case req @ POST -> Root / "update-roujin" =>
      Ok(
        for
          roujin <- req.as[Roujin]
          event <- Db.updateRoujin(roujin)
          _ <- publish(event)
        yield true
      )

    case GET -> Root / "delete-roujin" :? intRoujinId(roujinId) =>
      Ok(
        for
          event <- Db.deleteRoujin(roujinId)
          _ <- publish(event)
        yield true
      )

    case req @ POST -> Root / "enter-koukikourei" =>
      Ok(
        for
          koukikourei <- req.as[Koukikourei]
          result <- Db.enterKoukikourei(koukikourei)
          (entered, event) = result
          _ <- publish(event)
        yield entered
      )

    case req @ POST -> Root / "update-koukikourei" =>
      Ok(
        for
          koukikourei <- req.as[Koukikourei]
          event <- Db.updateKoukikourei(koukikourei)
          _ <- publish(event)
        yield true
      )

    case GET -> Root / "delete-koukikourei" :? intKoukikoureiId(
          koukikoureiId
        ) =>
      Ok(
        for
          event <- Db.deleteKoukikourei(koukikoureiId)
          _ <- publish(event)
        yield true
      )

    case req @ POST -> Root / "enter-kouhi" =>
      Ok(
        for
          kouhi <- req.as[Kouhi]
          result <- Db.enterKouhi(kouhi)
          (entered, event) = result
          _ <- publish(event)
        yield entered
      )

    case req @ POST -> Root / "update-kouhi" =>
      Ok(
        for
          kouhi <- req.as[Kouhi]
          event <- Db.updateKouhi(kouhi)
          _ <- publish(event)
        yield true
      )

    case GET -> Root / "delete-kouhi" :? intKouhiId(kouhiId) =>
      Ok(
        for
          event <- Db.deleteKouhi(kouhiId)
          _ <- publish(event)
        yield true
      )

    case GET -> Root / "list-recent-visit" :? intOffset(offset) +& intCount(
          count
        ) =>
      Ok(
        Db.listRecentVisit(offset, count)
      )

    case GET -> Root / "list-recent-visit-full" :? intOffset(
          offset
        ) +& intCount(
          count
        ) =>
      Ok(
        Db.listRecentVisitFull(offset, count)
      )

    case GET -> Root / "list-visit-by-date" :? dateAt(at) =>
      Ok(Db.listVisitByDate(at))

    case GET -> Root / "count-visit-by-patient" :? intPatientId(patientId) =>
      Ok(Db.countVisitByPatient(patientId))

    case GET -> Root / "list-visit-by-patient" :? intPatientId(
          patientId
        ) +& intOffset(offset) +& intCount(count) =>
      Ok(Db.listVisitByPatient(patientId, offset, count))

    case GET -> Root / "list-visit-by-patient-reverse" :? intPatientId(
          patientId
        ) +& intOffset(offset) +& intCount(count) =>
      Ok(Db.listVisitByPatientReverse(patientId, offset, count))

    case GET -> Root / "list-visit-id-by-patient" :? intPatientId(
          patientId
        ) +& intOffset(offset) +& intCount(count) =>
      Ok(Db.listVisitIdByPatient(patientId, offset, count))

    case GET -> Root / "list-visit-id-by-patient-reverse" :? intPatientId(
          patientId
        ) +& intOffset(offset) +& intCount(count) =>
      Ok(Db.listVisitIdByPatientReverse(patientId, offset, count))

    case req @ POST -> Root / "batch-get-text" =>
      Ok(for
        visitIds <- req.as[List[Int]]
        map <- Db.batchGetText(visitIds)
      yield map)

    case req @ POST -> Root / "batch-get-visit-ex" =>
      Ok(for
        visitIds <- req.as[List[Int]]
        result <- Db.batchGetVisitEx(visitIds)
      yield result)

    case GET -> Root / "get-text" :? intTextId(textId) =>
      val op = Db.getText(textId)
      Ok(op)

    case req @ POST -> Root / "enter-text" =>
      val op =
        for
          text <- req.as[Text]
          result <- Db.enterText(text)
          (entered, event) = result
          _ <- publish(event)
        yield entered
      Ok(op)

    case req @ POST -> Root / "update-text" =>
      val op =
        for
          text <- req.as[Text]
          event <- Db.updateText(text)
          _ <- publish(event)
        yield true
      Ok(op)

    case GET -> Root / "delete-text" :? intTextId(textId) =>
      val op =
        for
          event <- Db.deleteText(textId)
          _ <- publish(event)
        yield true
      Ok(op)

    case req @ POST -> Root / "enter-payment" =>
      val op =
        for
          payment <- req.as[Payment]
          event <- Db.enterPayment(payment)
          _ <- publish(event)
        yield true
      Ok(op)

    case GET -> Root / "list-current-disease-ex" :? intPatientId(patientId) =>
      Ok(Db.listCurrentDiseaseEx(patientId))

    case GET -> Root / "list-disease-ex" :? intPatientId(patientId) =>
      Ok(Db.listDiseaseEx(patientId))

    case GET -> Root / "search-byoumei-master" :? strText(text) +& dateAt(at) =>
      Ok(Db.searchByoumeiMaster(text, at))

    case GET -> Root / "search-shuushokugo-master" :? strText(text) +& dateAt(
          at
        ) =>
      Ok(Db.searchShuushokugoMaster(text, at))

    case req @ POST -> Root / "enter-disease-ex" =>
      val op = for
        data <- req.as[DiseaseEnterData]
        result <- Db.enterDiseaseEx(
          data.patientId,
          data.byoumeicode,
          data.startDate,
          data.adjCodes
        )
        (diseaseId, events) = result
        _ <- publishAll(events)
      yield diseaseId
      Ok(op)

    case GET -> Root / "get-disease-ex" :? intDiseaseId(diseaseId) =>
      Ok(Db.getDiseaseEx(diseaseId))

    case GET -> Root / "end-disease" :? intDiseaseId(diseaseId) +& dateEnd(
          endDate
        ) +& strEndReason(endReason) =>
      val op =
        for
          event <- Db.endDisease(diseaseId, endDate, endReason)
          _ <- publish(event)
        yield true
      Ok(op)

    case req @ POST -> Root / "update-disease-ex" =>
      val op =
        for
          body <- req.as[(Disease, List[Int])]
          (disease, shuushokugocodes) = body
          events <- Db.updateDiseaseEx(disease, shuushokugocodes)
          _ <- publishAll(events)
        yield true
      Ok(op)

    case GET -> Root / "delete-disease-ex" :? intDiseaseId(diseaseId) =>
      val op =
        for
          events <- Db.deleteDiseaseEx(diseaseId)
          _ <- publishAll(events)
        yield true
      Ok(op)

    case req @ POST -> Root / "batch-get-charge-payment" =>
      val op =
        for
          visitIds <- req.as[List[Int]]
          map <- Db.batchGetChargePayment(visitIds)
        yield map
      Ok(op)

    case GET -> Root / "list-visit-since" :? intPatientId(
          patientId
        ) +& dateDate(date) =>
      Ok(Db.listVisitSince(patientId, date))

    case req @ POST -> Root / "enter-wqueue" =>
      val op =
        for
          wq <- req.as[Wqueue]
          event <- Db.enterWqueue(wq)
          _ <- publish(event)
        yield true
      Ok(op)

    case GET -> Root / "get-charge" :? intVisitId(visitId) =>
      Ok(Db.getCharge(visitId))

    case GET -> Root / "list-mishuu-for-patient" :? intPatientId(
          patientId
        ) +& intNVisits(nVisits) =>
      Ok(Db.listMishuuForPatient(patientId, nVisits))

    case req @ POST -> Root / "enter-conduct-shinryou" =>
      val op =
        for
          cs <- req.as[ConductShinryou]
          result <- Db.enterConductShinryou(cs)
          (event, entered) = result
          _ <- publish(event)
        yield entered
      Ok(op)

    case req @ POST -> Root / "enter-conduct-drug" =>
      val op =
        for
          cs <- req.as[ConductDrug]
          result <- Db.enterConductDrug(cs)
          (event, entered) = result
          _ <- publish(event)
        yield entered
      Ok(op)

    case req @ POST -> Root / "enter-conduct-kizai" =>
      val op =
        for
          cs <- req.as[ConductKizai]
          result <- Db.enterConductKizai(cs)
          (event, entered) = result
          _ <- publish(event)
        yield entered
      Ok(op)

    case GET -> Root / "get-conduct-shinryou-ex" :? intConductShinryouId(
          conductShinryouId
        ) =>
      Ok(Db.getConductShinryouEx(conductShinryouId))

    case GET -> Root / "get-conduct-drug-ex" :? intConductDrugId(
          conductDrugId
        ) =>
      Ok(Db.getConductDrugEx(conductDrugId))

    case GET -> Root / "get-conduct-kizai-ex" :? intConductKizaiId(
          conductKizaiId
        ) =>
      Ok(Db.getConductKizaiEx(conductKizaiId))

    case GET -> Root / "get-conduct" :? intConductId(conductId) =>
      Ok(Db.getConduct(conductId))

    case GET -> Root / "delete-conduct-shinryou" :? intConductShinryouId(
          conductShinryouId
        ) =>
      val op =
        for
          event <- Db.deleteConductShinryou(conductShinryouId)
          _ <- publish(event)
        yield true
      Ok(op)

    case GET -> Root / "delete-conduct-drug" :? intConductDrugId(
          conductDrugId
        ) =>
      val op =
        for
          event <- Db.deleteConductDrug(conductDrugId)
          _ <- publish(event)
        yield true
      Ok(op)

    case GET -> Root / "delete-conduct-kizai" :? intConductKizaiId(
          conductKizaiId
        ) =>
      val op =
        for
          event <- Db.deleteConductKizai(conductKizaiId)
          _ <- publish(event)
        yield true
      Ok(op)

    case req @ POST -> Root / "update-conduct" =>
      val op =
        for
          conduct <- req.as[Conduct]
          event <- Db.updateConduct(conduct)
          _ <- publish(event)
        yield true
      Ok(op)

    case req @ POST -> Root / "set-gazou-label" =>
      val op =
        for
          gl <- req.as[GazouLabel]
          event <- Db.setGazouLabel(gl.conductId, gl.label)
          _ <- publish(event)
        yield true
      Ok(op)

    case GET -> Root / "search-presc-example-full" :? strText(text) =>
      Ok(Db.searchPrescExampleFull(text))

    case GET -> Root / "get-onshi" :? intVisitId(visitId) =>
      Ok(Db.getOnshi(visitId))

    case GET -> Root / "find-onshi" :? intVisitId(visitId) =>
      Ok(Db.findOnshi(visitId))

    case req @ POST -> Root / "set-onshi" =>
      val op =
        for
          onshi <- req.as[Onshi]
          events <- Db.setOnshi(onshi)
          _ <- publishAll(events)
        yield true
      Ok(op)

    case GET -> Root / "clear-onshi" :? intVisitId(visitId) =>
      val op =
        for
          event <- Db.clearOnshi(visitId)
          _ <- event match {
            case Some(e) => publish(e)
            case None    => ().pure[IO]
          }
        yield true
      Ok(op)

    case req @ POST -> Root / "batch-probe-onshi" =>
      val op =
        for
          visitIds <- req.as[List[Int]]
          checked <- Db.batchProbeOnshi(visitIds)
        yield checked
      Ok(op)

    case req @ POST -> Root / "new-shahokokuho" =>
      val op =
        for
          shahokokuho <- req.as[Shahokokuho]
          result <- Db.newShahokokuho(shahokokuho)
          (entered, events) = result
          _ <- publishAll(events)
        yield entered
      Ok(op)

    case req @ POST -> Root / "new-koukikourei" =>
      val op =
        for
          koukikourei <- req.as[Koukikourei]
          result <- Db.newKoukikourei(koukikourei)
          (entered, events) = result
          _ <- publishAll(events)
        yield entered
      Ok(op)

    case GET -> Root / "shahokokuho-usage-since" :? intShahokokuhoId(
          shahokokuhoId
        ) +& dateDate(date) =>
      Ok(Db.shahokokuhoUsageSince(shahokokuhoId, date))

    case GET -> Root / "shahokokuho-usage" :? intShahokokuhoId(
          shahokokuhoId
        ) =>
      Ok(Db.shahokokuhoUsage(shahokokuhoId))

    case GET -> Root / "koukikourei-usage-since" :? intKoukikoureiId(
          koukikoureiId
        ) +& dateDate(date) =>
      Ok(Db.koukikoureiUsageSince(koukikoureiId, date))

    case GET -> Root / "koukikourei-usage" :? intKoukikoureiId(
          koukikoureiId
        ) =>
      Ok(Db.koukikoureiUsage(koukikoureiId))

    case GET -> Root / "kouhi-usage-since" :? intKouhiId(
          kouhiId
        ) +& dateDate(date) =>
      Ok(Db.kouhiUsageSince(kouhiId, date))

    case GET -> Root / "kouhi-usage" :? intKouhiId(
          kouhiId
        ) =>
      Ok(Db.kouhiUsage(kouhiId))

    case GET -> Root / "list-visit-id-by-patient-and-month" :? intPatientId(
          patientId
        )
        +& intYear(year) +& intMonth(month) =>
      Ok(Db.listVisitIdByPatientAndMonth(patientId, year, month))

    case req @ POST -> Root / "batch-enter-or-update-hoken" =>
      val op =
        for
          lists <- req.as[HokenLists]
          r <- Db.batchEnterOrUpdateHoken(lists.shahokokuhoList, lists.koukikoureiList)
          s = r._1
          k = r._2
          e = r._3
          _ <- publishAll(e)
        yield (s, k)
      Ok(op)

  }
