package pl.edu.uj.ii.mmatuszewski.schedule

import pl.edu.uj.ii.mmatuszewski.schedule.ClassType.*
import java.time.DayOfWeek
import java.time.DayOfWeek.*
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

enum class ClassType { LECTURE, CLASS, PROJECT }

@Entity
data class Subject(@Id var id: Int = -1,
                   var name: String = "",
                   var type: ClassType = LECTURE,
                   @OneToMany var occurences: List<Event> = mutableListOf())

@Entity
data class Event(@Id var id: Int = -1,
                 var dayOfWeek: DayOfWeek = MONDAY,
                 var start: LocalTime = LocalTime.now(),
                 var end: LocalTime = LocalTime.now(),
                 var location: String = "",
                 var lecturer: String = "",
                 var selected: Boolean = false) {

    fun display() = "${dayOfWeek.toString().substring(0..2)} $start-$end ${lecturer.split(" ").last()}@$location"
}

data class SubjectView(var id: Int = -1,
                       var name: String = "",
                       var type: ClassType = LECTURE,
                       var occurences: List<EventView> = mutableListOf())

data class EventView(var id: Int = -1,
                     var title: String = "",
                     var start: LocalDateTime = LocalDateTime.now(),
                     var end: LocalDateTime = LocalDateTime.now(),
                     var selected: Boolean = false)

data class Payload(var subjects: List<Subject> = mutableListOf())

data class RequestPayload(var subjects: Array<Occurence> = arrayOf())

data class Occurence(var occurences: Int = -1)
