package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import scala.language.implicitConversions

trait NavUI:
  def gotoFirstLink: Option[HTMLElement]
  def gotoPrevLink: Option[HTMLElement]
  def gotoNextLink: Option[HTMLElement]
  def gotoLastLink: Option[HTMLElement]
  def infoWrapper: Option[HTMLElement]
  def ele: HTMLElement

class Nav(ui: NavUI):
  val engine = new NavEngine

  ui.gotoFirstLink.foreach(_(onclick := (() => engine.gotoFirst())))
  ui.gotoPrevLink.foreach(_(onclick := (() => engine.gotoPrev())))
  ui.gotoNextLink.foreach(_(onclick := (() => engine.gotoNext())))
  ui.gotoLastLink.foreach(_(onclick := (() => engine.gotoLast())))
  ui.ele(displayNone)
  engine.onInfoChanged(info => ui.infoWrapper.foreach(_(clear, info)))
  engine.onPagingChanged(isPaging => 
    if isPaging then ui.ele(displayDefault) else ui.ele(displayNone)  
  )

  def init(totalPages: Int, initPage: Option[Int]) =
    engine.setTotalPages(totalPages, initPage)

  def onPageChanged(handler: Option[Int] => Unit): Unit =
    engine.onPageChanged(handler)

class NavEngine:
  private var _totalPages: Int = 0
  private var _currentPage: Option[Int] = None
  private val pageChangedPublisher = LocalEventPublisher[Option[Int]]
  private val pagingChangedPublisher = LocalEventPublisher[Boolean]
  private val infoChangedPublisher = LocalEventPublisher[String]

  def setTotalPages(totalPages: Int, initPage: Option[Int]): Unit =
    _totalPages = totalPages
    _currentPage = adjustPage(initPage)
    pageChangedPublisher.publish(_currentPage)
    pagingChangedPublisher.publish(isPaging)
    infoChangedPublisher.publish(info)

  def onPageChanged(handler: Option[Int] => Unit): LocalEventUnsubscriber =
    pageChangedPublisher.subscribe(handler)

  def onPagingChanged(handler: Boolean => Unit): LocalEventUnsubscriber =
    pagingChangedPublisher.subscribe(handler)

  def onInfoChanged(handler: String => Unit): LocalEventUnsubscriber =
    infoChangedPublisher.subscribe(handler)

  def gotoPage(page: Option[Int]): Unit =
    val adj = adjustPage(page)
    if adj != _currentPage then
      _currentPage = adj
      pageChangedPublisher.publish(_currentPage)
      infoChangedPublisher.publish(info)

  def totalPages: Int = _totalPages
  def currentPage: Option[Int] = _currentPage
  def info: String =
    _currentPage.map(page => s"${page + 1}/${_totalPages}").getOrElse("")

  def isPaging: Boolean = _totalPages > 1

  def gotoFirst(): Unit = gotoPage(Some(0))
  def gotoPrev(): Unit = gotoPage(currentPage.map(_ - 1))
  def gotoNext(): Unit = gotoPage(currentPage.map(_ + 1))
  def gotoLast(): Unit = gotoPage(Some(totalPages - 1))

  private def adjustPage(page: Option[Int]): Option[Int] =
    page.flatMap(p =>
      if _totalPages == 0 then None
      else Some(p.max(0).min(_totalPages - 1))
    )

object NavEngine:
  def calcNumPages(total: Int, itemsPerPage: Int): Int =
    (total + itemsPerPage - 1) / itemsPerPage

