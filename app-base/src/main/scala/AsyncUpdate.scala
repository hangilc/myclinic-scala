package dev.myclinic.scala.web.appbase

import scala.concurrent.Future
import dev.myclinic.scala.model.AppModelEvent
import dev.myclinic.scala.webclient.global

object AsyncUpdate:
  def asyncUpdate(
      f: () => Future[Int],
      h: (Int, AppModelEvent) => Unit,
      e: () => Unit
  )(using fetcher: EventFetcher): Future[Unit] =
    for gen <- f()
    yield
      fetcher.catchup(gen, h)
      e()
