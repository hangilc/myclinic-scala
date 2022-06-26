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
import dev.myclinic.scala.drawerform.receipt.ReceiptDrawerData

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

    def listTodaysHotline()
        : Future[List[AppEvent]] = // (appEventId, HotlineCreated)
      get("list-todays-hotline", Params())

    def listWqueue(): Future[List[Wqueue]] =
      get("list-wqueue", Params())

    def listWqueueFull()
        : Future[(Int, List[Wqueue], Map[Int, Visit], Map[Int, Patient])] =
      get("list-wqueue-full", Params())

    def findWqueueFull(
        visitId: Int
    ): Future[Option[(Int, Wqueue, Visit, Patient)]] =
      get("find-wqueue-full", Params("visit-id" -> visitId))

    def findWqueue(visitId: Int): Future[Option[Wqueue]] =
      get("find-wqueue", Params("visit-id" -> visitId))

    def getWqueue(visitId: Int): Future[Wqueue] =
      get("get-wqueue", Params("visit-id" -> visitId))

    def updateWqueue(wq: Wqueue): Future[Boolean] =
      post("update-wqueue", Params(), wq)

    def changeWqueueState(visitId: Int, newState: WaitState): Future[Wqueue] =
      get(
        "change-wqueue-state",
        Params("visit-id" -> visitId, "wqueue-state" -> newState.code)
      )

    def getVisitPatient(visitId: Int): Future[(Int, Visit, Patient)] =
      get("get-visit-patient", Params("visit-id" -> visitId))

    def getPatientHoken(patientId: Int, at: LocalDate): Future[
      (
          Int,
          Patient,
          List[Shahokokuho],
          List[Koukikourei],
          List[Roujin],
          List[Kouhi]
      )
    ] =
      get("get-patient-hoken", Params("patient-id" -> patientId, "at" -> at))

    def getPatientAllHoken(patientId: Int): Future[
      (
          Int,
          Patient,
          List[Shahokokuho],
          List[Koukikourei],
          List[Roujin],
          List[Kouhi]
      )
    ] =
      get("get-patient-all-hoken", Params("patient-id" -> patientId))

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

    def startVisit(patientId: Int, at: LocalDateTime): Future[Visit] =
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

    def updateShahokokuho(shahokokuho: Shahokokuho): Future[Int] =
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

    def listRecentVisitFull(
        offset: Int,
        count: Int
    ): Future[List[(Visit, Patient)]] =
      get(
        "list-recent-visit-full",
        Params("offset" -> offset, "count" -> count)
      )

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

    def listVisitByPatientReverse(
        patientId: Int,
        offset: Int,
        count: Int
    ): Future[List[Visit]] =
      get(
        "list-visit-by-patient-reverse",
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

    def getCovid2ndShotData(
        patientId: Int
    ): Future[Option[(Int, LocalDate, LocalDate)]] =
      get("get-covid-2nd-shot-data", Params("patient-id" -> patientId))

    def getText(textId: Int): Future[Text] =
      get("get-text", Params("text-id" -> textId))

    def enterText(text: Text): Future[Text] =
      post("enter-text", Params(), text)

    def updateText(text: Text): Future[Boolean] =
      post("update-text", Params(), text)

    def deleteText(textId: Int): Future[Boolean] =
      get("delete-text", Params("text-id" -> textId))

    def searchShohouSample(text: String): Future[List[String]] =
      get("search-shohou-sample", Params("text" -> text))

    def enterShinryou(shinryou: Shinryou): Future[Shinryou] =
      post("enter-shinryou", Params(), shinryou)

    def batchEnterShinryou(
        visitId: Int,
        shinryoucodes: List[Int]
    ): Future[List[Int]] =
      post("batch-enter-shinryou", Params("visit-id" -> visitId), shinryoucodes)

    def batchGetShinryou(shinryouIds: List[Int]): Future[List[Shinryou]] =
      post("batch-get-shinryou", Params(), shinryouIds)

    def deleteShinryou(shinryouId: Int): Future[Boolean] =
      get("delete-shinryou", Params("shinryou-id" -> shinryouId))

    def batchEnterShinryouConduct(
        req: CreateShinryouConductRequest
    ): Future[(List[Int], List[Int])] =
      post("batch-enter-shinryou-conduct", Params(), req)

    def getShinryouEx(shinryouId: Int): Future[ShinryouEx] =
      get("get-shinryou-ex", Params("shinryou-id" -> shinryouId))

    def listShinryouExForVisit(visitId: Int): Future[List[ShinryouEx]] =
      get("list-shinryou-ex-for-visit", Params("visit-id" -> visitId))

    def getConductEx(conductId: Int): Future[ConductEx] =
      get("get-conduct-ex", Params("conduct-id" -> conductId))

    def deleteConductEx(conductId: Int): Future[Boolean] =
      get("delete-conduct-ex", Params("conduct-id" -> conductId))

    def updateChargeValue(visitId: Int, chargeValue: Int): Future[Charge] =
      get(
        "update-charge-value",
        Params("visit-id" -> visitId, "charge-value" -> chargeValue)
      )

    def setChargeValue(visitId: Int, chargeValue: Int): Future[Charge] =
      get(
        "set-charge-value",
        Params("visit-id" -> visitId, "charge-value" -> chargeValue)
      )

    def enterChargeValue(visitId: Int, chargeValue: Int): Future[Charge] =
      get(
        "enter-charge-value",
        Params("visit-id" -> visitId, "charge-value" -> chargeValue)
      )

    def enterPayment(payment: Payment): Future[Boolean] =
      post("enter-payment", Params(), payment)

    def countSearchTextGlobally(text: String): Future[Int] =
      get("count-search-text-globally", Params("text" -> text))

    def searchTextGlobally(
        text: String,
        limit: Int,
        offset: Int
    ): Future[List[(Text, Visit, Patient)]] =
      get(
        "search-text-globally",
        Params("text" -> text, "limit" -> limit, "offset" -> offset)
      )

    def countSearchTextForPatient(text: String, patientId: Int): Future[Int] =
      get(
        "count-search-text-for-patient",
        Params("text" -> text, "patient-id" -> patientId)
      )

    def searchTextForPatient(
        text: String,
        patientId: Int,
        limit: Int,
        offset: Int
    ): Future[List[(Text, Visit)]] =
      get(
        "search-text-for-patient",
        Params(
          "text" -> text,
          "patient-id" -> patientId,
          "limit" -> limit,
          "offset" -> offset
        )
      )

    def listCurrentDiseaseEx(patientId: Int): Future[
      List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]
    ] =
      get("list-current-disease-ex", Params("patient-id" -> patientId))

    def listDiseaseEx(patientId: Int): Future[
      List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]
    ] =
      get("list-disease-ex", Params("patient-id" -> patientId))

    def enterDiseaseEx(data: DiseaseEnterData): Future[Int] =
      post("enter-disease-ex", Params(), data)

    def getDiseaseEx(
        diseaseId: Int
    ): Future[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])] =
      get("get-disease-ex", Params("disease-id" -> diseaseId))

    def endDisease(
        diseaseId: Int,
        endDate: LocalDate,
        endReason: DiseaseEndReason
    ): Future[Boolean] =
      get(
        "end-disease",
        Params(
          "disease-id" -> diseaseId,
          "end-date" -> endDate,
          "end-reason" -> endReason.code
        )
      )

    def updateDiseaseEx(disease: Disease, shuushokugocodes: List[Int]): Future[Boolean] =
      post("update-disease-ex", Params(), (disease, shuushokugocodes))

    def deleteDiseaseEx(diseaseId: Int): Future[Boolean] =
      get("delete-disease-ex", Params("disease-id" -> diseaseId))
