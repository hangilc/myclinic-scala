package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.*

object RcptVisit:
  def getMeisai(visit: VisitEx)(using houkatsuKensa: HoukatsuKensa): List[MeisaiSection] =
    val simpleShinryouGroups = BufferList[SimpleShinryouGroup]()
    val houkatsuKensaGroups = BufferList[SimpleShinryouGroup]()
    def addShinryou(s: ShinryouEx): Unit =
      val houkatsuKensaKind = HoukatsuKensaKind.fromCode(s.master.houkatsukensa)
      if houkatsuKensaKind == HoukatsuKensaKind.NONE then
        simpleShinryouGroups
          .find(g => g.canAdd(s))
          .fold[Unit](
            simpleShinryouGroups :+ SimpleShinryouGroup(s)
          )(
            g => g.add(s)
          )
        houkatsuKensaGroups.find(g => g.canAdd(s))
          .fold[Unit]({
            val hg = HoukatsuKensaGroup(houkatsuKensa, visit.visitedAt.toLocalDate, houkatsuKensaKind)
            hg.add(s)
            houkatsuKensaGroups :+ hg
          })(hg => hg.add(s))
    if !visit.drugs.isEmpty then
      throw new RuntimeException("visit drugs not supported")
    visit.shinryouList.foreach(s => addShinryou(s))
