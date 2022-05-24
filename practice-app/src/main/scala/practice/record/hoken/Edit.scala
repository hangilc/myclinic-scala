package dev.myclinic.scala.web.practiceapp.practice.record.hoken

import dev.myclinic.scala.model.HokenInfo
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.apputil.HokenUtil.Ext.*
import org.scalajs.dom.HTMLElement

object Edit:
  def open(
      shahoOpt: Option[Shahokokuho],
      koukikoureiOpt: Option[Koukikourei],
      kouhiList: List[Kouhi]
  ): Unit =
    val shahoCheck: Option[CheckLabel[Shahokokuho]] =
      shahoOpt.map(h => CheckLabel[Shahokokuho](h, h.rep))
    val koukikoureiCheck: Option[CheckLabel[Koukikourei]] =
      koukikoureiOpt.map(h => CheckLabel[Koukikourei](h, h.rep))
    val kouhiChecks: List[CheckLabel[Kouhi]] =
      kouhiList.map(h => CheckLabel[Kouhi](h, h.rep))

    val dlog = new ModalDialog3()
    dlog.title(innerText := "保険選択")
    dlog.body(
      eleOf(shahoCheck),
      eleOf(koukikoureiCheck) //,
      //kouhiChecks.map(_.wrap(div))
    )
    dlog.commands(
      button("キャンセル", onclick := (_ => dlog.close()))
    )
    dlog.open()

  def eleOf[T](c: Option[CheckLabel[T]]): Option[HTMLElement] =
    c.map(c => c.wrap(div))
