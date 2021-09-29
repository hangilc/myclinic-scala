package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbAppointPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime

trait DbAppoint extends Sqlite:
  private def withEventId[A](f: Int => ConnectionIO[A]): IO[A] =
    sqlite(DbEventPrim.withEventId(eventId => f(eventId)))

  def batchEnterAppointTimes(appointTimes: List[AppointTime]): IO[Unit] =
    withEventId(eventId => {
      appointTimes
        .map(_.copy(eventId = eventId))
        .map(at => Prim.enterAppointTime(at))
        .sequence_
    })

  def listExistingAppointTimeDates(
      from: LocalDate,
      upto: LocalDate
  ): IO[List[LocalDate]] =
    sqlite(Prim.listExistingAppointTimeDates(from, upto).to[List])

//   def getAppoint(date: LocalDate, time: LocalTime): IO[Appoint] =
//     sqlite(DbAppointPrim.getAppoint(date, time).unique)

//   def listAppoint(from: LocalDate, upto: LocalDate): IO[List[Appoint]] =
//     sqlite(DbAppointPrim.listAppoint(from, upto).to[List])

//   def createAppointTimes(
//       times: List[(LocalDate, LocalTime)]
//   ): IO[List[AppEvent]] =
//     def seq(eventId: Int): ConnectionIO[List[AppEvent]] =
//       times
//         .map({ (d: LocalDate, t: LocalTime) =>
//           {
//             val app = Appoint(d, t, eventId, "", 0, "")
//             DbAppointPrim.enterAppoint(app) *>
//               AppEventHelper.enterAppointEvent(eventId, "created", app)
//           }
//         }.tupled)
//         .sequence

//     sqlite(DbEventPrim.withEventId(seq _))

//   def createAppointTimes(
//     year: Int, month: Int, day: Int,
//     slots: (Int, Int)*
//   ): IO[List[AppEvent]] =
//     val date = LocalDate.of(year, month, day)
//     val times = for
//       ((h: Int, m: Int)) <- slots
//     yield (date, LocalTime.of(h, m, 0))
//     createAppointTimes(times.toList)

//   def registerAppoint(a: Appoint): IO[AppEvent] =
//     require(!a.patientName.isEmpty)

//     sqlite(
//       DbEventPrim.withEventId(eventId =>
//         for
//           cur <- DbAppointPrim.getAppoint(a.date, a.time).unique
//           _ <- Helper.confirm(cur.isVacant, s"Appoint is not vacant: ${cur}")
//           to = a.copy(eventId = eventId)
//           _ <- DbAppointPrim.updateAppoint(to)
//           appEvent <- AppEventHelper.enterAppointEvent(eventId, "updated", to)
//         yield appEvent
//       )
//     )

//   def cancelAppoint(
//       date: LocalDate,
//       time: LocalTime,
//       patientName: String
//   ): IO[AppEvent] =
//     require(!patientName.isEmpty)

//     sqlite(
//       DbEventPrim.withEventId(eventId =>
//         for
//           cur <- DbAppointPrim.getAppoint(date, time).unique
//           _ <- Helper.confirm(!cur.isVacant, s"Appoint is vacant: ${cur}")
//           _ <- Helper.confirm(
//             cur.patientName == patientName,
//             s"Inconsistent patient names: ${patientName} != ${cur.patientName}"
//           )
//           to = Appoint(date, time, eventId, "", 0, "")
//           _ <- DbAppointPrim.updateAppoint(to)
//           appEvent <- AppEventHelper.enterAppointEvent(eventId, "updated", to)
//         yield appEvent
//       )
//     )
