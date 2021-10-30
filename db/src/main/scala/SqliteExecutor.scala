package dev.myclinic.scala.db

import org.sqlite.SQLiteConfig
import org.sqlite.SQLiteDataSource
import doobie.ConnectionIO
import doobie.ExecutionContexts
import doobie.Transactor
import doobie.implicits._
import javax.sql.DataSource
import cats.effect.IO

object SqliteExecutor:
  val dbfile: String = sys.env.get("MYCLINIC_SQLITE_DB").get
  val url = s"jdbc:sqlite:${dbfile}"

  val config = new SQLiteConfig()
  config.setEncoding(SQLiteConfig.Encoding.UTF8)
  config.enforceForeignKeys(true)
  config.setBusyTimeout(60000)
  val ds = new SQLiteDataSource(config)
  ds.setUrl(url)

  def transactor(ds: DataSource) =
    for
      ce <- ExecutionContexts.fixedThreadPool[IO](32)
    yield Transactor.fromDataSource[IO](ds, ce)

  val xa = transactor(ds)

  def execute[A](op: ConnectionIO[A]): IO[A] =
    xa.use(tx => op.transact(tx))


trait Sqlite {
  def sqlite[A](op: ConnectionIO[A]): IO[A] = SqliteExecutor.execute(op)
}