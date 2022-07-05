package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ModelProp
import dev.fujiwara.domq.ModelInput
import dev.fujiwara.domq.ModelInputs
import dev.fujiwara.domq.ModelInputProcs
import dev.myclinic.scala.model.*
import PatientValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.DispPanel

object ShahokokuhoProps:
  class HokenshaBangouProp extends ModelProp("保険者番号")
  class HihokenshaKigouProp extends ModelProp("被保険者記号")
  class HihokenshaBangouProp extends ModelProp("被保険者番号")
  class HonninProp extends ModelProp("本人・家族")
  class ValidFromProp extends ModelProp("期限開始")
  class ValidUptoProp extends ModelProp("期限終了")
  class KoureiProp extends ModelProp("高齢")
  class EdabanProp extends ModelProp("枝番")

  object hokenshaBangouProp extends HokenshaBangouProp
  object hihokenshaKigouProp extends HihokenshaKigouProp
  object hihokenshaBangouProp extends HihokenshaBangouProp
  object honninProp extends HonninProp
  object validFromProp extends ValidFromProp
  object validUptoProp extends ValidUptoProp
  object koureiProp extends KoureiProp
  object edabanProp extends EdabanProp

  val props = (
    hokenshaBangouProp,
    hihokenshaKigouProp,
    hihokenshaBangouProp,
    honninProp,
    validFromProp,
    validUptoProp,
    koureiProp,
    edabanProp
  )
