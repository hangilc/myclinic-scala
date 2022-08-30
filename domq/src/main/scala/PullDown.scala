package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement}
import org.scalajs.dom.{document}
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{Geometry, Icons, FloatingElement, Screen}
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.window
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global

import scala.util.Failure
import scala.{util => ju}
import scala.util.Success
import org.scalajs.dom.HTMLAnchorElement

class PullDownMenu:
  val screen: HTMLElement = Screen.screen
  val wrapper: HTMLElement = div()
  val menu: FloatingElement = FloatingElement(wrapper)
  val zIndexScreen = ZIndexManager.alloc()
  val zIndexMenu = ZIndexManager.alloc()

  def open(content: HTMLElement, locate: FloatingElement => Unit): Unit =
    wrapper(content)
    screen(zIndex := zIndexScreen)(
      onclick := ((e: MouseEvent) => {
        e.preventDefault()
        e.stopPropagation()
        close()
      })
    )
    menu.ele(zIndex := zIndexMenu)
    document.body(screen)
    menu.insert()
    locate(menu)
    menu.show()

  def close(): Unit =
    menu.remove()
    screen.remove()
    ZIndexManager.release(zIndexMenu)
    ZIndexManager.release(zIndexScreen)
    onClose()

  def onClose(): Unit = ()

  def createContent(items: List[(String, () => Unit)]): HTMLElement =
    div(
      items.map { (t, c) =>
        {
          a(
            t,
            onclick := { () =>
              close()
              c()
            }
          )
        }
      }
    )

class PullDown(
    val anchor: HTMLElement,
    mkMenu: () => Future[List[(String, () => Unit)]]
):
  anchor(onclick := (onClick _))

  def onClick(): Unit =
    val pm = new PullDownMenu()
    for items <- mkMenu()
    yield
      val contents: List[HTMLElement] = items.map { (t, c) =>
        {
          a(
            t,
            onclick := { () =>
              pm.close()
              c()
            }
          )
        }
      }
      pm.open(div(contents), fe => PullDown.locate(anchor, fe))

// class PullDownLinkBak(
//     label: String,
//     wrapperPostConstruct: HTMLElement => Unit = (_ => ())
// ):
//   private var builder
//       : (HTMLElement, PullDown.CloseFun, PullDown.Callback) => Unit =
//     (wrapper, close, cb) => cb()
//   val link: HTMLElement =
//     a(label, Icons.downTriangleFlat)(cls := "domq-pull-down-link")
//   link(onclick := ((e: MouseEvent) => {
//     PullDown.open(builder, fe => PullDown.locatePullDownMenu(link, fe))
//   }))
//   def onError(ex: Throwable): Unit =
//     System.err.println(ex)
//   def ele: HTMLElement = link

//   def setBuilder(
//       b: (HTMLElement, PullDown.CloseFun, PullDown.Callback) => Unit
//   ): Unit =
//     builder = b

//   def setBuilder(items: List[(String, () => Unit)]): Unit =
//     setBuilder { (wrapper, close, cb) =>
//       populateWrapper(wrapper, close, items)
//       cb()
//     }

//   def setBuilder(futItems: () => Future[List[(String, () => Unit)]]): Unit =
//     setBuilder { (wrapper, close, cb) =>
//       {
//         val f =
//           for items <- futItems()
//           yield
//             populateWrapper(wrapper, close, items)
//             cb()
//         f.onComplete {
//           case Success(_)  => ()
//           case Failure(ex) => onError(ex)
//         }
//       }
//     }

//   def populateWrapper(
//       wrapper: HTMLElement,
//       close: () => Unit,
//       items: List[(String, () => Unit)]
//   ): Unit =
//     wrapperPostConstruct(wrapper)
//     items.foreach { case (label, handler) =>
//       val anchor = a(label)(onclick := (() => {
//         close()
//         handler()
//       }))
//       wrapper(anchor)
//     }

object PullDown:
  // type CloseFun = () => Unit
  // type Callback = () => Unit
  // type Locator = FloatingElement => Unit
  // def open(
  //     builder: (HTMLElement, CloseFun, Callback) => Unit,
  //     locator: Locator
  // ): Unit =
  //   val screen: HTMLElement = Screen.screen
  //   val zIndexScreen = ZIndexManager.alloc()
  //   val zIndexMenu = ZIndexManager.alloc()
  //   val wrapper = div(cls := "domq-pull-down-wrapper")
  //   val fe = FloatingElement(wrapper)
  //   val close: () => Unit = () => {
  //     fe.hide()
  //     screen.remove()
  //     ZIndexManager.release(zIndexMenu)
  //     ZIndexManager.release(zIndexScreen)
  //   }

  //   screen(zIndex := zIndexScreen)(
  //     onclick := ((e: MouseEvent) => {
  //       e.preventDefault()
  //       e.stopPropagation()
  //       close()
  //     })
  //   )
  //   builder(
  //     wrapper,
  //     close,
  //     () => {
  //       fe.ele(zIndex := zIndexMenu)
  //       locator(fe)
  //       document.body(screen)
  //       fe.show()
  //     }
  //   )

  // def locatePullDownMenu(anchor: HTMLElement, f: FloatingElement): Unit =
  //   val w = Geometry.windowRect
  //   val rect = Geometry.getRect(anchor)
  //   f.leftTop = rect.leftBottom.shiftY(4)
  //   if Geometry.isWindowLeftOverflow(f.getRect) then
  //     f.left = 4
  //   if Geometry.isWindowBottomOverflow(f.getRect, w) then
  //     f.bottom = rect.top - 4
  //   if Geometry.isWindowRightOverflow(f.getRect, w) then
  //     f.right = w.right - 4

  // def pullDown(
  //     anchor: HTMLElement,
  //     content: ((() => Unit) => HTMLElement)
  // ): HTMLElement =
  //   anchor(
  //     onclick := (() => {
  //       val m = new PullDownMenu()
  //       val c = content(() => m.close())
  //       m.open(c, f => locatePullDownMenu(anchor, f))
  //     })
  //   )

  // def pullDownFuture(
  //     anchor: HTMLElement,
  //     content: (() => Unit) => Future[HTMLElement]
  // ): HTMLElement =
  //   anchor(
  //     onclick := (() => {
  //       val m = new PullDownMenu()
  //       (for c <- content(() => m.close())
  //       yield {
  //         m.open(c, f => locatePullDownMenu(anchor, f))
  //       }).onComplete {
  //         case Success(_)  => ()
  //         case Failure(ex) => System.err.println(ex.getMessage)
  //       }
  //     })
  //   )

  def locate(anchor: HTMLElement, fe: FloatingElement): Unit =
    val r = Geometry.rectInViewport(anchor)
    fe.ele.style.left = s"${r.left + Geometry.windowScrollX}px"
    fe.ele.style.top = s"${r.top + r.height + 4}px"

  def pullDownLink(
      label: String,
      commands: List[(String, () => Unit)]
  ): HTMLElement =
    //pullDown(createLinkAnchor(label), close => createContent(close, commands))
    PullDown(a(label), () => Future.successful((commands))).anchor

  def pullDownLink(
      label: String,
      commands: () => Future[List[(String, () => Unit)]]
  ): HTMLElement =
    PullDown(a(label), commands).anchor

  def pullDownButton(
      label: String,
      commands: List[(String, () => Unit)]
  ): HTMLElement =
    PullDown(button(label), () => Future.successful(commands)).anchor

  def attachPullDown(
      e: HTMLElement,
      commands: List[(String, () => Unit)]
  ): Unit =
    PullDown(e, () => Future.successful(commands))

  def attachPullDown(
      e: HTMLElement,
      commands: () => Future[List[(String, () => Unit)]]
  ): Unit =
    PullDown(e, commands)

  def createLinkAnchor(label: String): HTMLElement =
    a(cls := "domq-pull-down link")(
      label,
      Icons.downTriangleFlat
    )

  def createButtonAnchor(label: String): HTMLElement =
    button(cls := "domq-pull-down button")(
      label,
      Icons.downTriangleFlat
    )

// def createContent(
//     close: () => Unit,
//     items: List[(String, () => Unit)],
//     menuItemsWrapperClass: String = ""
// ): HTMLElement =
//   def anchor(label: String, f: () => Unit): HTMLElement =
//     a(
//       label,
//       onclick := (() => {
//         close()
//         f()
//       })
//     )
//   div(cls := "domq-context-menu", cls := menuItemsWrapperClass)(
//     children :=
//       items.map({ case (label, f) =>
//         div(anchor(label, f))
//       })
//   )
