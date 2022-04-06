package dev.myclinic.scala.masterdb

import cats.effect.*
import java.time.LocalDate
import dev.myclinic.scala.db.Db

object Main extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    args match {
      case "set-shinryou-valid-upto" :: validUptoArg :: _ => {
        val validUpto = LocalDate.parse(validUptoArg)
        for
          affected <- Db.setShinryouMasterValidUpto(validUpto)
        yield 
          println(affected)
          affected
      }.as[ExitCode](ExitCode.Success)
      case "set-iyakuhin-valid-upto" :: validUptoArg :: _ => {
        val validUpto = LocalDate.parse(validUptoArg)
        for
          affected <- Db.setIyakuhinMasterValidUpto(validUpto)
        yield 
          println(affected)
          affected
      }.as[ExitCode](ExitCode.Success)
      case "set-kizai-valid-upto" :: validUptoArg :: _ => {
        val validUpto = LocalDate.parse(validUptoArg)
        for
          affected <- Db.setKizaiMasterValidUpto(validUpto)
        yield 
          println(affected)
          affected
      }.as[ExitCode](ExitCode.Success)
      case _ => (IO { usage }).as[ExitCode](ExitCode.Error)
    }

def usage: Unit =
  System.err.println("usage: masterDb set-shinryou-valid-upto VALID-UPTO")
  System.err.println("usage: masterDb set-iyakuhin-valid-upto VALID-UPTO")
  System.err.println("usage: masterDb set-kizai-valid-upto VALID-UPTO")


