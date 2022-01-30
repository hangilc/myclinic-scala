package dev.myclinic.scala.webclient

import scala.concurrent.ExecutionContext
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits

given global: ExecutionContext = Implicits.global