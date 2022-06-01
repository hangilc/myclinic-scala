package dev.myclinic.scala.config

case class StampInfo(
  imageFile: String,
  scale: Double = 1.0,
  xPos: Double = 0.0,  // in mm unit
  yPos: Double = 0.0,  // in mm unit
  isImageCenterRelative: Boolean = true,  // (xPos, yPos) is the position of center of stamp
)