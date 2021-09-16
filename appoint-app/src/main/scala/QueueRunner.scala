package dev.myclinic.scala.web.appoint

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.timers.SetTimeoutHandle
import scala.util.Success
import scala.util.Failure
import scalajs.js.timers.setTimeout

class QueueRunner():
  trait Action:
    def start(): Future[Unit]
    def onComplete(success: Boolean): Unit


  private val queue = new js.Array[Action]()
  private var currentWorker: Option[SetTimeoutHandle] = None

  def enqueue(action: Action): Unit =
    queue.push(action)

  def start(): Unit = startWorker()

  private def startWorker(): Unit =
    if currentWorker.isEmpty && !queue.isEmpty then
      currentWorker = Some(createWorker(queue.shift()))

  private def createWorker(action: Action): SetTimeoutHandle = setTimeout(0) {
    def reportError(msg: String): Unit = System.err.println(msg)

    def finish(): Unit =
      currentWorker = None
      startWorker()

    def onError(ex: Throwable): Unit =
      reportError(ex.toString())
      action.onComplete(false)
      finish()

    def onSuccess(): Unit =
      action.onComplete(true)
      finish()
    
    try
      action.start().onComplete {
        case Success(_) => onSuccess()
        case Failure(ex) => onError(ex)
      }
    catch
      case ex: Throwable => onError(ex)
  }
