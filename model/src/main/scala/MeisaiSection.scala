package dev.myclinic.scala.model

enum MeisaiSection(val label: String, val items: List[MeisaiSectionItem]):
  case ShoshinSaisin(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("初・再診料", items)
  case IgakuKanri(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("医学管理等", items)
  case Zaitaku(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("在宅医療", items)
  case Kensa(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("検査", items)
  case Gazou(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("画像診断", items)
  case Touyaku(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("投薬", items)
  case Chuusha(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("注射", items)
  case Shochi(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("処置", items)
  case Sonota(override val items: List[MeisaiSectionItem])
      extends MeisaiSection("その他", items)

case class MeisaiSectionItem(tanka: Int, count: Int, label: String):
  def toal: Int = tanka * count
