package dev.myclinic.scala.db

import cats._
import cats.data._
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import org.sqlite.SQLiteDataSource
import org.sqlite.SQLiteConfig
import javax.sql.DataSource

object DbSqlite {
  val dbfile: String = sys.env.get("MYCLINIC_SQLITE_DB").get
  val url = s"jdbc:sqlite:${dbfile}"

  val config = new SQLiteConfig()
  config.setEncoding(SQLiteConfig.Encoding.UTF8)
  val ds = new SQLiteDataSource(config)
  ds.setUrl(url)

  def transactor(ds: DataSource) = {
    for{
      ce <- ExecutionContexts.fixedThreadPool[IO](32)
    } yield Transactor.fromDataSource[IO](ds, ce)
  }

  val xa = transactor(ds)
    
  // val xa2 = Transactor.fromDriverManager[IO](
  //   "org.sqlite.JDBC",
  //   url,
  //   "",
  //   ""
  // )

  def exec[A](q: ConnectionIO[A]): IO[A] = {
    xa.use(tx => q.transact(tx))
  }

}