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
    attributes: Option[String],
    patient: Patient,
    shahokokuho: Option[Shahokokuho] = None,
    roujin: Option[Roujin] = None,
    koukikourei: Option[Koukikourei] = None,
    kouhiList: List[Kouhi] = List.empty,
    texts: List[Text] = List.empty,
    drugs: List[DrugEx] = List.empty,
    shinryouList: List[ShinryouEx] = List.empty,
    conducts: List[ConductEx] = List.empty
)

object VisitEx:
  def apply(visit: Visit, patient: Patient, shahokokuho: Option[Shahokokuho], roujin: Option[Roujin],
  koukikourei: Option[Koukikourei], kouhiList: List[Kouhi], texts: List[Text], drugs: List[DrugEx],
  shinryouList: List[ShinryouEx], conducts: List[ConductEx]): VisitEx =
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
      visit.attributes,
      patient,
      shahokokuho,
      roujin,
      koukikourei,
      kouhiList,
      texts,
      drugs,
      shinryouList,
      conducts
    )

case class DrugEx(
    drugId: Int,
    visitId: Int,
    iyakuhincode: Int,
    amount: Double,
    usage: String,
    days: Int,
    category: Int,
    prescribed: Boolean,
    master: IyakuhinMaster
)

object DrugEx:
  def apply(drug: Drug, master: IyakuhinMaster): DrugEx =
    DrugEx(
      drug.drugId,
      drug.visitId,
      drug.iyakuhincode,
      drug.amount,
      drug.usage,
      drug.days,
      drug.category,
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
    kind: Int,
    drugs: List[ConductDrugEx] = List.empty,
    shinryouList: List[ConductShinryouEx] = List.empty,
    kizaiList: List[ConductKizaiEx] = List.empty
)

object ConductEx:
  def apply(
      c: Conduct,
      drugs: List[ConductDrugEx],
      shinryouList: List[ConductShinryouEx],
      kizaiList: List[ConductKizaiEx]
  ): ConductEx =
    ConductEx(
      c.conductId,
      c.visitId,
      c.kind,
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
