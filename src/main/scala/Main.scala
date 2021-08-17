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
import java.io.File
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContextExecutorService
import scala.concurrent.ExecutionContext

object Main extends IOApp {

  val indexService = HttpRoutes.of[IO] { case GET -> Root =>
    Ok("hello world")
  }

  val apiService = HttpRoutes.of[IO] { case GET -> Root / "api" / "hello" =>
    Ok("api-hello")
  }

  def staticService(blocker: Blocker) = 
    fileService[IO](FileService.Config("./web", blocker, "/static"))

  val app: Resource[IO, Server[IO]] = {
    for {
      server <- BlazeServerBuilder[IO]
        .withSocketReuseAddress(true)
        .withHttpApp(
          (indexService <+> apiService).orNotFound
        )
        .bindHttp(8080, "localhost")
        .resource
    } yield server
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val customBlockingEC: ExecutionContextExecutorService = 
      ExecutionContext.fromExecutorService(
        Executors.newCachedThreadPool()
      )
    val blocker = Blocker.liftExecutionContext(customBlockingEC)
    BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(8080, "localhost")
      .withHttpApp(
          (indexService <+> apiService <+> staticService(blocker)).orNotFound
      )
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
