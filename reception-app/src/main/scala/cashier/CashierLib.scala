package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.web.appbase.PrintDialog
import dev.fujiwara.domq.{Modal}
import dev.fujiwara.scala.drawer.Op

object CashierLib:
  def openPrintDialog(
      title: String,
      ops: List[Op]
  ): Unit =
    val scale = 3
    val w = 148
    val h = 105
    val dlog = PrintDialog(
      title,
      ops,
      w * scale,
      h * scale,
      s"0, 0, $w, $h",
      prefKind = "receipt"
    )
    dlog.open()
