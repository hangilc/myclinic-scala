package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}
import org.scalajs.dom.document

object Icons:

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
  // </svg>
  def cog(size: String = "1.5rem", color: String = "black"): HTMLElement =
    makeIcon(size, color, List(
      "M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z",
      "M15 12a3 3 0 11-6 0 3 3 0 016 0z"
    ))

  private def makeIcon(size: String, color: String, ds: List[String]): HTMLElement =
    val ns = "http://www.w3.org/2000/svg"
    val svg = document.createElementNS(ns, "svg").asInstanceOf[HTMLElement]
    def path = document.createElementNS(ns, "path").asInstanceOf[HTMLElement]
    val paths : List[Modifier] =
      ds.map(d => path(
        attr("stroke-linecap") := "round",
        attr("stroke-linejoin") := "round",
        attr("stroke-width") := "2",
        attr("d") := d
      ))
    svg(
      attr("viewBox") := "0 0 24 24",
      css(style => { style.height = size; style.width = size }),
      attr("fill") := "none",
      attr("stroke") := color,
    )(paths: _*)


