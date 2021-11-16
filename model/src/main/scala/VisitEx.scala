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

case class ShinryouEx(
    shinryouId: Int,
    visitId: Int,
    shinryoucode: Int,
    master: ShinryouMaster
)

case class ConductEx(
    conductId: Int,
    visitId: Int,
    kind: Int,
    drugs: List[ConductDrugEx] = List.empty,
    shinryouList: List[ConductShinryouEx] = List.empty,
    kizaiList: List[ConductKizaiEx] = List.empty
)

case class ConductDrugEx(
    conductDrugId: Int,
    conductId: Int,
    iyakuhincode: Int,
    amount: Double,
    master: IyakuhinMaster
)

case class ConductShinryouEx(
    conductShinryouId: Int,
    conductId: Int,
    shinryoucode: Int,
    master: ShinryouMaster
)

case class ConductKizaiEx(
    conductKizaiId: Int,
    conductId: Int,
    kizaicode: Int,
    amount: Double,
    master: KizaiMaster
)
