package dev.fujiwara.scala.drawer

import dev.fujiwara.drawer.op as javaOp
import scala.jdk.CollectionConverters.*

object ToJavaOp:
  def convert(op: Op): javaOp.Op =
    op match {
      case OpMoveTo(x, y)                 => new javaOp.OpMoveTo(x, y)
      case OpLineTo(x: Double, y: Double) => new javaOp.OpLineTo(x, y)
      case OpCreateFont(
            name,
            fontName,
            size,
            weight,
            italic
          ) =>
        new javaOp.OpCreateFont(name, fontName, size, weight, italic)
      case OpSetFont(name)         => new javaOp.OpSetFont(name)
      case OpSetTextColor(r, g, b) => new javaOp.OpSetTextColor(r, g, b)
      case OpDrawChars(chars, xs, ys) =>
        new javaOp.OpDrawChars(
          chars,
          xs.map(Double.box).asJava,
          ys.map(Double.box).asJava
        )
      case OpCreatePen(
            name,
            r,
            g,
            b,
            width,
            penStyle: List[Double]
          ) =>
        new javaOp.OpCreatePen(
          name,
          r,
          g,
          b,
          width,
          penStyle.map(Double.box).asJava
        )
      case OpSetPen(name)      => new javaOp.OpSetPen(name)
      case OpCircle(cx, cy, r) => new javaOp.OpCircle(cx, cy, r)
    }
