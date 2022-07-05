package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.ValidatedResult
import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import scala.language.implicitConversions

case class ModelProp(label: String)

object ModelProp:

  type Label[P] = P match {
    case ModelProp => String
  }

  def fLabel[P](p: P): Label[P] = p match {
    case pp: ModelProp => pp.label
  }

  def labels(props: Tuple): Tuple.Map[props.type, Label] =
    props.map[Label]([T] => (t: T) => fLabel(t))

  def labelsAsList(props: Tuple): List[String] =
    labels(props).toList.map(a => a.asInstanceOf[String])

