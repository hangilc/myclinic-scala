package dev.myclinic.scala.masterdb

import java.io.File

import cats.effect.IO
import fs2.Stream
import org.apache.commons.csv.{CSVParser, CSVFormat, CSVRecord}
import java.nio.charset.Charset

object CSVStream:
  def apply(csvFile: File): Stream[IO, CSVRecord] =
    val parser = CSVParser.parse(
      csvFile,
      Charset.forName("MS932"),
      CSVFormat.RFC4180
    )
    Stream.bracket(
      IO{ CSVParser.parse(
      csvFile,
      Charset.forName("MS932"),
      CSVFormat.RFC4180
      ) }
    )(parser => IO{ parser.close })
    .flatMap(parser => {
      Stream
      .unfold(parser.iterator)(i => if i.hasNext then Some(i.next, i) else None)
      .covary[IO]
    })
