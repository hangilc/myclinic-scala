// package dev.myclinic.scala.formatshohousen

// import scala.util.matching.Regex

// case class GlobalCommand(
//   raw: Boolean = false,
//   commands: List[String] = List.empty
// ): 
//   import GlobalCommand.*
//   def handle(command: String): GlobalCommand =
//     command match {
//       case rawPattern() => copy(raw = true)
//       case c => copy(commands = commands :+ c)
//     }

// object GlobalCommand:
//   val rawPattern: Regex = "@raw".r

//   def apply(commands: List[String]): GlobalCommand =
//     commands.foldLeft(GlobalCommand())((g, c) => g.handle(c))