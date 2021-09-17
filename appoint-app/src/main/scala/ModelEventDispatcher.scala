package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.web.appoint.Events.ModelEvent

object ModelEventDispatcher:
  type Handler = ModelEvent => Unit
  
  var listeners = Set[Handler]()

  def addHandler(handler: Handler): Unit =
    listeners += handler
  
  def removeHandler(handler: Handler): Unit =
    listeners -= handler

  def dispatch(modelEvent: ModelEvent): Unit =
    listeners.foreach(handler => {
      try
        handler(modelEvent)
      catch
        case t: Throwable => System.err.println(t)
    })
  