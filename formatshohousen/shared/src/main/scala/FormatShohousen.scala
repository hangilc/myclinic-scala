package dev.myclinic.scala.formatshohousen

object FormatShohousen:
  def format(src: String): String =
    val zs = FormatUtil.prepareForFormat(src)
    val parts = FormatUtil.splitToParts(zs)
    val ctx = FormatContext(parts.size)
    val (ls, cs, _) =
      parts.foldLeft((List.empty[String], List.empty[String], 1)) {
        case ((lines, commands, index), part) =>
          val (item, trails) = parseSubparts(part)
          val (cs, ts) =
            trails.foldLeft((List.empty[String], List.empty[String])) {
              case ((cur_cs, cur_ts), s) =>
                if s.startsWith("@") then (cur_cs :+ s, cur_ts)
                else (cur_cs, cur_ts :+ s)
            }
          val newLines = lines ++ item.format(index, ctx) ++
            ts.map(t => Formatter.softFormat("", t, ctx.lineSize))
          val newCommands = commands ++ cs
          (newLines, newCommands, index + 1)
      }
    (ls ++ cs).mkString("\n")

  def parseSubparts(part: String): (Item, List[String]) =
    val (lines, trails) = FormatUtil.splitToSubparts(part)
    val item = Item.parse(lines)
    (item, trails)
