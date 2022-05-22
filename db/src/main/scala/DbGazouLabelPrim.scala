package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import scala.math.Ordered.orderingToOrdered

object DbGazouLabelPrim:
  def findGazouLabel(conductId: Int): Query0[GazouLabel] =
    sql"""
      select * from visit_gazou_label where visit_conduct_id = ${conductId}
    """.query[GazouLabel]