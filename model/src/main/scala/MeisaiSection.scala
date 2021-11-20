package dev.myclinic.scala.model

enum MeisaiSection(val label: String):
  case ShoshinSaisin extends MeisaiSection("初・再診料")
  case IgakuKanri extends MeisaiSection("医学管理等")
  case Zaitaku extends MeisaiSection("在宅医療")
  case Kensa extends MeisaiSection("検査")
  case Gazou extends MeisaiSection("画像診断")
  case Touyaku extends MeisaiSection("投薬")
  case Chuusha extends MeisaiSection("注射")
  case Shochi extends MeisaiSection("処置")
  case Sonota extends MeisaiSection("その他")

case class MeisaiSectionItem(tanka: Int, count: Int, label: String):
  def toal: Int = tanka * count
