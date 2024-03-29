package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.model.ShinryouEx
import dev.myclinic.scala.web.practiceapp.practice.record.shinryou.Disp
import scala.language.implicitConversions

case class Shinryou(shinryou: ShinryouEx):
  val ele = div(Disp(shinryou).ele)

object Shinryou:
  given Comp[Shinryou] = _.ele
  given Ordering[Shinryou] = Ordering.by(_.shinryou.shinryoucode)
  given Dispose[Shinryou] = _ => ()

