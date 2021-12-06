package dev.myclinic.scala.webclient

import java.time.{LocalDate, LocalTime, LocalDateTime}
import scala.concurrent.Future
import dev.myclinic.scala.model._
import dev.myclinic.scala.clinicop.*
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions
import dev.fujiwara.scala.drawer.Op

object MiscApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def resolveClinicOperation(date: LocalDate): Future[ClinicOperation] =
      get("resolve-clinic-operation", Params("date" -> date))

    def batchResolveClinicOperations(
        dates: List[LocalDate]
    ): Future[Map[LocalDate, ClinicOperation]] =
      post("batch-resolve-clinic-operations", Params(), dates)

    def postHotline(hotline: Hotline): Future[Boolean] =
      post("post-hotline", Params(), hotline)

    def listTodaysHotline(): Future[List[HotlineCreated]] =
      get("list-todays-hotline", Params())

    def listWqueue(): Future[List[Wqueue]] =
      get("list-wqueue", Params())

    def listDrugForVisit(visitId: Int): Future[List[Drug]] =
      get("list-drug-for-visit", Params("visit-id" -> visitId))

    def listShinryouForVisit(visitId: Int): Future[List[Shinryou]] =
      get("list-shinryou-for-visit", Params("visit-id" -> visitId))

    def listConductForVisit(visitId: Int): Future[List[Conduct]] =
      get("list-conduct-for-visit", Params("visit-id" -> visitId))

    def listConductDrugForVisit(conductId: Int): Future[List[ConductDrug]] =
      get("list-conduct-drug-for-visit", Params("visit-id" -> conductId))

    def listConductShinryouForVisit(conductId: Int): Future[List[ConductShinryou]] =
      get("list-conduct-shinryou-for-visit", Params("visit-id" -> conductId))

    def listConductKizaiForVisit(conductId: Int): Future[List[ConductKizai]] =
      get("list-conduct-kizai-for-visit", Params("visit-id" -> conductId))

    def getMeisai(visitId: Int): Future[Meisai] =
      get("get-meisai", Params("visit-id" -> visitId))

    def finishCashier(payment: Payment): Future[Boolean] =
      post("finish-cashier", Params(), payment)

    def drawBlankReceipt(): Future[List[Op]] =
      get("draw-blank-receipt", Params())

    def drawReceipt(data: ReceiptDrawerData): Future[List[Op]] =
      post("draw-receipt", Params(), data)

    def startVisit(patientId: Int, at: LocalDateTime): Future[Boolean] =
      get("start-visit", Params("patient-id" -> patientId, "at" -> at))

    def findAvailableShahokokuho(patientId: Int, at: LocalDate): Future[Option[Shahokokuho]] =
      get("find-available-shahokokuho", Params("patient-id" -> patientId, "at" -> at))

    def findAvailableRoujin(patientId: Int, at: LocalDate): Future[Option[Roujin]] =
      get("find-available-roujin", Params("patient-id" -> patientId, "at" -> at))

    def findAvailableKoukikourei(patientId: Int, at: LocalDate): Future[Option[Koukikourei]] =
      get("find-available-koukikourei", Params("patient-id" -> patientId, "at" -> at))

    def listAvailableShahokokuho(patientId: Int, at: LocalDate): Future[List[Shahokokuho]] =
      get("list-available-shahokokuho", Params("patient-id" -> patientId, "at" -> at))

    def listAvailableRoujin(patientId: Int, at: LocalDate): Future[List[Roujin]] =
      get("list-available-roujin", Params("patient-id" -> patientId, "at" -> at))

    def listAvailableKoukikourei(patientId: Int, at: LocalDate): Future[List[Koukikourei]] =
      get("list-available-koukikourei", Params("patient-id" -> patientId, "at" -> at))

    def listAvailableKouhi(patientId: Int, at: LocalDate): Future[List[Kouhi]] =
      get("list-available-kouhi", Params("patient-id" -> patientId, "at" -> at))

    def listShahokokuho(patientId: Int): Future[List[Shahokokuho]] =
      get("list-shahokokuho", Params("patient-id" -> patientId))

    def listRoujin(patientId: Int): Future[List[Roujin]] =
      get("list-roujin", Params("patient-id" -> patientId))

    def listKoukikourei(patientId: Int): Future[List[Koukikourei]] =
      get("list-koukikourei", Params("patient-id" -> patientId))

    def listKouhi(patientId: Int): Future[List[Kouhi]] =
      get("list-kouhi", Params("patient-id" -> patientId))

    def enterShahokokuho(shahokokuho: Shahokokuho): Future[Boolean] =
      post("enter-shahokokuho", Params(), shahokokuho)

    def updateShahokokuho(shahokokuho: Shahokokuho): Future[Boolean] =
      post("update-shahokokuho", Params(), shahokokuho)
      