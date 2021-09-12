package dev.myclinic.scala.server

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._

object RestService {

  val routes = HttpRoutes.of[IO] {
    
    case GET -> Root / "hello" => Ok("hello, world")

  }

}