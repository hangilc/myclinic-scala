package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.*

object ConductUtil:
  def conductDrugRep(d: ConductDrugEx): String =
    s"${d.master.name} ${d.amount}${d.master.unit}"

  def conductKizaiRep(k: ConductKizaiEx): String =
    s"${k.master.name} ${k.amount}${k.master.unit}"