package pl.edu.uj.ii.mmatuszewski.calendar.model

import java.time.LocalDateTime
import java.time.LocalDateTime.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class RemovedEvent(@Id var id: String = "",
                        var owner: String = "")

data class EventDisplay(var id: String = "",
                        var start: LocalDateTime = now(),
                        var end: LocalDateTime = now(),
                        var name: String = "",
                        var removed: Boolean = false)
