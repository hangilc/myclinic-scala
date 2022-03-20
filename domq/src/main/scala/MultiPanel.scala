package dev.fujiwara.domq

import dev.fujiwara.domq.TypeClasses.*
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import org.scalajs.dom.HTMLElement


trait MultiPanelCapability[Id]:
  def getElement: HTMLElement
  def getId: Id
  def onSwitchTo(handler: Id => Unit): Unit

object MultiPanel:
  implicit def toCapability[T, Id](t: T)(using 
    elementProvider: ElementProvider[T],
    idProvider: IdProvider[T, Id],
    switchToTriggerProvider: GeneralTriggerDataProvider[T, Id, "switch-to"]
  ): MultiPanelCapability[Id] =
    new MultiPanelCapability[Id]:
      def getElement: HTMLElement = elementProvider.getElement(t)
      def getId: Id = idProvider.getId(t)
      def onSwitchTo(handler: Id => Unit): Unit =
        switchToTriggerProvider.setTriggerHandler(t, handler)

class MultiPanel[Id](init: MultiPanelCapability[Id], others: List[MultiPanelCapability[Id]]):
  type Panel = MultiPanelCapability[Id]
  private val panels: List[Panel] = init :: others
  private var cur: Panel = init
  private val idMap: Map[Id, Panel] = Map.from(panels.map(panel => (panel.getId, panel)))
  private val cave = div(displayNone)
  panels.foreach(panel => {
    cave(panel.getElement)
    panel.onSwitchTo(switchTo)
  })

  def ele: HTMLElement = cur.getElement
  def switchTo(id: Id): Unit =
    val panel = idMap(id)
    if cur != panel then
      val c = cur.getElement
      val p = panel.getElement
      cave.remove()
      c.replaceBy(p)
      cave(c)
      cur = panel
