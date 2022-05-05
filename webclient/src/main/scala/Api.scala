package dev.myclinic.scala.webclient

object Api extends AppointApi.Api with PatientApi.Api with MiscApi.Api
  with PrintApi.Api with VisitApi.Api with MasterApi.Api
  with DrawerApi.Api
