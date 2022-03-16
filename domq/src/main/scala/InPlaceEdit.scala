package dev.fujiwara.domq

import dev.fujiwara.domq.TypeClasses.{*, given}

class InPlaceEdit[Disp, Edit, Data](
    dispComponent: Disp,
    editComponent: Edit,
    origDataOpt: Option[Data]
)(using
    dispElementProvider: ElementProvider[Disp],
    dispSetter: DataAcceptor[Disp, Option[Data]],
    dispTrigger: TriggerProvider[Disp],
    editElementProvider: ElementProvider[Edit],
    editSetter: DataAcceptor[Edit, Option[Data]],
    editGetter: DataProvider[Edit, Option[Data]],
    editDoneTrigger: TriggerProvider[Edit]
):
  private var dataOpt: Option[Data] = origDataOpt
  var onDataChange: Option[Data] => Unit = _ => ()
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

  def disp(): Unit =
    dispSetter.setData(dispComponent, dataOpt)
    flipFlop.flip()

  def edit(): Unit =
    editSetter.setData(editComponent, dataOpt)
    flipFlop.flop()

