package dev.myclinic.scala.config

import java.io.File
import java.io.Reader
import io.circe.Decoder
import io.circe.Decoder.Result
import io.circe.Encoder
import io.circe.HCursor
import io.circe.generic.semiauto._
import io.circe.syntax.*
import io.circe.yaml.parser
import java.nio.file.Path
import java.nio.file.Files

trait FileConfigurator:
  def readYaml[T: Decoder](file: File): T =
    val reader: Reader = _root_.java.io.FileReader(file)
    try
      parser
        .parse(reader)
        .flatMap(_.as[T])
        .getOrElse(
          throw new RuntimeException("Failed to read: " + file.toString)
        )
    finally reader.close()

  def fileContent(path: Path): String =
    Files.readString(path)

