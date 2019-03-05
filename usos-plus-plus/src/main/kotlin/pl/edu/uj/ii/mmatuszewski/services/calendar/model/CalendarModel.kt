package pl.edu.uj.ii.mmatuszewski.services.calendar.model

import java.time.LocalDateTime
import java.time.LocalDateTime.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class RemovedEvent(@Id var id: String = "",
                        var owner: String = "")

@Entity
data class TimeAdjustment(@Id @GeneratedValue var id: Long = -1,
                          var owner: String = "",
                          var summary: String = "",
                          var startOffset: Long = 0,
                          var durationOffset: Long = 0)

class EventDisplay(var id: String = "",
                   var start: LocalDateTime = now(),
                   var end: LocalDateTime = now(),
                   var name: String = "",
                   var removed: Boolean = false)
