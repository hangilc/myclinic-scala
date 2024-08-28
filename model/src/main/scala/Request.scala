package dev.myclinic.scala.model

import dev.fujiwara.scala.drawer.Op;

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

case class DrawerPdfWithStampRequest(
    paperSize: String,
    opsList: List[List[Op]],
    outFile: String,
    stamp: String, // base64 encoded JPEG image
    page: Int, // 1-based
    left: Double,
    top: Double,
    width: Double,
    height: Double,
)
