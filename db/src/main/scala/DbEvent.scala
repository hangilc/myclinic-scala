package dev.myclinic.scala.db

import cats.effect.IO

trait DbEvent extends Sqlite {

  def nextGlobalEventId(): IO[Int] = {
    sqlite(DbEventPrim.nextGlobalEventId())
  }

}