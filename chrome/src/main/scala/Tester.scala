package dev.myclinic.scala.chrome

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}

class Tester(
    baseUrl: String = "http://localhost:8080",
    headless: Boolean = true
):
  val opts = new ChromeOptions()
  if headless then opts.addArguments("--headless")
  val driver: ChromeDriver = new ChromeDriver(opts)

  def rest[T](f: Client[IO] => IO[T]): T =
    BlazeClientBuilder[IO].resource.use(f).unsafeRunSync()

  def isTesting: Boolean =
    rest[Boolean] { client =>
      for
        patientOpt <- client.expect[Option[Patient]](
          apiUrl("/find-patient?patient-id=1")
        )
      yield patientOpt.fold(false)(p =>
        p.lastName == "Shinryou" && p.firstName == "Tarou"
      )
    }
