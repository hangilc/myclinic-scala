package dev.myclinic.scala.webclient

import org.scalajs.dom.FormData
import scala.concurrent.Future
import org.scalajs.dom.RequestInit
import org.scalajs.dom.HttpMethod
import ParamsImplicits.given
import scala.language.implicitConversions

object FileApi extends FetchBase:
  def baseUrl = "/api/"

  trait Api:
    def uploadPatientImage(patientId: Int, data: FormData): Future[Boolean] =
      val init = new RequestInit{}
      init.method = HttpMethod.POST
      init.body = data
      doFetch("upload-patient-image", Params("patient-id" -> patientId), init)

    def deletePortalTmpFile(fileName: String): Future[Boolean] =
      val init = new RequestInit{}
      init.method = HttpMethod.GET
      doFetch("delete-portal-tmp-file", Params("file-name" -> fileName), init)

    def getWebphoneToken(): Future[String] =
      val init = new RequestInit{}
      init.method = HttpMethod.GET
      doFetch("get-webphone-token", Params(), init)
      