package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{Appoint, AppointTime}

class AdminAppointSheet extends AppointSheet:
  override def makeAppointTimeBox(
      appointTime: AppointTime,
      appoints: List[Appoint]
  ): AppointTimeBox =
    val box = AdminAppointTimeBox(appointTime)
    box.init(appoints)
    box
    
