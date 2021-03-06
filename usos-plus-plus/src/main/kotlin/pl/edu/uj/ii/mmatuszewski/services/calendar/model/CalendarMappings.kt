package pl.edu.uj.ii.mmatuszewski.services.calendar.model

import biweekly.component.VEvent
import biweekly.property.DateOrDateTimeProperty
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun VEvent.toDisplay() =
        EventDisplay(uid.value, dateStart.toLocalDateTime(), dateEnd.toLocalDateTime(), summary.value)

fun DateOrDateTimeProperty.toLocalDateTime(): LocalDateTime =
        value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

fun LocalDateTime.toPolishDate(): Date =
        Date.from(atZone(ZoneId.of("Europe/Warsaw")).toInstant())
