package dev.myclinic.scala.model

import java.time.LocalDateTime

case class VisitEx(
    visitId: Int,
    visitedAt: LocalDateTime,
    attributesStore: Option[String],
    patient: Patient,
    hoken: HokenInfo,
    texts: List[Text] = List.empty,
    drugs: List[DrugEx] = List.empty,
    shinryouList: List[ShinryouEx] = List.empty,
    conducts: List[ConductEx] = List.empty,
    chargeOption: Option[Charge] = None,
    lastPayment: Option[Payment] = None
):
  def patientId: Int = patient.patientId
  def shahokokuho: Option[Shahokokuho] = hoken.shahokokuho
  def koukikourei: Option[Koukikourei] = hoken.koukikourei
  def roujin: Option[Roujin] = hoken.roujin
  def kouhiList: List[Kouhi] = hoken.kouhiList
  def toVisit: Visit =
    Visit(
      visitId,
      patientId,
      visitedAt,
      hoken.shahokokuhoId,
      hoken.roujinId,
      hoken.kouhi1Id,
      hoken.kouhi2Id,
      hoken.kouhi3Id,
      hoken.koukikoureiId,
      attributesStore
    )

object VisitEx:
  def apply(
      visit: Visit,
      patient: Patient,
      hoken: HokenInfo,
      texts: List[Text],
      drugs: List[DrugEx],
      shinryouList: List[ShinryouEx],
      conducts: List[ConductEx],
      chargeOption: Option[Charge],
      lastPayment: Option[Payment]
  ): VisitEx =
    VisitEx(
      visit.visitId,
      visit.visitedAt,
      visit.attributesStore,
      patient,
      hoken,
      texts,
      drugs,
      shinryouList,
      conducts,
      chargeOption,
      lastPayment
    )

case class HokenInfo(
    shahokokuho: Option[Shahokokuho] = None,
    roujin: Option[Roujin] = None,
    koukikourei: Option[Koukikourei] = None,
    kouhiList: List[Kouhi] = List.empty
):
  def shahokokuhoId: Int = shahokokuho.map(_.shahokokuhoId).getOrElse(0)
  def roujinId: Int = roujin.map(_.roujinId).getOrElse(0)
  def koukikoureiId: Int = koukikourei.map(_.koukikoureiId).getOrElse(0)
  lazy val kouhiIds: List[Int] = kouhiList.map(_.kouhiId)
  def kouhi1Id: Int = kouhiIds.applyOrElse(0, _ => 0)
  def kouhi2Id: Int = kouhiIds.applyOrElse(1, _ => 0)
  def kouhi3Id: Int = kouhiIds.applyOrElse(2, _ => 0)

case class DrugEx(
    drugId: Int,
    visitId: Int,
    iyakuhincode: Int,
    amount: Double,
    usage: String,
    days: Int,
    categoryStore: Int,
    prescribed: Boolean,
    master: IyakuhinMaster
):
  lazy val category: DrugCategory =
    DrugCategory.fromCode(categoryStore)

object DrugEx:
  def apply(drug: Drug, master: IyakuhinMaster): DrugEx =
    DrugEx(
      drug.drugId,
      drug.visitId,
      drug.iyakuhincode,
      drug.amount,
      drug.usage,
      drug.days,
      drug.categoryStore,
      drug.prescribed,
      master
    )

case class ShinryouEx(
    shinryouId: Int,
    visitId: Int,
    shinryoucode: Int,
    master: ShinryouMaster
):
  def toShinryou: Shinryou =
    Shinryou(shinryouId, visitId, shinryoucode)

object ShinryouEx:
  def apply(shinryou: Shinryou, master: ShinryouMaster): ShinryouEx =
    ShinryouEx(
      shinryou.shinryouId,
      shinryou.visitId,
      shinryou.shinryoucode,
      master
    )

case class ConductEx(
    conductId: Int,
    visitId: Int,
    kind: ConductKind,
    gazouLabel: Option[String],
    drugs: List[ConductDrugEx] = List.empty,
    shinryouList: List[ConductShinryouEx] = List.empty,
    kizaiList: List[ConductKizaiEx] = List.empty
):
  def toConduct: Conduct =
    Conduct(conductId, visitId, kind.code)

object ConductEx:
  def apply(
      c: Conduct,
      gazouLabel: Option[String],
      drugs: List[ConductDrugEx],
      shinryouList: List[ConductShinryouEx],
      kizaiList: List[ConductKizaiEx]
  ): ConductEx =
    ConductEx(
      c.conductId,
      c.visitId,
      c.kind,
      gazouLabel,
      drugs,
      shinryouList,
      kizaiList
    )

case class ConductDrugEx(
    conductDrugId: Int,
    conductId: Int,
    iyakuhincode: Int,
    amount: Double,
    master: IyakuhinMaster
)

object ConductDrugEx:
  def apply(d: ConductDrug, master: IyakuhinMaster): ConductDrugEx =
    ConductDrugEx(
      d.conductDrugId,
      d.conductId,
      d.iyakuhincode,
      d.amount,
      master
    )

case class ConductShinryouEx(
    conductShinryouId: Int,
    conductId: Int,
    shinryoucode: Int,
    master: ShinryouMaster
)

object ConductShinryouEx:
  def apply(s: ConductShinryou, master: ShinryouMaster): ConductShinryouEx =
    ConductShinryouEx(
      s.conductShinryouId,
      s.conductId,
      s.shinryoucode,
      master
    )

case class ConductKizaiEx(
    conductKizaiId: Int,
    conductId: Int,
    kizaicode: Int,
    amount: Double,
    master: KizaiMaster
)

object ConductKizaiEx:
  def apply(k: ConductKizai, master: KizaiMaster): ConductKizaiEx =
    ConductKizaiEx(k.conductKizaiId, k.conductId, k.kizaicode, k.amount, master)
