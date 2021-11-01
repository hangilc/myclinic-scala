package dev.myclinic.scala.server

class CmdArgs:
  var ssl: Boolean = false

object CmdArgs:
  def apply(args: List[String]): CmdArgs =
    val cmdArgs = new CmdArgs
    parse(args, cmdArgs)
    cmdArgs

  @annotation.tailrec
  def parse(args: List[String], cmdArgs: CmdArgs): Unit =
    args match {
      case Nil => ()
      case "-ssl" :: tail => {
        cmdArgs.ssl = true
        parse(tail, cmdArgs)
      }
      case _ => {
        System.err.println(s"Cannot handle arg: %{arg(0)}")
        sys.exit(1)
      }
    }
