package dev.myclinic.scala.chrome

import org.http4s.Uri

object Config:
  val baseUrl = Uri(
    Some(Uri.Scheme.http),
    Some(Uri.Authority(
      host = Uri.RegName("localhost"),
      port = Some(8080)
    )))

  lazy val headless: Boolean =
    val envVal: String = System.getenv("MYCLINIC_CHROME_HEADLESS")
    envVal == null || envVal.toUpperCase == "TRUE"