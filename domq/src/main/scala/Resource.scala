package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.MutationObserver
import scala.scalajs.js.Array
import org.scalajs.dom.MutationRecord
import org.scalajs.dom.MutationObserverInit

class Resource:
  import Resource.ClassResourceRemoved
  private var observer: MutationObserver = new MutationObserver(callback _)

  def startObserve(e: HTMLElement): Unit =
    val init = new MutationObserverInit{}
    init.childList = true
    init.subtree = true
    init.attributeFilter = Array(ClassResourceRemoved)
    observer.observe(e, init)

  def endObserve(): Unit =
    observer.disconnect()

  def callback(records: Array[MutationRecord], ob: MutationObserver): Unit =
    records.foreach(record => 
      record.removedNodes.foreach(n =>
        if n.isInstanceOf[HTMLElement] then
          val e = n.asInstanceOf[HTMLElement]
          if e.classList.contains(ClassResourceRemoved) then
            val event = CustomEvent(ClassResourceRemoved, (), false)
            e.dispatchEvent(event)
      )  
    )

object Resource:
  private val ClassResourceRemoved = "domq-resource-removed"

  def markAsResourceEventAcceptor(e: HTMLElement): Unit =
    e.classList.add(ClassResourceRemoved)

  def addRemovedEventHandler(e: HTMLElement, handler: () => Unit): Unit =
    e.addEventListener(ClassResourceRemoved, _ => handler())

case class ResourceCleanups(e: HTMLElement):
  private var cleanups: List[() => Unit] = Nil
  
  Resource.markAsResourceEventAcceptor(e)
  Resource.addRemovedEventHandler(e, () => cleanups.foreach(_()))

  def add(handler: () => Unit): Unit =
    cleanups = handler :: cleanups

