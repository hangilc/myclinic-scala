package dev.myclinic.scala.client

import scala.scalajs.js.URIUtils

object URIEncoder {
  implicit val uriComponentEncoder: URIComponentEncoder = new URIComponentEncoder {
    def encode(src: String): String = {
      URIUtils.encodeURIComponent(src)
    }
  }
}