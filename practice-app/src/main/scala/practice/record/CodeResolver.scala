package dev.myclinic.scala.web.practiceapp.practice.record

import cats.data.EitherT
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.*
import java.time.LocalDate

object CodeResolver:
  def resolveShinryoucode(origCode: Int, at: LocalDate): EitherT[Future, String, Int] =
    EitherT.fromOptionF(
      Api.resolveShinryoucode(origCode, at),
      s"有効な診療コードを見つけられませんでした。（${origCode}）"
    )

  def resolveShinryoucodeByName(name: String, at: LocalDate): EitherT[Future, String, Int] =
    EitherT.fromOptionF(
      Api.resolveShinryoucodeByName(name, at),
      s"有効な診療コードを見つけられませんでした。（${name}）"
    )

  def resolveIyakuhincode(origCode: Int, at: LocalDate): EitherT[Future, String, Int] =
    EitherT.fromOptionF(
      Api.resolveIyakuhincode(origCode, at),
      s"有効な医薬品コードを見つけられませんでした。（${origCode}）"
    )

  def resolveKizaicode(origCode: Int, at: LocalDate): EitherT[Future, String, Int] =
    EitherT.fromOptionF(
      Api.resolveKizaicode(origCode, at),
      s"有効な器材コードを見つけられませんでした。（${origCode}）"
    )

  def resolveKizaicodeByName(name: String, at: LocalDate): EitherT[Future, String, Int] =
    EitherT.fromOptionF(
      Api.resolveKizaicodeByName(name, at),
      s"有効な器材コードを見つけられませんでした。（${name}）"
    )


