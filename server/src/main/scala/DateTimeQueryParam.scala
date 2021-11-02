package dev.myclinic.scala.server

import org.http4s.QueryParamDecoder
import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.util.DateUtil

trait DateTimeQueryParam:
  given QueryParamDecoder[LocalDate] =
    QueryParamDecoder[String].map(DateUtil.stringToDate(_))
  given QueryParamDecoder[LocalTime] =
    QueryParamDecoder[String].map(DateUtil.stringToTime(_))
