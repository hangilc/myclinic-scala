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
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import fs2.Chunk
import scodec.bits.ByteVector
import fs2.io.file.Path
import fs2.io.file.CopyFlags
import fs2.io.file.CopyFlag
import dev.myclinic.scala.config.Config
import org.http4s.headers.*
import dev.myclinic.scala.model.FileInfo
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}

object FileService extends DateTimeQueryParam with Publisher:
  object intPatientId extends QueryParamDecoderMatcher[Int]("patient-id")
  object strFileName extends QueryParamDecoderMatcher[String]("file-name")
  object strSrc extends QueryParamDecoderMatcher[String]("src")
  object strDst extends QueryParamDecoderMatcher[String]("dst")

  private def saveToFile(req: Request[IO], path: Path): IO[Response[IO]] =
    Ok(
      req.body
        .through(fs2.io.file.Files[IO].writeAll(path))
        .compile
        .drain
        .map(_ => true)
    )

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case req @ POST -> Root / "save-patient-image" :? intPatientId(
          patientId
        ) +& strFileName(fileName) =>
      val loc =
        new java.io.File(Config.paperScanDir(patientId), fileName).getPath
      saveToFile(req, Path(loc))

    case GET -> Root / "rename-patient-image" :? intPatientId(patientId)
        +& strSrc(src) +& strDst(dst) =>
      val dir = Config.paperScanDir(patientId)
      val srcLoc = Path(new java.io.File(dir, src).getPath)
      val dstLoc = Path(new java.io.File(dir, dst).getPath)
      val op =
        fs2.io.file
          .Files[IO]
          .move(srcLoc, dstLoc)
          .map(_ => true)
      Ok(op)

    case GET -> Root / "delete-patient-image" :? intPatientId(patientId)
        +& strFileName(fileName) =>
      val dir = Config.paperScanDir(patientId)
      val loc = Path(new java.io.File(dir, fileName).getPath)
      val op =
        fs2.io.file.Files[IO].delete(loc).map(_ => true)
      Ok(op)

    case GET -> Root / "list-patient-image" :? intPatientId(patientId) =>
      val dir = Config.paperScanDir(patientId)
      val loc = Path(new java.io.File(dir).getPath)
      val op =
        fs2.io.file.Files[IO].list(loc).evalMap(path => 
          fs2.io.file.Files[IO].getBasicFileAttributes(path).map(attr => 
            val ctime = FileInfo.fromTimestamp(attr.creationTime)
            FileInfo(path.fileName.toString, ctime, attr.size)
          )
        ).compile.toList
      Ok(op)
      
    case GET -> Root / "get-patient-image" :? intPatientId(patientId) +& strFileName(fileName) =>
      val dir = Config.paperScanDir(patientId)
      val loc = Path(new java.io.File(dir, fileName).getPath)
      val mediaType: MediaType = 
        if fileName.endsWith(".pdf") then MediaType.application.pdf
        else MediaType.image.jpeg
      val op =
        fs2.io.file.Files[IO].readAll(loc)
      Ok(op, `Content-Type`(mediaType))

  }
