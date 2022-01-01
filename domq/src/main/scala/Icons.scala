package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement}
import org.scalajs.dom.document

object Icons:

  val defaultStyle: Modifier = css(style => {
    style.verticalAlign = "middle"
    style.cursor = "pointer"
  })

  val defaultStaticStyle: Modifier = css(style => {
    style.verticalAlign = "middle"
  })

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
  // </svg>
  def calendar: HTMLElement =
    makeIcon(
      List(
        "M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
  // </svg>
  def cog: HTMLElement =
    makeIcon(
      List(
        "M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z",
        "M15 12a3 3 0 11-6 0 3 3 0 016 0z"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
  // </svg>
  def check: HTMLElement =
    makeIcon(
      List(
        "M5 13l4 4L19 7"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
  // </svg>
  def checkCircle: HTMLElement =
    makeIcon(
      List(
        "M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
      )
    )

  def circle: HTMLElement =
    makeIcon(
      List(
        "M 40 120 A 80 80 0 0 1 120 40 A 80 80 0 0 1 200 120 A 80 80 0 0 1 120 200 A 80 80 0 0 1 40 120"
      ),
      "0 0 240 240"
    )(cls := "domq-icon-circle")

  def circleFilled: HTMLElement =
    makeIcon(
      List(
        "M 40 120 A 80 80 0 0 1 120 40 A 80 80 0 0 1 200 120 A 80 80 0 0 1 120 200 A 80 80 0 0 1 40 120"
      ),
      "0 0 240 240"
    )(cls := "domq-icon-filled")

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z" />
  // </svg>
  def dotsHorizontal: HTMLElement =
    makeIcon(
      List(
        "M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z" />
  // </svg>    
  def dotsVertical: HTMLElement =
    makeIcon(
      List(
        "M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z"
      )
    )

  def downTriangle: HTMLElement =
    val w: Double = 240
    val pad: Double = 0.15
    val x0 = w * pad
    val y0 = x0
    val x1 = w * (1 - pad)
    val y1 = y0
    val x2 = w * 0.5
    val y2 = w * (1 - pad)
    makeIcon(
      List(
        s"M $x0 $y0 L $x1 $y1 L $x2 $y2 Z"
      ),
      s"0 0 $w $w"
    )(cls := "domq-icon-filled")

  def downTriangleFlat: HTMLElement =
    val w: Double = 240
    val padx: Double = 0.1
    val pady: Double = 0.3
    val x0 = w * padx
    val y0 = w * pady
    val x1 = w * (1 - padx)
    val y1 = y0
    val x2 = w * 0.5
    val y2 = w * (1 - pady)
    makeIcon(
      List(
        s"M $x0 $y0 L $x1 $y1 L $x2 $y2 Z"
      ),
      s"0 0 $w $w"
    )(cls := "domq-icon-filled")

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
  // </svg>
  def menu: HTMLElement =
    makeIcon(
      List(
        "M4 6h16M4 12h16M4 18h16"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8h16M4 16h16" />
  // </svg>
  def menuAlt4: HTMLElement =
    makeIcon(
      List(
        "M4 8h16M4 16h16"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
  // </svg>
  def pencil: HTMLElement =
    makeIcon(
      List(
        "M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
  // </svg>
  def pencilAlt: HTMLElement =
    makeIcon(
      List(
        "M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z" />
  // </svg>
  def plusCircle: HTMLElement =
    makeIcon(
      List(
        "M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
  // </svg>
  def refresh: HTMLElement =
    makeIcon(
      List(
        "M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
  // </svg>
  def search: HTMLElement =
    makeIcon(
      List(
        "M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
  // </svg>
  def trash: HTMLElement =
    makeIcon(
      List(
        "M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
  // </svg>
  def x: HTMLElement =
    makeIcon(
      List(
        "M6 18L18 6M6 6l12 12"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
  // </svg>
  def xCircle: HTMLElement =
    makeIcon(
      List(
        "M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v3m0 0v3m0-3h3m-3 0H7" />
  // </svg>  
  def zoomIn: HTMLElement =
    makeIcon(
      List(
        "M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v3m0 0v3m0-3h3m-3 0H7"
      )
    )

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  //   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM13 10H7" />
  // </svg>
  def zoomOut: HTMLElement =
    makeIcon(
      List(
        "M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM13 10H7"
      )
    )

  // private def makeIcon(
  //     width: String,
  //     height: String,
  //     strokeColor: String,
  //     ds: List[String],
  //     viewBox: String,
  //     strokeWidth: String,
  //     fillColor: String
  // ): HTMLElement =
  //   val ns = "http://www.w3.org/2000/svg"
  //   val svg = document.createElementNS(ns, "svg").asInstanceOf[HTMLElement]
  //   def path = document.createElementNS(ns, "path").asInstanceOf[HTMLElement]
  //   val paths: List[Modifier] =
  //     ds.map(d =>
  //       path(
  //         attr("stroke-linecap") := "round",
  //         attr("stroke-linejoin") := "round",
  //         attr("stroke-width") := strokeWidth,
  //         attr("d") := d
  //       )
  //     )
  //   svg(
  //     attr("viewBox") := viewBox,
  //     css(style => { style.height = height; style.width = width }),
  //     attr("fill") := fillColor,
  //     attr("stroke") := strokeColor
  //   )(paths: _*)

  // private def makeIcon(
  //     size: String,
  //     strokeColor: String,
  //     ds: List[String],
  //     viewBox: String = "0 0 24 24",
  //     strokeWidth: String = "2",
  //     fillColor: String = "none"
  // ): HTMLElement = makeIcon(size, size, strokeColor, ds, viewBox, strokeWidth, fillColor)

  // private def makeIcon2(
  //     strokeColor: String,
  //     ds: List[String],
  //     viewBox: String,
  //     strokeWidth: String,
  //     fillColor: String
  // ): HTMLElement =
  //   val ns = "http://www.w3.org/2000/svg"
  //   val svg = document.createElementNS(ns, "svg").asInstanceOf[HTMLElement]
  //   def path = document.createElementNS(ns, "path").asInstanceOf[HTMLElement]
  //   val paths: List[Modifier] =
  //     ds.map(d =>
  //       path(
  //         attr("stroke-linecap") := "round",
  //         attr("stroke-linejoin") := "round",
  //         attr("stroke-width") := strokeWidth,
  //         attr("d") := d
  //       )
  //     )
  //   svg(
  //     attr("viewBox") := viewBox,
  //     //css(style => { style.height = height; style.width = width }),
  //     //attr("fill") := fillColor,
  //     attr("stroke") := strokeColor
  //   )(paths: _*)

  private def makeIcon(
      ds: List[String],
      viewBox: String = "0 0 24 24"
  ): HTMLElement =
    val ns = "http://www.w3.org/2000/svg"
    val svg = document.createElementNS(ns, "svg").asInstanceOf[HTMLElement]
    def path = document.createElementNS(ns, "path").asInstanceOf[HTMLElement]
    val paths: List[Modifier] =
      ds.map(d =>
        path(
          attr("stroke-linecap") := "round",
          attr("stroke-linejoin") := "round",
          attr("d") := d
        )
      )
    svg(
      attr("viewBox") := viewBox,
      cls := "domq-icon",
    )(paths: _*)

  private def makeIcon(
    size: String,
    color: String,
    ds: List[String]
  ): HTMLElement = ???

