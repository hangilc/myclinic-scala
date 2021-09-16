package dev.fujiwara.domq

object GenId:
  private var genIdCounter = 0

  def genId(): String =
    genIdCounter += 1
    s"genId-$genIdCounter"
