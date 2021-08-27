package dev.myclinic.scala.client

import scala.concurrent.Future

trait HttpClient {
  def get[R](url: String): Future[R]
  def post[R](url: String, jsonBody: String): Future[R]
}