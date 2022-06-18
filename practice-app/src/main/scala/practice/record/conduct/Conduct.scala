package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class Conduct(conduct: ConductEx):
  val ele = div()
  ele(Disp(conduct).ele)

object Conduct:
  given Comp[Conduct] = _.ele
  given Dispose[Conduct] = _ => ()

