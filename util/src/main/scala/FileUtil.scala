package dev.myclinic.scala.util

object FileUtil:
  private val extPattern = """.*\.([a-zA-Z0-9]+)$""".r

  def findFileExtension(filename: String): Option[String] =
    filename match {
      case extPattern(ext) => Some(ext)
      case _ => None
    }
