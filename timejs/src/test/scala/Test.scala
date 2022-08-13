package dev.myclinic.scala.timejs

import org.scalatest.funsuite.AnyFunSuite
import java.time.LocalDate

class Test extends AnyFunSuite {
  test("now()"){
    assert(TimeJS.now().isInstanceOf[LocalDate])
  }

  test("1"){
    assert(1 == 1)
  }
}
