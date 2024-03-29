package dev.fujiwara.domq

import dev.fujiwara.domq.TypeClasses.{*, given}

class InPlaceEdit[Disp, Edit, Data](
    dispComponent: Disp,
    editComponent: Edit,
    origDataOpt: Option[Data],
    onDataChange: Option[Data] => Unit
)(using
    dispElementProvider: ElementProvider[Disp],
    dispSetter: DataAcceptor[Disp, Option[Data]],
    dispTrigger: TriggerProvider[Disp],
    dispActive: EventAcceptor[Disp, Unit, "activate"],
    editElementProvider: ElementProvider[Edit],
    editSetter: DataAcceptor[Edit, Option[Data]],
    editGetter: DataProvider[Edit, Option[Data]],
    editDoneTrigger: TriggerProvider[Edit],
    editCancelTrigger: GeneralTriggerProvider[Edit, "cancel"],
    editActive: EventAcceptor[Edit, Unit, "activate"]
):
  private var dataOpt: Option[Data] = origDataOpt
  val flipFlop = new FlipFlop(dispComponent, editComponent)
  def ele = flipFlop.ele
  def getData: Option[Data] = dataOpt
  disp()

  editDoneTrigger.setTriggerHandler(editComponent, () => {
    triggerEditDone(editGetter.getData(editComponent))
  })

  editCancelTrigger.setTriggerHandler(editComponent, () => {
    disp()
  })

  dispTrigger.setTriggerHandler(dispComponent, () => {
    editSetter.setData(editComponent, dataOpt)
    edit()
  })

  def triggerEditDone(opt: Option[Data]): Unit =
    dataOpt = opt
    dispSetter.setData(dispComponent, dataOpt)
    disp()
    onDataChange(dataOpt)

  def disp(): Unit =
    dispSetter.setData(dispComponent, dataOpt)
    flipFlop.flip()

  def edit(): Unit =
    editSetter.setData(editComponent, dataOpt)
    flipFlop.flop()

