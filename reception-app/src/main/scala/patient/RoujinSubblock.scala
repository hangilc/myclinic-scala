package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{
  Icons,
  Form,
  ErrorBox,
  Modifier,
  ShowMessage,
  CustomEvent
}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.{Api, global}

import dev.myclinic.scala.model.*
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.util.{HokenRep, RcptUtil}
import dev.myclinic.scala.apputil.FutanWari
import dev.myclinic.scala.web.appbase.SyncedDataSource
import dev.myclinic.scala.web.appbase.EventFetcher

class RoujinSubblock(oriGen: Int, oriRoujin: Roujin)(using EventFetcher):
  val ds = SyncedDataSource[Roujin](oriGen, oriRoujin)
  def gen = ds.gen
  def roujin = ds.data
  val eContent = div()
  val eCommands = div()
  val block: Subblock = Subblock(
    "老人保険",
    eContent,
    eCommands
  )
  block.ele(
    cls := s"roujin-${roujin.roujinId}"
  )
  disp()
  ds.startSync(ele)

  def ele = block.ele

  def disp(): Unit =
    eContent(clear)
    eContent(RoujinDisp(ds).ele)
    eCommands(clear)
    eCommands(
      button("閉じる", onclick := (() => block.ele.remove()))
    )

