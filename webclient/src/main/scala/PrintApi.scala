package dev.myclinic.scala.webclient

import scala.concurrent.Future
import dev.myclinic.scala.webclient.ParamsImplicits.given
import scala.language.implicitConversions
import dev.fujiwara.scala.drawer.PrintRequest
import io.circe.*
import io.circe.syntax.*
import io.circe.Decoder
import dev.myclinic.scala.model.ScannerDevice
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import scala.scalajs.js.typedarray.ArrayBuffer

object PrintApi extends ApiBase:
  def baseUrl: String = "http://localhost:48080/"

  trait Api:
    given Decoder[Option[String]] = Decoder.decodeOption[String]

    def beep(): Future[Unit] =
      get("beep", Params())

    def listPrintSetting(): Future[List[String]] =
      get("setting/", Params())

    def getPrintPref(kind: String): Future[Option[String]] =
      get("pref/" + kind, Params())

    def setPrintPref(kind: String, pref: String): Future[Option[String]] =
      post("pref/" + kind, Params(), pref)

    def deletePrintPref(kind: String): Future[Option[String]] =
      delete("pref/" + kind, Params())

    def printDrawer(
        req: PrintRequest,
        setting: Option[String]
    ): Future[Boolean] =
      val sub: String = setting.getOrElse("")
      post("print/" + sub, Params(), req)

    def listScannerDevices(): Future[List[ScannerDevice]] =
      get("scanner/device/", Params())

    def scan(
        deviceId: String,
        progress: (Double, Double) => Unit,
        resolution: Int = 100
    ): Future[String] =
      get(
        "scanner/scan",
        Params("device-id" -> deviceId, "resolution" -> resolution),
        progress = Some(progress),
        resultHandler = Some(xhr => {
          xhr.getResponseHeader("x-saved-image")
        })
      )

    def getScannedFile(savedFile: String): Future[ArrayBuffer] =
      val req = new BinaryDownloadRequest
      val url = s"${baseUrl}scanner/image/${savedFile}"
      req.send("GET", url)
