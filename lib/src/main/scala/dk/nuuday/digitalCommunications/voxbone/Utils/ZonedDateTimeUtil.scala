package dk.nuuday.digitalCommunications.voxbone.Utils

import java.time.{Instant, ZoneId, ZonedDateTime}

object ZonedDateTimeUtil {




  def now: ZonedDateTime = ZonedDateTime.now()
  def aDayAgo: ZonedDateTime = now.minusDays(1)



}
