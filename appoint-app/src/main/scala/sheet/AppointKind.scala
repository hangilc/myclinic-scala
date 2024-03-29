package dev.myclinic.scala.web.appoint.sheet

class AppointKind(
    val code: String,
    val label: String,
    val ord: Int,
    val iconColor: String
):
  def cssClass: String = "appoint-" + code

object AppointKind:
  var registry: Map[String, AppointKind] = Map()
  initRegistry()
  println(registry)

  val maxOrder = 100
  val defaultIconColor = "gray"

  def addToRegistry(k: AppointKind): Unit =
    registry = registry + (k.code -> k)

  def initRegistry(): Unit =
    addToRegistry(new AppointKind("regular", "", 0, "green"))
    addToRegistry(new AppointKind("covid-vac", "コロナワクチン", 1, "purple"))
    addToRegistry(new AppointKind("flu-vac", "インフルワクチン", 2, "yellow"))
    addToRegistry(new AppointKind("covid-vac-pfizer", "コロナワクチン（ファイザー）", 3, "lightblue"))
    addToRegistry(new AppointKind("covid-vac-pfizer-om", "コロナワクチン（ファイザー、オミクロン）", 4, "lightblue"))
    addToRegistry(new AppointKind("covid-vac-moderna", "コロナワクチン（モデルナ）", 5, "orange"))

  def apply(kind: String): AppointKind =
    def addNew(): AppointKind =
      val k = new AppointKind(kind, kind, maxOrder - 1, defaultIconColor)
      addToRegistry(k)
      k
    registry.getOrElse(kind, addNew())
