package dev.myclinic.scala.util

object Misc:
  def countPages(total: Int, itemsPerPage: Int): Int =
    (total + itemsPerPage - 1) / itemsPerPage