package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.ShahokokuhoProps

object HokenHelper:
  def hokenDetail(hoken: Hoken): String =
    hoken match {
      case h: Shahokokuho => shahokokuhoDetail(h)
      case h: Koukikourei => koukikoureiDetail(h)
      case h: Roujin => roujinDetail(h)
      case h: Kouhi => kouhiDetail(h)
    }

  def shahokokuhoDetail(h: Shahokokuho): String =
    ???

  def koukikoureiDetail(h: Koukikourei): String =
    ???

  def roujinDetail(h: Roujin): String =
    ???

  def kouhiDetail(h: Kouhi): String =
    ???

