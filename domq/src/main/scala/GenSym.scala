package dev.fujiwara.domq

object GenSym:
  private var serial: Int = 0

  def genSym: String = 
    serial += 1
    val id = serial
    s"domq-gensym-${id}"