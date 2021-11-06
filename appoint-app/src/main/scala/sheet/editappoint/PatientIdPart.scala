package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Colors, ErrorBox}
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import dev.myclinic.scala.webclient.Api
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.{Patient, Appoint}
import org.scalajs.dom.document
import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future

class PatientIdPart(var appoint: Appoint):
  val keyPart = span("患者番号：")
  val manager = ValuePartManager(Disp())
  val valuePart = manager.ele

  def onAppointChanged(newAppoint: Appoint): Unit =
    appoint = newAppoint
    manager.updateUI()

  def changeValuePartTo(part: ValuePart): Unit =
    manager.changeValuePartTo(part)

  class Disp() extends ValuePart:
    val editIcon = Icons.pencilAlt(color = "gray", size = "1.2rem")
    val main = div(
      span({ println(("label", label)); label }),
      editIcon(
        Icons.defaultStyle,
        ml := "0.5rem",
        displayNone,
        onclick := (() => changeValuePartTo(Edit()))
      )
    )
    main(onmouseenter := (() => {
      editIcon(displayDefault)
      ()
    }))
    main(onmouseleave := (() => {
      editIcon(displayNone)
      ()
    }))

    def updateUI(): Unit =
      changeValuePartTo(Disp())

    def label: String =
      if appoint.patientId == 0 then "（設定なし）"
      else appoint.patientId.toString

  class Edit() extends ValuePart with SearchResult:
    def patientId: Int = appoint.patientId
    val input = inputText(value := initialValue)
    val enterIcon = Icons.checkCircle(color = Colors.primary, size = "1.2rem")
    val discardIcon = Icons.xCircle(color = Colors.danger, size = "1.2rem")
    //val refreshIcon = Icons.refresh(color = "gray", size = "1.2rem")
    val searchIcon = Icons.search(color = "gray", size = "1.2rem")
    enterIcon(onclick := (() => onEnter()))
    discardIcon(onclick := (() => changeValuePartTo(Disp())))
    //refreshIcon(onclick := (() => doRefresh()))
    searchIcon(onclick := (() => onSearchClick()))

    val main = div(
      input(value := initialValue, width := "4rem"),
      enterIcon(
        Icons.defaultStyle,
        ml := "0.5rem"
      ),
      discardIcon(Icons.defaultStyle),
      //refreshIcon(Icons.defaultStyle),
      searchIcon(Icons.defaultStyle)
    )

    def updateUI(): Unit =
      input.value = appoint.patientId.toString

    def initialValue: String = if patientId == 0 then "" else patientId.toString

    def onEnter(): Unit =
      val patientIdResult = AppointValidator.validatePatientId(input.value.trim)
      patientIdResult.toEither() match {
        case Right(patientIdValue) => {
          if patientId == patientIdValue then changeValuePartTo(Disp())
          else
            for
              appoint <- Api.getAppoint(appoint.appointId)
              patientOption <- Api.findPatient(patientIdValue)
            yield {
              val newAppoint = appoint.copy(patientId = patientIdValue)
              AppointValidator
                .validateForUpdate(newAppoint, patientOption)
                .toEither() match {
                case Right(newAppoint) => {
                  Api.updateAppoint(newAppoint)
                  changeValuePartTo(Disp())
                }
                case Left(msg) => showError(msg)
              }

            }
        }
        case Left(msg) => showError(msg)
      }

    // def makePatientSlot(patient: Patient): HTMLElement =
    //   div(hoverBackground("#eee"), padding := "2px 4px", cursor := "pointer")(
    //     s"(${patient.patientId}) ${patient.fullName()}"
    //   )

    // def populateWorkarea(patients: List[Patient]): Unit =
    //   initWorkarea()
    //   patients.foreach(patient => {
    //     val slot = makePatientSlot(patient)
    //     slot(onclick := (() => {
    //       input.value = patient.patientId.toString
    //       post()
    //     }))
    //     addToWorkarea(slot)
    //   })

    // def doRefresh(): Unit =
    //   initWorkarea()
    //   val f =
    //     for patients <- Api.searchPatient(appoint.patientName)
    //     yield populateWorkarea(patients)
    //   f.catchErr

    def onSearchClick(): Unit =
      initWorkarea()
      AppointValidator.validatePatientId(input.value).toEither() match {
        case Right(patientId) => {
          (for patientOption <- Api.findPatient(patientId)
          yield populateSearchResult(
            patientOption.toList,
            appoint.appointId,
            () => changeValuePartTo(Disp())
          )).catchErr
        }
        case Left(msg) => showError(msg)
      }

// def post(): Unit =
//   val f =
//     for appointResult <- validate()
//     yield {
//       appointResult match {
//         case Right(appoint) => {
//           if appoint.patientId != patientId then Api.updateAppoint(appoint)
//           initWorkarea()
//         }
//         case Left(msg) => showError(msg)
//       }
//     }
//   f.catchErr

// def validate(): Future[Either[String, Appoint]] =
//   val patientIdResult = AppointValidator.validatePatientId(input.value)
//   patientIdResult.toEither() match {
//     case Left(msg) => Future.successful(Left(msg))
//     case Right(patientId) => {
//       for
//         appoint <- Api.getAppoint(appoint.appointId)
//         patientOption <- Api.findPatient(patientId)
//       yield {
//         AppointValidator
//           .validateForUpdate(
//             appoint.copy(patientId = patientId),
//             patientOption
//           )
//           .toEither()
//       }
//     }
//   }
