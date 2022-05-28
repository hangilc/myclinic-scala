package dev.myclinic.scala.model

case class CreateConductRequest(
    visitId: Int,
    kind: Int,
    labelOption: Option[String] = None,
    shinryouList: List[ConductShinryou] = List.empty,
    drugs: List[ConductDrug] = List.empty,
    kizaiList: List[ConductKizai] = List.empty
)

case class CreateShinryouConductRequest(
    shinryouList: List[Shinryou] = List.empty,
    conducts: List[CreateConductRequest] = List.empty
)
