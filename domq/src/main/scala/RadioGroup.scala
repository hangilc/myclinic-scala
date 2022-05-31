package dev.fujiwara.domq

case class RadioGroup[T](name: String, items: List[(String, T)]):
  val radioLabels: List[RadioLabel[T]] = items.map {
    case (label, value) => RadioLabel(name, value, label)
  }
