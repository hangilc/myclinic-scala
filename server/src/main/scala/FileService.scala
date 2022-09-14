package dev.myclinic.scala.server

import cats.*
import cats.syntax.all.*
import cats.effect.*
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.headers._
import org.http4s.multipart.*
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import io.circe._
import io.circe.syntax._
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import fs2.*
import scodec.bits.ByteVector
import fs2.io.file.Path
import fs2.io.file.CopyFlags
import fs2.io.file.CopyFlag
import dev.myclinic.scala.config.Config
import org.http4s.headers.*
import dev.myclinic.scala.model.FileInfo
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import java.time.LocalDate
import java.io.File
import scala.io.Source
import org.typelevel.ci.CIString

object FileService extends DateTimeQueryParam with Publisher:
  object intPatientId extends QueryParamDecoderMatcher[Int]("patient-id")
  object strFileName extends QueryParamDecoderMatcher[String]("file-name")
  object strSrc extends QueryParamDecoderMatcher[String]("src")
  object strDst extends QueryParamDecoderMatcher[String]("dst")
  object strDir extends QueryParamDecoderMatcher[String]("dir")

  val covid2ndShotMap: Map[Int, (Int, LocalDate, LocalDate)] =
    var map: Map[Int, (Int, LocalDate, LocalDate)] = Map.empty
    val pat = """(\d+)\s+(\d+)\s+([0-9-]+)\s+([0-9-]+)""".r
    val file = new File(System.getenv("MYCLINIC_DATA"), "covid-2nd-shots.txt")
    for line <- Source.fromFile(file).getLines.map(_.trim) do
      line match {
        case pat(pPatientId, pAge, pSecondShot, pThirdShotDue) =>
          val patientId = pPatientId.toInt
          val age = pAge.toInt
          val secondShot = LocalDate.parse(pSecondShot)
          val thirdShotDue = LocalDate.parse(pThirdShotDue)
          map = map + (patientId -> (age, secondShot, thirdShotDue))
        case _ => System.err.println(s"Invalid 2nd shot data: %{line}")
      }
    map

  private def saveToFile(req: Request[IO], path: Path): IO[Response[IO]] =
    Ok(saveToFile(req.body, path).compile.drain.map(_ => true))

  private def saveToFile(
      sin: Stream[IO, Byte],
      path: Path
  ): Stream[IO, Nothing] =
    sin.through(fs2.io.file.Files[IO].writeAll(path))

  private def resolveDir(dir: String): Path =
    dir match {
      case "scan-dir" => Path.fromNioPath(Config.paperScanRoot)
      case _ => throw new RuntimeException("Cannot resolve dir: " + dir)
    }

  private def sanitizeFileName(fileName: String): String =
    import dev.myclinic.scala.util.FunUtil.*
    def ensureNoParentDir(s: String): String =
      if s.startsWith("../") || s.contains("/../") then
        throw new RuntimeException("Invalid file name (including parent dir).")
      s
    def ensureNoParentDirWindows(s: String): String =
      if s.startsWith("..\\") || s.contains("\\..\\") then
        throw new RuntimeException("Invalid file name (including parent dir).")
      s
    fileName
      |> ensureNoParentDir _
      |> ensureNoParentDirWindows _

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case req @ POST -> Root / "save-patient-image" :? intPatientId(
          patientId
        ) +& strFileName(fileName) =>
      val loc =
        new java.io.File(
          Config.paperScanDir(patientId),
          sanitizeFileName(fileName)
        ).getPath
      saveToFile(req, Path(loc))

    case GET -> Root / "rename-patient-image" :? intPatientId(patientId)
        +& strSrc(src) +& strDst(dst) =>
      val dir = Config.paperScanDir(patientId)
      val srcLoc = Path(new java.io.File(dir, sanitizeFileName(src)).getPath)
      val dstLoc = Path(new java.io.File(dir, sanitizeFileName(dst)).getPath)
      val op =
        fs2.io.file
          .Files[IO]
          .move(srcLoc, dstLoc)
          .map(_ => true)
      Ok(op)

    case GET -> Root / "delete-patient-image" :? intPatientId(patientId)
        +& strFileName(fileName) =>
      val dir = Config.paperScanDir(patientId)
      val loc = Path(new java.io.File(dir, sanitizeFileName(fileName)).getPath)
      val op =
        fs2.io.file.Files[IO].delete(loc).map(_ => true)
      Ok(op)

    case GET -> Root / "list-patient-image" :? intPatientId(patientId) =>
      val dir = Config.paperScanDir(patientId)
      val loc = Path(new java.io.File(dir).getPath)
      val op =
        for list <- 
          fs2.io.file
            .Files[IO]
            .list(loc)
            .evalMap(path =>
              fs2.io.file
                .Files[IO]
                .getBasicFileAttributes(path)
                .map(attr =>
                  val ctime = FileInfo.fromTimestamp(attr.creationTime)
                  println(path.fileName)
                  println(attr.creationTime)
                  FileInfo(path.fileName.toString, ctime, attr.size)
                )
            )
            .compile
            .toList
        yield list.sortBy(fi => fi.createdAt).reverse
      Ok(op)

    case GET -> Root / "get-patient-image" :? intPatientId(
          patientId
        ) +& strFileName(fileName) =>
      val dir = Config.paperScanDir(patientId)
      val loc = Path(new java.io.File(dir, sanitizeFileName(fileName)).getPath)
      val mediaType: MediaType =
        if fileName.endsWith(".pdf") then MediaType.application.pdf
        else MediaType.image.jpeg
      val op =
        fs2.io.file.Files[IO].readAll(loc)
      Ok(op, `Content-Type`(mediaType))

    case req @ GET -> Root / "patient-image" :? intPatientId(
          patientId
        ) +& strFileName(fileName) =>
      val file =
        Path(Config.paperScanDir(patientId)) / sanitizeFileName(fileName)
      StaticFile.fromPath(file, Some(req)).getOrElse(Response(Status.NotFound))

    case GET -> Root / "get-covid-2nd-shot-data" :? intPatientId(patientId) =>
      val data = covid2ndShotMap.get(patientId)
      Ok(data)

    case req @ POST -> Root / "upload-file" :? strDir(dir) =>
      val dirPath = resolveDir(dir)
      req.decode[Multipart[IO]] { m =>
        Stream
          .emits(m.parts)
          .covary[IO]
          .flatMap(part =>
            val filename: String = sanitizeFileName(part.filename.get)
            saveToFile(part.body, dirPath / filename)
          )
          .compile
          .drain
          .flatMap(_ => Ok(true))
      }

    case req @ POST -> Root / "upload-patient-image" :? intPatientId(
          patientId
        ) =>
      val nioDirPath = java.nio.file.Path.of(Config.paperScanDir(patientId))
      if !java.nio.file.Files
          .exists(nioDirPath) && !nioDirPath.toFile.mkdirs
      then throw new RuntimeException("Cannot make patient image directory.")
      val dirPath = Path.fromNioPath(nioDirPath)
      req.decode[Multipart[IO]] { m =>
        Stream
          .emits(m.parts)
          .covary[IO]
          .flatMap(part =>
            val filename: String = sanitizeFileName(part.filename.get)
            saveToFile(part.body, dirPath / filename)
          )
          .compile
          .drain
          .flatMap(_ => Ok(true))
      }

    case GET -> Root / "delete-portal-tmp-file" :? strFileName(fileName) =>
      val path = Config.resolvePortalTmpFile(fileName)
      java.nio.file.Files.delete(path)
      Ok(true)

    case GET -> Root / "get-webphone-token" => Ok(TwilioUtil.getWebphoneToken())

  }
