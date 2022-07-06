package dev.fujiwara.domq.prop

trait ModelUtil:
  def tupleToList[T](tuple: Tuple): List[T] =
    tuple match {
      case EmptyTuple => List.empty
      case h *: t => h.asInstanceOf[T] :: tupleToList[T](t)
    }