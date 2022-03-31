package dev.myclinic.scala.masterdb

@main def main(cmd: String, cmdArgs: String*): Unit =
  cmd match {
    case "update-shinryou" => Update.updateShinryou()
    case _ => usage
  }

def usage: Unit =
  System.err.println("usage: masterDb download")


