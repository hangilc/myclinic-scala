package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement}
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Success
import scala.util.Failure

trait SideMenuService:
  def getElement: HTMLElement = div
  def getElements: List[HTMLElement] = List(getElement)
  def init(): Future[Unit] = Future.successful(())
  def onReactivate: Future[Unit] = Future.successful(())
  def dispose(): Unit = ()

trait SideMenuProcs

class SideMenu(
    wrapper: HTMLElement,
    args: List[(String, SideMenuProcs => SideMenuService)]
):
  val ele: HTMLElement = div()
  private val procs: SideMenuProcs = new AnyRef with SideMenuProcs
  private var current: Option[SideMenuItem] = None
  private var items: List[SideMenuItem] = List.empty

  addItems(args)

  def addItem(label: String, builder: SideMenuProcs => SideMenuService): Unit =
    val link = a(label)
    val item = SideMenuItem(label, link, builder)
    link(onclick := (() => invoke(item)))
    ele(item.link)
    items = items :+ item

  def addItems(items: List[(String, SideMenuProcs => SideMenuService)]): Unit =
    items.foreach((addItem _).tupled)

  def invokeByLabel(label: String): Unit =
    items.find(_.label == label).foreach(invoke(_))

  private def clearCurrent(): Unit =
    current.foreach(item => {
      item.inactivate()
      current = None
    })

  private def invoke(item: SideMenuItem): Unit =
    clearCurrent()
    item.activate(wrapper, procs).onComplete {
      case Success(_)  => current = Some(item)
      case Failure(ex) => 
        ex.printStackTrace()
        ShowMessage.showError(ex.getMessage)
    }

object SideMenu:
  def apply(
      wrapper: HTMLElement,
      args: List[(String, () => SideMenuService)] = List.empty
  ): SideMenu =
    new SideMenu(
      wrapper,
      args.map { case (label, f) =>
        (label, _ => f())
      }
    )

case class SideMenuItem(
    label: String,
    link: HTMLElement,
    builder: SideMenuProcs => SideMenuService
):
  var cache: Option[(SideMenuService, List[HTMLElement])] = None
  def inactivate(): Unit =
    cache.foreach { case (service, eles) =>
      eles.foreach(e => DomUtil.hook(e))
    }
    link(cls :- "current")

  def activate(wrapper: HTMLElement, procs: SideMenuProcs): Future[Unit] =
    cache.fold({
      val service = builder(procs)
      val eles = service.getElements
      eles.foreach(wrapper(_))
      link(cls := "current")
      cache = Some(service, eles)
      service.init()
    }) { case (service, eles) =>
      eles.foreach(wrapper(_))
      link(cls := "current")
      service.onReactivate
    }

object MockSideMenuService:
  def apply(label: String = "MOCK"): SideMenuService =
    new SideMenuService:
      override def getElement = div(label)
