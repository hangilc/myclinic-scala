package dev.myclinic.scala

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.blaze._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.staticcontent._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {
  // val blocker = Blocker[IO]
  // val restService = HttpRoutes.of[IO] { 
  //   case GET -> Root => Ok("hello")
  //   case GET -> Root / "static" => fileService(FileService.Config("./web", blocker))
  // }

  // def run(args: List[String]): IO[ExitCode] = {
  //   val httpApp = Router("/" -> restService).orNotFound
  //   BlazeServerBuilder[IO](global)
  //     .bindHttp(8080, "localhost")
  //     .withHttpApp(httpApp)
  //     .serve
  //     .compile
  //     .drain
  //     .as(ExitCode.Success)
  // }

  // def restService(blocker: Blocker) = HttpRoutes.of[IO] { 
  //   //case GET -> Root => Ok("hello")
  //   case GET -> Root / "static" => fileService(FileService.Config("./web", blocker))
  // }

  // def httpApp(blocker: Blocker) = Router("/" -> restService(blocker)).orNotFound

  val app: Resource[IO, Server[IO]] = {
    for {
        blocker <- Blocker[IO]
        server <- BlazeServerBuilder[IO]
          .bindHttp(8080, "localhost")
          .withHttpApp(fileService[IO](FileService.Config("./web", blocker)).orNotFound)
          .resource
    } yield server
  }

  override def run(args: List[String]): IO[ExitCode] = {
    app.use(_ => IO.never).as(ExitCode.Success)
  }
}
