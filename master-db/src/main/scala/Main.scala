package dev.myclinic.scala.masterdb

import cats.effect.*

object Main extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    args match {
      case "update-shinryou" :: rest => 
        Update.updateShinryou().as[ExitCode](ExitCode.Success)
      case _ => (IO { usage }).as[ExitCode](ExitCode.Error)
    }

def usage: Unit =
  System.err.println("usage: masterDb update-shinryou [go]")


