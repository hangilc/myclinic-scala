package dev.myclinic.scala.model

trait DataId[T]:
  def getId(data: T): Int

trait ModelSymbol[T]:
  def getSymbol: String

 
