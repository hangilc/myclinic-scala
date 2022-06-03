package dev.fujiwara.scala.drawer

import io.circe.*
import io.circe.syntax.*
import io.circe.parser.*
import io.circe.generic.semiauto._

sealed trait Op

case class OpMoveTo(x: Double, y: Double) extends Op
case class OpLineTo(x: Double, y: Double) extends Op
case class OpCreateFont(
    name: String,
    fontName: String,
    size: Double,
    weight: Int,
    italic: Boolean
) extends Op
case class OpSetFont(name: String) extends Op
case class OpSetTextColor(r: Int, g: Int, b: Int) extends Op
case class OpDrawChars(chars: String, xs: List[Double], ys: List[Double]) extends Op
case class OpCreatePen(
    name: String,
    r: Int,
    g: Int,
    b: Int,
    width: Double,
    penStyle: List[Double]
) extends Op
case class OpSetPen(name: String) extends Op
case class OpCircle(cx: Double, cy: Double, r: Double) extends Op

object Op:
  given Encoder[Op] = new Encoder[Op]:
    def apply(op: Op): Json =
      op match {
        case OpMoveTo(x, y) => ("move_to", x, y).asJson
        case OpLineTo(x, y) => ("line_to", x, y).asJson
        case OpCreateFont(name, fontName, size, weight, italic) =>
          ("create_font", name, fontName, size, weight, if italic then 1 else 0).asJson
        case OpSetFont(name)            => ("set_font", name).asJson
        case OpSetTextColor(r, g, b)    => ("set_text_color", r, g, b).asJson
        case OpDrawChars(chars, xs, ys) => ("draw_chars", chars, xs, ys).asJson
        case OpCreatePen(name, r, g, b, width, penStyle) =>
          ("create_pen", name, r, g, b, width, penStyle).asJson
        case OpSetPen(name)      => ("set_pen", name).asJson
        case OpCircle(cx, cy, r) => ("circle", cx, cy, r).asJson
      }

  given Decoder[Op] = new Decoder[Op]:
    def apply(c: HCursor): Decoder.Result[Op] =
      val iter = c.values.get.iterator
      iter.next
        .as[String]
        .flatMap(code => {
          code match {
            case "move_to"        => 
              for
                x <- iter.next.as[Double]
                y <- iter.next.as[Double]
              yield OpMoveTo(x, y)
            case "line_to"        => 
              for
                x <- iter.next.as[Double]
                y <- iter.next.as[Double]
              yield OpLineTo(x, y)
            case "create_font"    => 
              for
                name <- iter.next.as[String]
                fontName <- iter.next.as[String]
                size <- iter.next.as[Double]
                weight <- iter.next.as[Int]
                italic <- iter.next.as[Int]
              yield OpCreateFont(name, fontName, size, weight, if italic == 0 then false else true)
            case "set_font"       => 
              for
                name <- iter.next.as[String]
              yield OpSetFont(name)
            case "set_text_color" => 
              for
                r <- iter.next.as[Int]
                g <- iter.next.as[Int]
                b <- iter.next.as[Int]
              yield OpSetTextColor(r, g, b)
            case "draw_chars"     => 
              for
                chars <- iter.next.as[String]
                xs <- iter.next.as[List[Double]]
                ys <- iter.next.as[List[Double]]
              yield OpDrawChars(chars, xs, ys)
            case "create_pen"     => 
              for
                name <- iter.next.as[String]
                r <- iter.next.as[Int]
                g <- iter.next.as[Int]
                b <- iter.next.as[Int]
                width <- iter.next.as[Double]
                penStyle <- iter.next.as[List[Double]]
              yield OpCreatePen(name, r, g, b, width, penStyle)
            case "set_pen"        => 
              for
                name <- iter.next.as[String]
              yield OpSetPen(name)
            case "circle"         => 
              for
                cx <- iter.next.as[Double]
                cy <- iter.next.as[Double]
                r <- iter.next.as[Double]
              yield OpCircle(cx, cy, r)
            case c => throw new java.lang.RuntimeException("Unknown code: " + c)
          }
        })
