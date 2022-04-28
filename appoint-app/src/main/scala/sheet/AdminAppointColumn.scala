package dev.myclinic.scala.web.appoint.sheet

import java.time.LocalDate
import dev.myclinic.scala.clinicop.ClinicOperation
import dev.myclinic.scala.web.appoint.Misc
import dev.fujiwara.domq.ShowMessage
import dev.myclinic.scala.webclient.{Api, global}
import cats.*
import cats.syntax.all.*
import scala.util.{Success, Failure}
import dev.myclinic.scala.model.AppointTime
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.web.appbase.SyncedDataSource

class AdminAppointColumn(date: LocalDate, op: ClinicOperation)(using EventFetcher)
    extends AppointColumn(date, op):
  override protected def composeContextMenu: List[(String, () => Unit)] =
    import AdminAppointColumn.*
    val menu = List(
        "予約枠追加" -> (() => doAddAppointTime(date))
    ) ++ (
        if totalAppoints == 0 then
            List("予約枠全削除" -> (() => doDeleteAllAppointTimes(boxes.list, date)))
        else List.empty
    )
    super.composeContextMenu ++ menu

  override def createAppointTimeBox(
      g: Int,
      appointTime: AppointTime
  ): AppointTimeBox =
    AdminAppointTimeBox(
      SyncedDataSource(g, appointTime),
      () => AppointColumn.findVacantFollowers(boxes.list, appointTime)
    )


object AdminAppointColumn:
  def doAddAppointTime(date: LocalDate): Unit =
    val dlog = AddAppointTimeDialog(date)
    dlog.open()
    dlog.initFocus()

  def doDeleteAllAppointTimes(boxes: List[AppointTimeBox], date: LocalDate): Unit =
    val dateRep = Misc.formatAppointDate(date)
    ShowMessage.confirm(
      s"${dateRep}の予約枠を全部削除していいですか？"
    )(() => {
      val ids: List[Int] = boxes.map(_.appointTime.appointTimeId).toList
      (for _ <- ids.map(id => Api.deleteAppointTime(id)).sequence.void
      yield ()).onComplete {
        case Success(_)  => ()
        case Failure(ex) => System.err.println(ex.getMessage)
      }
    })


// class AdminAppointColumn(date: LocalDate, op: ClinicOperation)(using EventFetcher)
//     extends AppointColumn(date, op):

//   override def composeContextMenu(
//       prev: List[(String, () => Unit)]
//   ): List[(String, () => Unit)] =
//     (prev :+ ("予約枠追加", (doAddAppointTime _)))
//       ++ (if totalAppoints == 0 then
//             List("予約枠全削除" -> (doDeleteAllAppointTimes _))
//           else List.empty)

//   def doAddAppointTime(): Unit =
//     AddAppointTimeDialog(date).open()

//   def doDeleteAllAppointTimes(): Unit =
//     val dateRep = Misc.formatAppointDate(date)
//     ShowMessage.confirm(
//       s"${dateRep}の予約枠を全部削除していいですか？"
//     )(() => {
//       val ids: List[Int] = boxes.map(_.appointTimeId).toList
//       (for _ <- ids.map(id => Api.deleteAppointTime(id)).sequence.void
//       yield ()).onComplete {
//         case Success(_)  => ()
//         case Failure(ex) => System.err.println(ex.getMessage)
//       }
//     })

//   override def makeAppointTimeBox(
//       appointTime: AppointTime,
//       gen: Int,
//       findVacantFollower: () => List[AppointTime]
//   ): AppointTimeBox =
//     new AdminAppointTimeBox(appointTime, gen, findVacantFollower)
