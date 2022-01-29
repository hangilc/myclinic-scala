package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.*

trait Updatable[M]:
  def update(orig: M, appModelEvent: AppModelEvent): M

object Updatable:
  given updatableList[M](using u: Updatable[M]): Updatable[List[M]] with
    def update(orig: List[M], e: AppModelEvent): List[M] =
      orig.map(m => u.update(m, e))

  given updatableMap[K, M](using u: Updatable[M]): Updatable[Map[K, M]] with
    def update(orig: Map[K, M], e: AppModelEvent): Map[K, M] =
      orig.map((k, v) => (k, u.update(v, e)))

  given updatableAppoinTime: Updatable[AppointTime] with
    def update(orig: AppointTime, e: AppModelEvent): AppointTime =
      e match {
        case AppointTimeUpdated(_, updated) if updated.appointTimeId == orig.appointTimeId => updated
        case _ => orig
      }


case class GenModel[M: Updatable](gen: Int, model: M):
  def map[T: Updatable](f: M => T): GenModel[T] = GenModel(gen, f(model))

