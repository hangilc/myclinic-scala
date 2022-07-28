package dev.myclinic.scala.web.practiceapp.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.*

class WqueueTable:
  import WqueueTable.Item
  val itemWrapper = div
  val wqueue = new CompSortList[Item](itemWrapper)
  val ele = div(
    titles
  )

  def titles: List[HTMLElement] =
    List(
      div("患者"),
      div("請求額"),
      div("操作")
    )

object WqueueTable:
  case class Item(wqueue: Wqueue)

  given Ordering[Item] = Ordering.by(_.wqueue.visitId)
  given Comp[Item] = _.ele