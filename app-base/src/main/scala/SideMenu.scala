package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

case class SideMenuItem(label: String, creator: () => HTMLElement)

trait SideMenuService:
  def getElement: HTMLElement
  def init(): Future[Unit] = Future.successful(())
  def onReactivate: Future[Unit] = Future.successful(())
  def dispose(): Unit = ()

class SideMenu(
    main: HTMLElement,
    args: List[(String, () => SideMenuService)]
):
  val ele: HTMLElement = div()
  def invokeByLabel(label: String): Unit =
    items.find(_.label == label).foreach(invoke(_))

  private case class Item(
      label: String,
      link: HTMLElement,
      builder: () => SideMenuService,
      var cache: Option[SideMenuService]
  ):
    def inactivate(): Unit =
      cache.foreach(service => {
        service.getElement.remove()
        link(cls :- "current")
      })
    def activate(): Future[Unit] =
      cache.fold({
        val service = builder()
        val e = service.getElement
        main(e)
        link(cls := "current")
        cache = Some(service)
        service.init()
      })(service => {
        main(service.getElement)
        link(cls := "current")
        service.onReactivate
      })

  private val items: List[Item] = args.map(arg =>
    arg match {
      case (label, builder) => {
        val link = a(label)
        val item = Item(label, link, builder, None)
        link(onclick := (() => invoke(item)))
        item
      }
    }
  )
  items.foreach(item => {
    ele(item.link)
  })
  private var current: Option[Item] = None

  private def invoke(item: Item): Unit =
    current.foreach(item => {
      item.inactivate()
      current = None
    })
    item.activate().onComplete {
      case Success(_)  => current = Some(item)
      case Failure(ex) => ShowMessage.showError(ex.getMessage)
    }
