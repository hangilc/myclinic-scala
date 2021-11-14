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

class SideMenu(
    main: HTMLElement,
    args: List[(String, () => Future[HTMLElement])]
):
  val ele: HTMLElement = div()

  private case class Item(
      label: String,
      link: HTMLElement,
      creator: () => Future[HTMLElement],
      var cache: Option[HTMLElement]
  ):
    def inactivate(): Unit =
      cache.foreach(e => {
        e.remove()
        link(cls :- "current")
      })
    def activate(): Future[Unit] =
      cache.fold({
        for e <- creator()
        yield {
          cache = Some(e)
          main(e)
          link(cls := "current")
          ()
        }
      })(e => {
        main(e)
        link(cls := "current")
        Future.successful(())
      })

  private val items: List[Item] = args.map(arg =>
    arg match {
      case (label, creator) => {
        val link = a(label)
        val item = Item(label, link, creator, None)
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
