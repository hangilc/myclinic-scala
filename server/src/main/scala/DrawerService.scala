package dev.myclinic.scala.server

import cats.*
import cats.syntax.*
import cats.effect.*
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.headers._
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.db.Db
import dev.myclinic.scala.config.Config
import dev.fujiwara.drawer.forms.shohousen.{ShohousenData, ShohousenDrawer}
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import dev.fujiwara.dto.ClinicInfoDTO
import dev.myclinic.scala.model.{ClinicInfo, Text, Visit, Patient}
import dev.myclinic.scala.formatshohousen.Shohou
import dev.myclinic.scala.formatshohousen.FormatShohousen

object DrawerService:
  object intTextId extends QueryParamDecoderMatcher[Int]("text-id")
  val clinicInfo = Config.getClinicInfo
  val objectMapper = dev.fujiwara.drawer.op.JsonCodec.createMapper()

  def routes = HttpRoutes.of[IO] {
    case GET -> Root / "shohousen-drawer" :? intTextId(textId) =>
      for
        text <- Db.getText(textId)
        visit <- Db.getVisit(text.visitId)
        patient <- Db.getPatient(visit.patientId)
      yield
        val json = drawShohousen(text, visit, patient)
        Response(
          body = fs2.Stream.emits(json.getBytes()),
          headers = Headers(`Content-Type`(MediaType("application", "json")))
        )

    case req @ POST -> Root / "shohousen-drawer-text" =>
      for
        text <- req.as[Text]
        visit <- Db.getVisit(text.visitId)
        patient <- Db.getPatient(visit.patientId)
      yield
        val json = drawShohousen(text, visit, patient)
        Response(
          body = fs2.Stream.emits(json.getBytes()),
          headers = Headers(`Content-Type`(MediaType("application", "json")))
        )

      
  }

  def drawShohousen(text: Text, visit: Visit, patient: Patient): String =
    val data = new ShohousenData()
    data.setClinicInfo(clinicInfoDTO(clinicInfo))
    val c = FormatShohousen.parse(text.content).formatForPrint
    data.setDrugs(c)
    val drawer = new ShohousenDrawer()
    drawer.init()
    data.applyTo(drawer)
    val ops = drawer.getOps()
    objectMapper.writeValueAsString(ops)

  def clinicInfoDTO(src: ClinicInfo): ClinicInfoDTO =
    val dto = new ClinicInfoDTO()
    dto.name = src.name
    dto.postalCode = src.postalCode
    dto.address = src.address
    dto.tel = src.tel
    dto.fax = src.fax
    dto.todoufukencode = src.todoufukencode
    dto.tensuuhyoucode = src.tensuuhyoucode
    dto.kikancode = src.kikancode
    dto.homepage = src.homepage
    dto.doctorName = src.doctorName
    dto
