package pl.edu.uj.ii.mmatuszewski.schedule.model

import pl.edu.uj.ii.mmatuszewski.schedule.model.ClassType.*
import java.time.DayOfWeek
import java.time.DayOfWeek.*
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import java.time.LocalTime
import javax.persistence.CascadeType.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

enum class ClassType { LECTURE, CLASS, PROJECT }

/* Database entities. */
@Entity
data class Subject(@Id @GeneratedValue var id: Long? = null,
                   var owner: String = "",
                   var name: String = "",
                   var type: ClassType = LECTURE,
                   @OneToMany(cascade = [ALL]) val occurences: MutableList<Event> = mutableListOf())

@Entity
data class Event(@Id @GeneratedValue var id: Long? = null,
                 var dayOfWeek: DayOfWeek = MONDAY,
                 @Column(name = "startTime") var start: LocalTime = LocalTime.now(),
                 @Column(name = "endTime") var end: LocalTime = LocalTime.now(),
                 var location: String = "",
                 var lecturer: String = "",
                 var selected: Boolean = false) {

    val displayTitle
        get() = "${dayOfWeek.toString().substring(0..2)} $start-$end ${lecturer.split(" ").last()}@$location"
}

/* DTO used for editing the above. */
data class SubjectWithEvent(
        var id: Long? = null,
        var name: String = "",
        var type: ClassType = LECTURE,
        var dayOfWeek: DayOfWeek = MONDAY,
        var start: LocalTime = LocalTime.now(),
        var end: LocalTime = LocalTime.now(),
        var location: String = "",
        var lecturer: String = ""
)

data class SubjectWithEventCollection(var data: List<SubjectWithEvent>)

/* DTO used for displaying choices. */
data class SubjectViews(var subjects: List<SubjectView> = mutableListOf())

data class SubjectView(var id: Long = -1,
                       var name: String = "",
                       var type: ClassType = LECTURE,
                       var occurences: List<EventView> = mutableListOf())

data class EventView(var id: Long = -1,
                     var title: String = "",
                     var start: LocalDateTime = now(),
                     var end: LocalDateTime = now(),
                     var selected: Boolean = false)

/* DTO used for rendering by the JavaScript FullCalendar. */
data class RenderedEvent(var id: Long = -1,
                         var title: String = "",
                         var start: LocalDateTime = now(),
                         var end: LocalDateTime = now())

/* DTO sent by the frontend on lecture group change. Do not rename. */
data class OccurenceSelections(var subjects: Array<Long> = arrayOf())
