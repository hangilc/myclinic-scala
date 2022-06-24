package dev.myclinic.scala.server

import org.http4s.QueryParamDecoder
import java.time.{LocalDate, LocalTime, LocalDateTime}
import dev.fujiwara.kanjidate.DateUtil

trait DateTimeQueryParam:
  given QueryParamDecoder[LocalDate] =
    QueryParamDecoder[String].map(DateUtil.stringToDate(_))
  given QueryParamDecoder[LocalTime] =
    QueryParamDecoder[String].map(DateUtil.stringToTime(_))
  given QueryParamDecoder[LocalDateTime] =
    QueryParamDecoder[String].map(DateUtil.stringToDateTime(_))
