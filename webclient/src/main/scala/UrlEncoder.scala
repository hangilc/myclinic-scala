package dev.myclinic.scala.webclient

import scala.scalajs.js.URIUtils

object URIEncoder {
  given uriComponentEncoder: URIComponentEncoder = new URIComponentEncoder {
    def encode(src: String): String = {
      URIUtils.encodeURIComponent(src)
    }
  }
}
