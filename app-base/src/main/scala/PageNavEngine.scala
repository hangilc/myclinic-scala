package dev.myclinic.scala.appbase

import dev.fujiwara.domq.LocalEventPublisher
import dev.fujiwara.domq.LocalEventUnsubscriber

class PageNavEngine:
  private var totalPages: Int = 0
  private var currentPage: Option[Int] = None
  private val pageChangedPublisher = LocalEventPublisher[Option[Int]]

  def setTotalPages(totalPages: Int, initPage: Option[Int]): Unit =
    this.totalPages = totalPages
    gotoPage(initPage)

  def onPageChanged(handler: Option[Int] => Unit): LocalEventUnsubscriber =
    pageChangedPublisher.subscribe(handler)

  def gotoPage(page: Option[Int]): Unit =
    val adj = adjustPage(page)
    if adj != currentPage then
      currentPage = adj
      pageChangedPublisher.publish(currentPage)
  
  def isPaging: Boolean = totalPages > 1

  private def adjustPage(page: Option[Int]): Option[Int] =
    page.flatMap(p => 
      if totalPages == 0 then None
      else Some(p.max(0).min(totalPages - 1))
    )

