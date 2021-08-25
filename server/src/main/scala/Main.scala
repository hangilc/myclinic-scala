package dev.myclinic.scala.server

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.staticcontent._

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {

  val helloService = HttpRoutes.of[IO] {
    case GET -> Root / "hello" => Ok("api-hello")
  }

  val apiService = helloService <+> AppointService.service

  val staticService = fileService[IO](FileService.Config("./web", "/"))

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(8080, "localhost")
      .withHttpApp(
        Router("/api" -> apiService, "/" -> staticService).orNotFound
      )
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
