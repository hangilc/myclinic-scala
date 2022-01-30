package dev.myclinic.scala.webclient

import scala.concurrent.ExecutionContext
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits

val global: ExecutionContext = Implicits.global