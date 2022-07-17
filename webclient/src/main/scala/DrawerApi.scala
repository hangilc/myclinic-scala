package dev.myclinic.scala.webclient

import io.circe._
import io.circe.syntax._
import dev.fujiwara.scala.drawer.Op
import scala.concurrent.Future
import dev.myclinic.scala.webclient.ParamsImplicits.given
import dev.myclinic.scala.model.Text
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import scala.language.implicitConversions

object DrawerApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def shohousenDrawer(textId: Int): Future[List[Op]] =
      get("shohousen-drawer", Params("text-id" -> textId))

    def shohousenDrawerText(text: Text): Future[List[Op]] =
      post("shohousen-drawer-text", Params(), text)

    def createPdfFile(
        ops: List[Op],
        paperSize: String,
        fileName: String
    ): Future[Boolean] =
      post(
        "create-pdf-file",
        Params("paper-size" -> paperSize, "file-name" -> fileName),
        ops
      )

    def stampPdf(
        fileName: String,
        stampName: String
    ): Future[Boolean] =
      get(
        "stamp-pdf",
        Params(
          "file-name" -> fileName,
          "stamp" -> stampName
        )
      )

    def concatPdfFiles(files: List[String], outFile: String): Future[Boolean] =
      post("concat-pdf-files", Params("out-file" -> outFile), files)
