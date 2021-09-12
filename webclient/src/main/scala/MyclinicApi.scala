package dev.myclinic.scala.webclient

import sttp.client3._
import sttp.tapir.client.sttp.SttpClientInterpreter
import dev.myclinic.scala.api.MyclinicEndpoints

object MyclinicApi {
  val hello = SttpClientInterpreter().toQuickClient(
    MyclinicEndpoints.helloEndpoint,
    Some(uri"http://localhost:8080/rest")
  )
}
