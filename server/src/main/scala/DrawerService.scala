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
import dev.fujiwara.drawer.pdf.PdfPrinter
import dev.fujiwara.scala.drawer.Op
import java.io.OutputStream
import java.io.FileOutputStream
import scala.collection.JavaConverters.*
import dev.fujiwara.scala.drawer.ToJavaOp
import dev.myclinic.scala.config.StampInfo
import dev.fujiwara.drawer.pdf.Stamper

object DrawerService:
  object intTextId extends QueryParamDecoderMatcher[Int]("text-id")
  object strPaperSize extends QueryParamDecoderMatcher[String]("paper-size")
  object strFileName extends QueryParamDecoderMatcher[String]("file-name")
  object strInFile extends QueryParamDecoderMatcher[String]("in-file")
  object strOutFile extends QueryParamDecoderMatcher[String]("out-file")
  object strStamp extends QueryParamDecoderMatcher[String]("stamp")

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
          headers = Headers(`Content-Type`(MediaType.application.json))
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

    case req @ POST -> Root / "create-pdf-file" :? strPaperSize(
          paperSize
        ) +& strFileName(fileName) =>
      val op =
        for ops <- req.as[List[Op]]
        yield
          val printer = new PdfPrinter(paperSize)
          val outPath = Config.resolvePortalTmpFile(fileName)
          val outStream = new FileOutputStream(outPath.toString)
          try
            printer.print(
              List(ops.map(ToJavaOp.convert(_)).asJava).asJava,
              outStream
            )
            true
          finally outStream.close()
      Ok(op)

    case req @ POST -> Root / "concat-pdf-files" :? strOutFile(outFile) =>
      val op =
        for
          files <- req.as[List[String]]
          srcList = files.map(f => Config.resolvePortalTmpFile(f).toString)
        yield
          dev.fujiwara.drawer.pdf.Concatenator.concatenate(srcList.asJava, 
            Config.resolvePortalTmpFile(outFile).toString)
          true
      Ok(op)

    case GET -> Root / "stamp-pdf" :? strFileName(fileName) +& strStamp(stamp) =>
      val stampInfo: StampInfo = Config.getStampInfo(stamp)
      val opt = new Stamper.StamperOption()
      opt.scale = stampInfo.scale
      opt.xPos = stampInfo.xPos
      opt.yPos = stampInfo.yPos
      opt.stampCenterRelative = stampInfo.isImageCenterRelative
      val srcFile = Config.resolvePortalTmpFile(fileName)
      val tmpFile = Config.resolvePortalTmpFile(stampFileName(fileName))
      val stamper: Stamper = new Stamper()
      stamper.putStamp(srcFile.toString, stampInfo.imageFile, tmpFile.toString, opt)
      java.nio.file.Files.delete(srcFile)
      java.nio.file.Files.move(tmpFile, srcFile)
      Ok(true)

    // case GET -> Root / "stamp-pdf" :? strInFile(inFile) +& strOutFile(outFile) +& strStamp(stamp) =>
    //   val stampInfo: StampInfo = Config.getStampInfo(stamp)
    //   val opt = new Stamper.StamperOption()
    //   opt.scale = stampInfo.scale
    //   opt.xPos = stampInfo.xPos
    //   opt.yPos = stampInfo.yPos
    //   opt.stampCenterRelative = stampInfo.isImageCenterRelative
    //   val dir = Config.portalTmpDir
    //   val srcFile = dir.resolve(inFile).toString
    //   val dstFile = dir.resolve(outFile).toString
    //   val stamper: Stamper = new Stamper()
    //   stamper.putStamp(srcFile, stampInfo.imageFile, dstFile, opt)
    //   Ok(true)
  }

  def stampFileName(src: String): String =
    val (name, ext) = {
      val index = src.lastIndexOf(".")
      if index >= 0 then (src.substring(0, index), src.substring(index))
      else (src, "")
    }
    s"${name}-stamp-tmp${ext}"

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
