package dev.myclinic.scala.db
import doobie._
import doobie.implicits.*
import io.circe.Json
import io.circe.parser.*
import io.circe.syntax.*
import doobie.util.query.Query0

object DbConfigsPrim:
  
  def getConfig(name: String): Query0[Json] =
    sql"""
      select content from configs where name = ${name}
    """.query[String].map(parse(_).getOrElse(Json.Null))

  def updateConfig(name: String, content: Json): ConnectionIO[Boolean] =
    sql"""
      update configs set content = ${content.toString} where name = ${name}
    """.update.run.map(_ == 1)

  def enterConfig(name: String, content: Json): ConnectionIO[Boolean] =
    sql"""
      insert into configs (name, content) values (${name}, ${content.toString()})
    """.update.run.map(_ == 1)