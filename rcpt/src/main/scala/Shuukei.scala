package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.MeisaiSection
import dev.myclinic.scala.model.MeisaiSection.*
import dev.myclinic.java.MyclinicConsts.*

object Shuukei:
  def shuukeisakiToMeisaiSection(shuukeisaki: String): MeisaiSection =
    shuukeisaki match {
      case SHUUKEI_SHOSHIN |
        SHUUKEI_SAISHIN_SAISHIN |
        SHUUKEI_SAISHIN_GAIRAIKANRI |
        SHUUKEI_SAISHIN_JIKANGAI |
        SHUUKEI_SAISHIN_KYUUJITSU |
        SHUUKEI_SAISHIN_SHINYA =>
          ShoshinSaisin
      case SHUUKEI_SHIDOU =>
          IgakuKanri
      case SHUUKEI_ZAITAKU =>
          Zaitaku
      case SHUUKEI_KENSA =>
          Kensa
      case SHUUKEI_GAZOUSHINDAN =>
          Gazou
      case SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI |
        SHUUKEI_TOUYAKU_GAIYOUCHOUZAI |
        SHUUKEI_TOUYAKU_SHOHOU |
        SHUUKEI_TOUYAKU_MADOKU |
        SHUUKEI_TOUYAKU_CHOUKI =>
          Touyaku
      case SHUUKEI_CHUUSHA_SEIBUTSUETC |
        SHUUKEI_CHUUSHA_HIKA |
        SHUUKEI_CHUUSHA_JOUMYAKU |
        SHUUKEI_CHUUSHA_OTHERS =>
          Chuusha
      case SHUUKEI_SHOCHI =>
          Shochi
      case SHUUKEI_SHUJUTSU_SHUJUTSU |
        SHUUKEI_SHUJUTSU_YUKETSU |
        SHUUKEI_MASUI |
        SHUUKEI_OTHERS =>
          Sonota 
      case _ => Sonota     
    }
