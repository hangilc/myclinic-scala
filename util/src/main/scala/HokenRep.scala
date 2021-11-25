package dev.myclinic.scala.util

import scala.collection.mutable.ListBuffer

object HokenRep:
  def apply(
      shahokokuhoHokenshaBangou: Option[Int],
      shahokokuhoKoureiFutanWari: Option[Int],
      koukikoureiFutanWari: Option[Int],
      roujinFutanWari: Option[Int],
      kouhi1FutanshaBangou: Option[Int],
      kouhi2FutanshaBangou: Option[Int],
      kouhi3FutanshaBangou: Option[Int]
  ): String =
    val terms = ListBuffer[String]()
    def add(s: String): Unit = terms += s
    println(
      ("hoken-rep", shahokokuhoHokenshaBangou, shahokokuhoKoureiFutanWari)
    )
    for bangou <- shahokokuhoHokenshaBangou
    yield add(shahokokuhoRep(bangou, shahokokuhoKoureiFutanWari))
    for futan <- koukikoureiFutanWari yield add(koukikoureiRep(futan))
    for futan <- roujinFutanWari yield add(roujinRep(futan))
    terms ++= List(
      kouhi1FutanshaBangou,
      kouhi2FutanshaBangou,
      kouhi3FutanshaBangou
    )
      .filter(_.isDefined)
      .map(_.get)
      .map(kouhiRep(_))
    terms.toList.mkString("・")

  def shahokokuhoRep(
      shahokokuhoHokenshaBangou: Int,
      shahokokuhoKoureiFutanWari: Option[Int]
  ): String =
    val name: String = shahokokuhoName(shahokokuhoHokenshaBangou)
    shahokokuhoKoureiFutanWari.fold(name)(futan =>
      (name + "・高齢" + futan.toString + "割")
    )

  def shahokokuhoName(hokenshaBangou: Int): String =
    if hokenshaBangou <= 9999 then "政管健保"
    else if hokenshaBangou <= 999999 then "国保"
    else
      (hokenshaBangou / 1000000) match {
        case 1  => "協会けんぽ"
        case 2  => "船員"
        case 3  => "日雇一般"
        case 4  => "日雇特別"
        case 6  => "組合健保"
        case 7  => "自衛官"
        case 31 => "国家公務員共済"
        case 32 => "地方公務員共済"
        case 33 => "警察公務員共済"
        case 34 => "学校共済"
        case 63 => "特定健保退職"
        case 67 => "国保退職"
        case 72 => "国家公務員共済退職"
        case 73 => "地方公務員共済退職"
        case 74 => "警察公務員共済退職"
        case 75 => "学校共済退職"
        case _  => "不明"
      }

  def koukikoureiRep(futanWari: Int): String = "後期高齢" + futanWari.toString + "割"
  def roujinRep(futanWari: Int): String = "老人" + futanWari.toString + "割"
  def kouhiRep(futanshaBangou: Int): String =
    if ((futanshaBangou / 1000000) == 41) then "マル福"
    else if ((futanshaBangou / 1000) == 80136) then "マル障（１割負担）"
    else if ((futanshaBangou / 1000) == 80137) then "マル障（負担なし）"
    else if ((futanshaBangou / 1000) == 81136) then "マル親（１割負担）"
    else if ((futanshaBangou / 1000) == 81137) then "マル親（負担なし）"
    else if ((futanshaBangou / 1000000) == 88) then "マル乳"
    else String.format("公費負担（%d）", futanshaBangou)
