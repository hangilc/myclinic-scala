package dev.myclinic.scala.web.appbase

import cats.implicits.*
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Sex
import cats.data.Validated
import cats.data.Validated.*
import dev.fujiwara.validator.ValidatorUtil.*

object SexValidator:
  sealed class SexError(msg: String) extends ValidationError:
    def message: String = msg

  object EmptySexError extends SexError("性別が入力されていません。")
  object InvalidSexError extends SexError("性別の入力が不適切です。")

  def validateSexInput(input: Option[String]): Validated[List[SexError], Sex] =
    isSome(input, EmptySexError)
      .andThen(isNotEmpty(_, EmptySexError))
      .andThen {
          case "M" | "男" => Valid(Sex.Male)
          case "F" | "女" => Valid(Sex.Female)
          case _         => Invalid(List(InvalidSexError))
      }
