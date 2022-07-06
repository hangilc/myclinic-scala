package dev.fujiwara.domq

trait ModelPropUtil:
  def tupleToList[T](tuple: Tuple): List[T] =
    tuple match {
      case EmptyTuple => List.empty
      case h *: t => h.asInstanceOf[T] :: tupleToList[T](t)
    }