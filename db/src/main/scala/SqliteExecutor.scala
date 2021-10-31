package dev.myclinic.scala.db

import org.sqlite.SQLiteConfig
import org.sqlite.SQLiteDataSource
import doobie.ConnectionIO
import doobie.ExecutionContexts
import doobie.Transactor
import doobie.implicits._
import javax.sql.DataSource
import cats.effect.IO
import java.sql.Connection
import java.sql.DriverManager

object SqliteExecutor:
  val dbfile: String = sys.env.get("MYCLINIC_SQLITE_DB").get
  val url = s"jdbc:sqlite:${dbfile}"

  val config = new SQLiteConfig()
  config.setEncoding(SQLiteConfig.Encoding.UTF8)
  config.enforceForeignKeys(true)
  config.setBusyTimeout(60000)
  config.setTransactionMode(SQLiteConfig.TransactionMode.IMMEDIATE)
  val ds = new SQLiteDataSource(config)
  ds.setUrl(url)

  val ec = ExecutionContexts.fixedThreadPool[IO](6)

  def execute[A](op: ConnectionIO[A]): IO[A] =
    ec.use(c => {
      val xa = Transactor.fromDataSource[IO](ds, c)
      op.transact(xa)
    })

trait Sqlite {
  def sqlite[A](op: ConnectionIO[A]): IO[A] = SqliteExecutor.execute(op)
}