package dev.myclinic.scala.masterdb

import cats.effect.*
import java.time.LocalDate
import dev.myclinic.scala.db.Db
import java.io.File
import MasterDbConsts.*

object Main extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    args match {
      case "set-shinryou-valid-upto" :: validUptoArg :: _ =>
        {
          val validUpto = LocalDate.parse(validUptoArg)
          for affected <- Db.setShinryouMasterValidUpto(validUpto)
          yield
            println(affected)
            affected
        }.as[ExitCode](ExitCode.Success)

      case "set-iyakuhin-valid-upto" :: validUptoArg :: _ =>
        {
          val validUpto = LocalDate.parse(validUptoArg)
          for affected <- Db.setIyakuhinMasterValidUpto(validUpto)
          yield
            println(affected)
            affected
        }.as[ExitCode](ExitCode.Success)

      case "set-kizai-valid-upto" :: validUptoArg :: _ =>
        {
          val validUpto = LocalDate.parse(validUptoArg)
          for affected <- Db.setKizaiMasterValidUpto(validUpto)
          yield
            println(affected)
            affected
        }.as[ExitCode](ExitCode.Success)

      case "install-shinryou" :: validFromArg :: masterCSV :: _ =>
        {
          val validFrom = LocalDate.parse(validFromArg)
          CSVStream(new File(masterCSV))
            .map(ShinryouMasterCSV.from(_))
            .filter(rec =>
              rec.kubun != HenkouKubunHaishi && rec.kubun != HenkouKubunMasshou
            )
            .map(rec => rec.toMaster(validFrom))
            .evalMap(m => Db.enterShinryouMaster(m))
            .compile
            .drain
        }.as[ExitCode](ExitCode.Success)

      case "install-kizai" :: validFromArg :: masterCSV :: _ =>
        {
          val validFrom = LocalDate.parse(validFromArg)
          CSVStream(new File(masterCSV))
            .map(KizaiMasterCSV.from(_))
            .filter(rec =>
              rec.kubun != HenkouKubunHaishi && rec.kubun != HenkouKubunMasshou
            )
            .map(rec => rec.toMaster(validFrom))
            .filter(_.kingakuStore.size <= 10)
            // .evalTap(m => IO {
            //   if m.kingakuStore.size > 10 then println(m)
            // })
            .evalMap(m => Db.enterKizaiMaster(m))
            .compile
            .drain
        }.as[ExitCode](ExitCode.Success)
      
      case "help" :: _ => (IO { usage }).as[ExitCode](ExitCode.Success)
      case _ => (IO { usage }).as[ExitCode](ExitCode.Error)
    }

def usage: Unit =
  System.err.println("usage: masterDb set-shinryou-valid-upto VALID-UPTO")
  System.err.println("usage: masterDb set-iyakuhin-valid-upto VALID-UPTO")
  System.err.println("usage: masterDb set-kizai-valid-upto VALID-UPTO")
  System.err.println("usage: masterDb install-shinryou VALID-FROM MASTER.CSV")
  System.err.println("usage: masterDb install-kizai VALID-FROM MASTER.CSV")
  System.err.println("usage: masterDb help")
