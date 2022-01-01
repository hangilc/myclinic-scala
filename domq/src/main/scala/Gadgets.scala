// package dev.fujiwara.domq

// import org.scalajs.dom.{HTMLElement}
// import dev.fujiwara.domq.ElementQ.{*, given}
// import dev.fujiwara.domq.Html.{*, given}
// import dev.fujiwara.domq.Modifiers.{*, given}
// import dev.fujiwara.domq.{ContextMenu}
// import scala.language.implicitConversions
// import org.scalajs.dom.ClientRect
// import org.scalajs.dom.MouseEvent

// object Gadgets:
//   def pullDown(
//       label: String,
//       menu: List[(String, () => Unit)],
//       zIndex: Int = Modal.zIndexDefault
//   ): HTMLElement =
//     new PullDown(label, menu, zIndex).ele

//   class PullDown(label: String, commands: List[(String, () => Unit)], zIndex: Int):
//     var cm: Option[PullDownContextMenu] = None
//     val ele: HTMLElement = button(
//       "患者選択",
//       Icons.downTriangleFlat(size = "0.7rem", color = "#989898")(
//         ml := "0.2rem",
//         css(style => {
//           style.position = "relative"
//           style.top = "0.08rem"
//         })
//       ),
//       onclick := ((e: MouseEvent) => {
//         cm match {
//           case None =>
//             val cmSome = PullDownContextMenu(ele, commands, zIndex)
//             cm = Some(cmSome)
//             cmSome.open(e)
//           case Some(cmSome) =>
//             cmSome.close()
//             cm = None
//         }
//       })
//     )


//   class PullDownContextMenu(button: HTMLElement, commands: List[(String, () => Unit)], zIndex: Int)
//       extends ContextMenu(zIndex):
//     override def calcPlacement(
//         clickX: Double,
//         clickY: Double,
//         rect: ClientRect,
//         windowWidth: Double,
//         windowHeight: Double
//     ): (Double, Double) =
//       val btnRect: ClientRect = button.getClientRects()(0)
//       val x =
//         if btnRect.left + rect.width < windowWidth then btnRect.left
//         else btnRect.right - rect.width
//       val y =
//         if btnRect.bottom + 3 + rect.height < windowHeight then btnRect.bottom + 3
//         else btnRect.top - 3 - rect.height
//       (x, y)
//     def makeItem(label: String, f: () => Unit): HTMLElement =
//       div(
//         a(
//           label,
//           href := "",
//           onclick := ((e: MouseEvent) => {
//             e.preventDefault
//             close()
//             f()
//           })
//         )
//       )
//     val items = commands.map((name, f) => {
//       Modifier(e => e.appendChild(makeItem(name, f)))
//     })
//     menu(items: _*)

