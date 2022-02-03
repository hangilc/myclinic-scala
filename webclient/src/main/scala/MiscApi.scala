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
import scala.scalajs.js.typedarray.ArrayBuffer

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

    def hotlineBeep(recipient: String): Future[Boolean] =
      get("hotline-beep", Params("recipient" -> recipient))

    def listTodaysHotline(): Future[List[(Int, HotlineCreated)]] = // (appEventId, HotlineCreated)
      get("list-todays-hotline", Params())

    def listWqueue(): Future[List[Wqueue]] =
      get("list-wqueue", Params())

    def listWqueueFull(): Future[(Int, List[Wqueue], Map[Int, Visit], Map[Int, Patient])] =
      get("list-wqueue-full", Params())

    def getVisitPatient(visitId: Int): Future[(Int, Visit, Patient)] =
      get("get-visit-patient", Params("visit-id" -> visitId))

    def listDrugForVisit(visitId: Int): Future[List[Drug]] =
      get("list-drug-for-visit", Params("visit-id" -> visitId))

    def listShinryouForVisit(visitId: Int): Future[List[Shinryou]] =
      get("list-shinryou-for-visit", Params("visit-id" -> visitId))

    def listConductForVisit(visitId: Int): Future[List[Conduct]] =
      get("list-conduct-for-visit", Params("visit-id" -> visitId))

    def listConductDrugForVisit(conductId: Int): Future[List[ConductDrug]] =
      get("list-conduct-drug-for-visit", Params("visit-id" -> conductId))

    def listConductShinryouForVisit(
        conductId: Int
    ): Future[List[ConductShinryou]] =
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

    def findAvailableShahokokuho(
        patientId: Int,
        at: LocalDate
    ): Future[Option[Shahokokuho]] =
      get(
        "find-available-shahokokuho",
        Params("patient-id" -> patientId, "at" -> at)
      )

    def findAvailableRoujin(
        patientId: Int,
        at: LocalDate
    ): Future[Option[Roujin]] =
      get(
        "find-available-roujin",
        Params("patient-id" -> patientId, "at" -> at)
      )

    def findAvailableKoukikourei(
        patientId: Int,
        at: LocalDate
    ): Future[Option[Koukikourei]] =
      get(
        "find-available-koukikourei",
        Params("patient-id" -> patientId, "at" -> at)
      )

    def listAvailableShahokokuho(
        patientId: Int,
        at: LocalDate
    ): Future[List[Shahokokuho]] =
      get(
        "list-available-shahokokuho",
        Params("patient-id" -> patientId, "at" -> at)
      )

    def listAvailableRoujin(
        patientId: Int,
        at: LocalDate
    ): Future[List[Roujin]] =
      get(
        "list-available-roujin",
        Params("patient-id" -> patientId, "at" -> at)
      )

    def listAvailableKoukikourei(
        patientId: Int,
        at: LocalDate
    ): Future[List[Koukikourei]] =
      get(
        "list-available-koukikourei",
        Params("patient-id" -> patientId, "at" -> at)
      )

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

    def enterRoujin(roujin: Roujin): Future[Boolean] =
      post("enter-roujin", Params(), roujin)

    def enterKoukikourei(koukikourei: Koukikourei): Future[Boolean] =
      post("enter-koukikourei", Params(), koukikourei)

    def enterKouhi(kouhi: Kouhi): Future[Boolean] =
      post("enter-kouhi", Params(), kouhi)

    def updateShahokokuho(shahokokuho: Shahokokuho): Future[Boolean] =
      post("update-shahokokuho", Params(), shahokokuho)

    def updateRoujin(roujin: Roujin): Future[Boolean] =
      post("update-roujin", Params(), roujin)

    def updateKoukikourei(koukikourei: Koukikourei): Future[Boolean] =
      post("update-koukikourei", Params(), koukikourei)

    def updateKouhi(kouhi: Kouhi): Future[Boolean] =
      post("update-kouhi", Params(), kouhi)

    def deleteShahokokuho(shahokokuhoId: Int): Future[Boolean] =
      get("delete-shahokokuho", Params("shahokokuho-id" -> shahokokuhoId))

    def deleteRoujin(roujinId: Int): Future[Boolean] =
      get("delete-roujin", Params("roujin-id" -> roujinId))

    def deleteKoukikourei(koukikoureiId: Int): Future[Boolean] =
      get("delete-koukikourei", Params("koukikourei-id" -> koukikoureiId))

    def deleteKouhi(kouhiId: Int): Future[Boolean] =
      get("delete-kouhi", Params("kouhi-id" -> kouhiId))

    def listRecentVisit(offset: Int, count: Int): Future[List[Visit]] =
      get("list-recent-visit", Params("offset" -> offset, "count" -> count))

    def listVisitByDate(at: LocalDate): Future[List[Visit]] =
      get("list-visit-by-date", Params("at" -> at))

    def countVisitByPatient(patientId: Int): Future[Int] =
      get("count-visit-by-patient", Params("patient-id" -> patientId))

    def listVisitByPatient(
        patientId: Int,
        offset: Int,
        count: Int
    ): Future[List[Visit]] =
      get(
        "list-visit-by-patient",
        Params("patient-id" -> patientId, "offset" -> offset, "count" -> count)
      )

    def listVisitIdByPatient(
        patientId: Int,
        offset: Int,
        count: Int
    ): Future[List[Int]] =
      get(
        "list-visit-id-by-patient",
        Params("patient-id" -> patientId, "offset" -> offset, "count" -> count)
      )

    def listVisitIdByPatientReverse(
        patientId: Int,
        offset: Int,
        count: Int
    ): Future[List[Int]] =
      get(
        "list-visit-id-by-patient-reverse",
        Params("patient-id" -> patientId, "offset" -> offset, "count" -> count)
      )

    def batchGetText(visitIds: List[Int]): Future[Map[Int, List[Text]]] =
      post("batch-get-text", Params(), visitIds)

    def batchGetVisitEx(visitIds: List[Int]): Future[List[VisitEx]] =
      post("batch-get-visit-ex", Params(), visitIds)

    def savePatientImage(
        patientId: Int,
        fileName: String,
        data: ArrayBuffer
    ): Future[Boolean] =
      postBinary(
        "save-patient-image",
        Params("patient-id" -> patientId, "file-name" -> fileName),
        data
      )

    def renamePatientImage(
        patientId: Int,
        src: String,
        dst: String
    ): Future[Boolean] =
      get(
        "rename-patient-image",
        Params("patient-id" -> patientId, "src" -> src, "dst" -> dst)
      )

    def deletePatientImage(patientId: Int, fileName: String): Future[Boolean] =
      get(
        "delete-patient-image",
        Params("patient-id" -> patientId, "file-name" -> fileName)
      )

    def listPatientImage(patientId: Int): Future[List[FileInfo]] =
      get("list-patient-image", Params("patient-id" -> patientId))

    def getCovid2ndShotData(patientId: Int): Future[Option[(Int, LocalDate, LocalDate)]] =
      get("get-covid-2nd-shot-data", Params("patient-id" -> patientId))
