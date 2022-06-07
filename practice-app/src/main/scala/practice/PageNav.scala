package dev.myclinic.scala.web.practiceapp.practice

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.LocalEventPublisher
import dev.fujiwara.domq.LocalEventUnsubscriber

trait PageNav:
  def activate(initPage: Int, totalPages: Int): Unit
  def inactivate(): Unit
  def onPageChange(handler: Option[Int] => Unit): Unit

class PageNavEngine(
  gotoFirstLink: HTMLElement,
  gotoPrevLink: HTMLElement,
  gotoNextLink: HTMLElement,
  gotoLastLink: HTMLElement
) extends PageNav:
  private var currentPage: Option[Int] = None
  private var totalPages: Int = 0
  private val pageChangeEvent = LocalEventPublisher[Option[Int]]

  def activate(initPage: Int, totalPages: Int): Unit =
    this.totalPages = totalPages
    this.currentPage = Some(initPage.max(0).min(totalPages - 1))
    triggerPage()

  def inactivate(): Unit =
    totalPages = 0
    currentPage = None
    triggerPage()

  def onPageChange(handler: Option[Int] => Unit): Unit =
    pageChangeEvent.subscribe(handler)

  def triggerPage(): Unit =
    pageChangeEvent.publish(currentPage)





