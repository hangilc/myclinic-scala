package dev.fujiwara.domq

class InPlaceEdit[Disp, Edit, Data](
    dispComponent: Disp,
    editComponent: Edit,
    origDataOpt: Option[Data]
)(using
    dispElementProvider: ElementProvider[Disp],
    dispSetter: DataAcceptor[Disp, Option[Data]],
    editElementProvider: ElementProvider[Edit],
    editSetter: DataAcceptor[Edit, Option[Data]],
    editGetter: DataProvider[Edit, Option[Data]],
    editDoneTrigger: TriggerProvider[Edit]
):
  private var dataOpt: Option[Data] = origDataOpt
  val flipFlop = new FlipFlop(dispComponent, editComponent)
  def ele = flipFlop.ele
  def getData: Option[Data] = dataOpt

  editDoneTrigger.setTriggerHandler(editComponent, () => {
    dataOpt = editGetter.getData(editComponent)
    dispSetter.setData(dispComponent, dataOpt)
    disp()
  })

  def disp(): Unit =
    dispSetter.setData(dispComponent, dataOpt)
    flipFlop.flip()

  def edit(): Unit =
    editSetter.setData(editComponent, dataOpt)
    flipFlop.flop()

