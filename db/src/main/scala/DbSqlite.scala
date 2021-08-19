package dev.myclinic.scala.db

import doobie._
import doobie.implicits._
import cats.effect.IO

object DbSqlite {
  val dbfile: String = sys.env.get("MYCLINIC_SQLITE_DB").get
  val url = s"jdbc:sqlite:${dbfile}"

  val xa = Transactor.fromDriverManager[IO](
    "org.sqlite.JDBC",
    url,
    "",
    ""
  )

  def exec[A](q: ConnectionIO[A]): IO[A] = {
    q.transact(xa)
  }
}