package dev.myclinic.scala.config

import dev.myclinic.scala.model.ClinicInfo
import dev.myclinic.{java => javaConfig}

class JavaConfigurator:
  val config = new javaConfig.Config()

  def getClinicInfo: ClinicInfo =
    val jc = config.getClinicInfo()
    ClinicInfo(
      jc.name,
      jc.postalCode,
      jc.address,
      jc.tel,
      jc.fax,
      jc.todoufukencode,
      jc.tensuuhyoucode,
      jc.kikancode,
      jc.homepage,
      jc.doctorName,
      jc.doctorLastName,
      jc.doctorFirstName
    )
