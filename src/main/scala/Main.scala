package dev.myclinic.scala

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.blaze._
import org.http4s.implicits._
import scala.concurrent.ExecutionContext.Implicits.global
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {
  val restService = HttpRoutes.of[IO] { case GET -> Root =>
    Ok("hello")
  }

  def run(args: List[String]): IO[ExitCode] = {
    val httpApp = Router("/" -> restService).orNotFound
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
