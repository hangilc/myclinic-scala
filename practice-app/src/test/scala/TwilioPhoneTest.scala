package dev.myclinic.scala.web.practiceapp.practice.twilio

import org.scalatest.funsuite.AnyFunSuite

class TwilioPhoneTest extends AnyFunSuite:
  test("local"){
    assert(TwilioPhone.canonicalPhoneNumber("03-1234-4321") == Some("+81312344321"))
  }

  test("canonical"){
    assert(TwilioPhone.canonicalPhoneNumber("+81312344321") == Some("+81312344321"))
  }

  test("tokyo"){
    assert(TwilioPhone.canonicalPhoneNumber("1234-4321") == Some("+81312344321"))
  }