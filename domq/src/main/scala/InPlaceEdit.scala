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
    dispActive: EventAcceptor[Disp, "activate", Unit],
    editElementProvider: ElementProvider[Edit],
    editSetter: DataAcceptor[Edit, Option[Data]],
    editGetter: DataProvider[Edit, Option[Data]],
    editDoneTrigger: TriggerProvider[Edit],
    editCancelTrigger: GeneralTriggerProvider[Edit, "cancel"],
    editActive: EventAcceptor[Edit, "activate", Unit]
):
  private var dataOpt: Option[Data] = origDataOpt
  val flipFlop = new FlipFlop(dispComponent, editComponent)
  def ele = flipFlop.ele
  def getData: Option[Data] = dataOpt
  disp()

  editDoneTrigger.setTriggerHandler(editComponent, () => {
    dataOpt = editGetter.getData(editComponent)
    dispSetter.setData(dispComponent, dataOpt)
    disp()
    onDataChange(dataOpt)
  })

  editCancelTrigger.setTriggerHandler(editComponent, () => {
    disp()
  })

  dispTrigger.setTriggerHandler(dispComponent, () => {
    editSetter.setData(editComponent, dataOpt)
    edit()
  })

  def disp(): Unit =
    dispSetter.setData(dispComponent, dataOpt)
    flipFlop.flip()

  def edit(): Unit =
    editSetter.setData(editComponent, dataOpt)
    flipFlop.flop()

