package dev.fujiwara.domq

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.ValidUpto
import org.scalajs.dom.HTMLElement

trait ModelRep[M]:
  trait Rep:
    def rep(mOpt: Option[M]): String

trait ModelReps[M]:
  this: ModelRep[M] =>

  class ModelSimpleRep[T](
    modelValue: M => T,
    noneValue: () => String = () => ""
  ) extends Rep:
    def rep(mOpt: Option[M]): String =
      mOpt.fold(noneValue())(modelValue(_).toString)

  class ModelConvertRep[T](
    modelValue: M => T,
    conv: T => String,
    noneValue: () => String = () => ""
  ) extends Rep:
    def rep(mOpt: Option[M]): String =
      mOpt.fold(noneValue())(m => conv(modelValue(m)))

  class ModelDateRep(
    modelValue: M => LocalDate,
    conv: LocalDate => String = (d: LocalDate) => KanjiDate.dateToKanji(d),
    noneValue: () => String = () => ""
  ) extends ModelConvertRep[LocalDate](modelValue, conv, noneValue)

  class ModeValidUptoRep(
    modelValue: M => ValidUpto,
    conv: LocalDate => String = (d: LocalDate) => KanjiDate.dateToKanji(d),
    noneValue: () => String = () => "（期限なし）",
  ) extends Rep:
    def rep(mOpt: Option[M]): String =
      mOpt.fold(noneValue())(m => modelValue(m).value.fold(noneValue())(conv(_)))

trait ModelRepOps[M]:
  this: ModelRep[M] =>

  def create(props: Tuple): Tuple

  def tupleToList[T](tuple: Tuple): List[T] =
  tuple match {
    case EmptyTuple => List.empty
    case h *: t => h.asInstanceOf[T] :: tupleToList[T](t)
  }

  type RepString[P] = P match {
    case Rep => String
  }

  def fRepString[P](p: P, modelOpt: Option[M]): RepString[P] = p match {
    case pp: Rep => pp.rep(modelOpt)
  }

  def repStrings(reps: Tuple, modelOpt: Option[M]): Tuple.Map[reps.type, RepString] =
    reps.map([T] => (t: T) => fRepString(t, modelOpt))

  def repStringAsList(reps: Tuple, modelOpt: Option[M]): List[String] =
    tupleToList[String](repStrings(reps, modelOpt))

  // def labelRepPairs(
  //     props: Tuple,
  //     modelOpt: Option[M]
  // ): List[(String, String)] =
  //   val reps = create(props)
  //   val ls = ModelProp.labelsAsList(props)
  //   val rs = repStringAsList(reps, modelOpt)
  //   ls.zip(rs)

  // type DispPanel[P] = P match {
  //   case Rep => HTMLElement
  // }

  // def dispPanel(
  //   props: Tuple,
  //   modelOpt: Option[M]
  // ): HTMLElement =
  //   val reps = create(props)


