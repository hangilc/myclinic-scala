package dev.myclinic.scala.db

import cats.effect.IO
import io.circe.Json

trait DbConfigs extends Mysql:
  def getConfig(name: String): IO[Option[Json]] =
    mysql(DbConfigsPrim.getConfig(name).option)

  def updateConfig(name: String, content: Json): IO[Boolean] =
    mysql(DbConfigsPrim.updateConfig(name, content))

  def setConfig(name: String, content: Json): IO[Boolean] =
    val op =
      for
        cur <- DbConfigsPrim.getConfig(name).option
        ok <- cur match {
          case Some(_) => DbConfigsPrim.updateConfig(name, content)
          case None    => DbConfigsPrim.enterConfig(name, content)
        }
      yield ok
    mysql(op)
