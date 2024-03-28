package dev.myclinic.scala.server

import cats.*
import cats.syntax.*
import cats.effect.*
import cats.implicits.toTraverseOps
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.headers._
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.EntityEncoder._
import io.circe._
import io.circe.syntax._
import io.circe.Decoder.decodeList
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
import dev.myclinic.scala.model.HokenInfo
import dev.myclinic.scala.apputil.FutanWari
import java.time.LocalDate
import scala.concurrent.Future
import dev.myclinic.scala.model.Shahokokuho
import dev.myclinic.scala.util.ZenkakuUtil
import fs2.io.{readOutputStream, readInputStream}
import java.io.PipedOutputStream
import java.io.PipedInputStream
import java.util.ArrayList

object DrawerService:
  object intTextId extends QueryParamDecoderMatcher[Int]("text-id")
  object strPaperSize extends QueryParamDecoderMatcher[String]("paper-size")
  object strFileName extends QueryParamDecoderMatcher[String]("file-name")
  object strInFile extends QueryParamDecoderMatcher[String]("in-file")
  object strOutFile extends QueryParamDecoderMatcher[String]("out-file")
  object strStamp extends QueryParamDecoderMatcher[String]("stamp")

  val clinicInfo = Config.getClinicInfo
  val objectMapper = dev.fujiwara.drawer.op.JsonCodec.createMapper()

  def adjustHokenInfo(src: HokenInfo): HokenInfo =
    val shahokokuho = src.shahokokuho.map(shahokokuho => {
      Shahokokuho.apply(
        shahokokuho.shahokokuhoId,
        shahokokuho.patientId,
        shahokokuho.hokenshaBangou,
        ZenkakuUtil.toHankaku(shahokokuho.hihokenshaKigou),
        ZenkakuUtil.toHankaku(shahokokuho.hihokenshaBangou),
        shahokokuho.honninStore,
        shahokokuho.validFrom,
        shahokokuho.validUpto,
        shahokokuho.koureiStore,
        ZenkakuUtil.toHankaku((shahokokuho.edaban))
      )
    })
    val koukikourei = src.koukikourei.map(koukikourei => {
      koukikourei
    })
    HokenInfo.apply(shahokokuho, src.roujin, koukikourei, src.kouhiList)

  def handleShohousenDraw(text: Text): IO[Response[IO]] =
    for
      visit <- Db.getVisit(text.visitId)
      patient <- Db.getPatient(visit.patientId)
      hokenInfo <- Db.getHokenInfo(visit.visitId)
    yield
      // val issuedAt: LocalDate = LocalDate.now()
      val issuedAt: LocalDate = visit.visitedAt.toLocalDate();
      val validUpto: Option[LocalDate] = None
      val json = drawShohousen(
        text,
        visit,
        patient,
        adjustHokenInfo(hokenInfo),
        issuedAt,
        validUpto
      )
      Response[IO](
        body = fs2.Stream.emits(json.getBytes()),
        headers = Headers(`Content-Type`(MediaType.application.json))
      )

  def opsListAsJava(
      opsList: List[List[Op]]
  ): java.util.List[java.util.List[dev.fujiwara.drawer.op.Op]] =
    val cvt: Op => dev.fujiwara.drawer.op.Op = a => ToJavaOp.convert(a)
    opsList.map(ops => ops.map(cvt).asJava).asJava

  def routes = HttpRoutes.of[IO] {
    case GET -> Root / "shohousen-drawer" :? intTextId(textId) =>
      for
        text <- Db.getText(textId)
        resp <- handleShohousenDraw(text)
      yield resp

    case req @ POST -> Root / "shohousen-drawer-text" =>
      for
        text <- req.as[Text]
        resp <- handleShohousenDraw(text)
      yield resp

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

    case req @ POST -> Root / "create-multi-page-pdf-file" :? strPaperSize(
          paperSize
        ) +& strFileName(fileName) =>
      val op =
        for opsListReq <- req.as[List[List[Op]]]
        yield
          val opsList = opsListAsJava((opsListReq))
          val printer = new PdfPrinter(paperSize)
          val outPath = Config.resolvePortalTmpFile(fileName)
          val outStream = new FileOutputStream(outPath.toString)
          try
            printer.print(
              opsList,
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
          dev.fujiwara.drawer.pdf.Concatenator.concatenate(
            srcList.asJava,
            Config.resolvePortalTmpFile(outFile).toString
          )
          srcList.map(s => java.nio.file.Files.delete(java.nio.file.Path.of(s)))
          true
      Ok(op)

    case GET -> Root / "stamp-pdf" :? strFileName(fileName) +& strStamp(
          stamp
        ) =>
      val stampInfo: StampInfo = Config.getStampInfo(stamp)
      val opt = new Stamper.StamperOption()
      opt.scale = stampInfo.scale
      opt.xPos = stampInfo.xPos
      opt.yPos = stampInfo.yPos
      opt.stampCenterRelative = stampInfo.isImageCenterRelative
      val srcFile = Config.resolvePortalTmpFile(fileName)
      val tmpFile = Config.resolvePortalTmpFile(stampFileName(fileName))
      val stamper: Stamper = new Stamper()
      stamper.putStamp(
        srcFile.toString,
        stampInfo.imageFile,
        tmpFile.toString,
        opt
      )
      java.nio.file.Files.delete(srcFile)
      java.nio.file.Files.move(tmpFile, srcFile)
      Ok(true)

    case req @ POST -> Root / "drawer-pdf" :? strPaperSize(paperSize) =>
      for opsList <- req.as[List[List[Op]]]
      yield
        val printer = new PdfPrinter(paperSize)
        val outStream = new PipedOutputStream()
        val inStream = new PipedInputStream(outStream)
        val cvt: Op => dev.fujiwara.drawer.op.Op = a => ToJavaOp.convert(a)
        val javaOpsList = opsList.map(ops => ops.map(op => cvt(op)))
        val javaOpsList2 = javaOpsList.map(ops => ops.asJava)
        val pages = new java.util.ArrayList(javaOpsList2.asJava)
        Response(
          body = readOutputStream(1024)(out =>
            IO {
              printer.print(pages, out); out.close()
            }
          ),
          headers = Headers(`Content-Type`(MediaType.text.plain))
        )
  }

  def stampFileName(src: String): String =
    val (name, ext) = {
      val index = src.lastIndexOf(".")
      if index >= 0 then (src.substring(0, index), src.substring(index))
      else (src, "")
    }
    s"${name}-stamp-tmp${ext}"

  def drawShohousen(
      text: Text,
      visit: Visit,
      patient: Patient,
      hokenInfo: HokenInfo,
      koufuDate: LocalDate,
      validUptoDate: Option[LocalDate]
  ): String =
    import dev.myclinic.scala.javalib.convs.Convs.*
    val data = new ShohousenData()
    data.setClinicInfo(clinicInfoDTO(clinicInfo))
    data.setHoken(hokenInfo.toDTO)
    data.setPatient(patient.toDTO)
    data.setFutanWari(FutanWari.calcFutanWari(patient, visit, hokenInfo))
    data.setKoufuDate(koufuDate)
    validUptoDate.foreach(d => data.setValidUptoDate(d))
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
