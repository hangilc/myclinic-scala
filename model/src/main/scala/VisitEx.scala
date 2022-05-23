package dev.myclinic.scala.model

import java.time.LocalDateTime

case class VisitEx(
    visitId: Int,
    patientId: Int,
    visitedAt: LocalDateTime,
    shahokokuhoId: Int,
    roujinId: Int,
    kouhi1Id: Int,
    kouhi2Id: Int,
    kouhi3Id: Int,
    koukikoureiId: Int,
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
  def toVisit: Visit =
    Visit(
      visitId,
      patientId,
      visitedAt,
      shahokokuhoId,
      roujinId,
      kouhi1Id,
      kouhi2Id,
      kouhi3Id,
      koukikoureiId,
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
      visit.patientId,
      visit.visitedAt,
      visit.shahokokuhoId,
      visit.roujinId,
      visit.kouhi1Id,
      visit.kouhi2Id,
      visit.kouhi3Id,
      visit.koukikoureiId,
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
)

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
)

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
)

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
