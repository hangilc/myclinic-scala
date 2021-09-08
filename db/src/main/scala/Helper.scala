package dev.myclinic.scala.db

import cats.implicits._
import doobie._

object Helper {

  def confirm(b: Boolean, msg: => String): ConnectionIO[Unit] = {
    if( !b ){
      throw new RuntimeException(msg)
    }
    ().pure[ConnectionIO]
  }

  def confirmUpdate(err: => String): Int => ConnectionIO[Unit] = {
    affected => confirm(affected == 1, err)
  }

}
