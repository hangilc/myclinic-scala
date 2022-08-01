package dev.myclinic.scala.chrome

object Config:
  val baseUrl = "http://localhost:8080"

  lazy val headless: Boolean =
    val envVal: String = System.getenv("MYCLINIC_CHROME_HEADLESS")
    envVal == null || envVal.toUpperCase != "FALSE"