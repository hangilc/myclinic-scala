package dev.myclinic.scala.formatshohousen

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterAll

class ItemTest extends AnyFunSuite with BeforeAndAfterAll:
  test("countLeadingDigits"){
    assert(Item.countLeadingDigits("２８日分") == 2)
    assert(Item.countLeadingDigits("７日分") == 1)
    assert(Item.countLeadingDigits("日分") == 0)
  }