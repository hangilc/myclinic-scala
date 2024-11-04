package dev.myclinic.scala.db

import doobie._
import doobie.implicits._
import doobie.util.log.LogEvent
import cats.effect.IO
import java.util.Properties

object MysqlExecutor:

  private val user = sys.env.get("MYCLINIC_DB_USER").get
  private val pass = sys.env.get("MYCLINIC_DB_PASS").get
  private val host = sys.env.get("MYCLINIC_DB_HOST").getOrElse("localhost")
  private val port = sys.env.get("MYCLINIC_DB_PORT").getOrElse(3306)

  private var url = s"jdbc:mysql://${host}:${port}/myclinic"
  url += "?zeroDateTimeBehavior=convertToNull"
  url += "&noDatetimeStringSync=true"
  url += "&useUnicode=true"
  url += "&characterEncoding=utf8"
  // url += "&serverTimezone=JST"
  url += "&serverTimezone=Asia/Tokyo"
  url += "&useSSL=false"

  println("jdbc-utl: " + url)

  val props = new Properties()
  props.setProperty("user", user)
  props.setProperty("password", pass)

  val printSqlLogHandler: LogHandler[IO] = new LogHandler[IO] {
    def run(logEvent: LogEvent): IO[Unit] =
      IO {
        println(logEvent.sql)
      }
  }

  val xa = Transactor.fromDriverManager[IO](
    "com.mysql.cj.jdbc.Driver",
    url,
    props,
    None // Some(printSqlLogHandler)
  )

  def execute[A](sql: ConnectionIO[A]): IO[A] =
    sql.transact(xa)

trait Mysql:
  def mysql[A](op: ConnectionIO[A]): IO[A] = MysqlExecutor.execute(op)
