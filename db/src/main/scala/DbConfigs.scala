package dev.myclinic.scala.db

import cats.effect.IO
import io.circe.Json

trait DbConfigs extends Mysql:
  def getConfig(name: String): IO[Option[Json]] = 
    mysql(DbConfigsPrim.getConfig(name).option)

  def setConfig(name: String, content: Json): IO[Boolean] =
    mysql(DbConfigsPrim.setConfig(name, content))