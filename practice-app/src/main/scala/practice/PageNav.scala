package dev.myclinic.scala.web.practiceapp.practice

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.LocalEventPublisher

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

  gotoFirstLink(onclick := (gotoFirst _))
  gotoPrevLink(onclick := (gotoPrev _))
  gotoNextLink(onclick := (gotoNext _))
  gotoLastLink(onclick := (gotoLast _))

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

  private def gotoPage(page: Int): Unit =
    if page >= 0 && page < totalPages - 1 then
      currentPage = Some(page)
      triggerPage()

  def gotoFirst(): Unit =
    gotoPage(0)

  def gotoPrev(): Unit =
    currentPage.foreach(page => gotoPage(page - 1))

  def gotoNext(): Unit =
    currentPage.foreach(page => gotoPage(page + 1))

  def gotoLast(): Unit =
    gotoPage(totalPages - 1)






