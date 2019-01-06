package pl.edu.uj.ii.mmatuszewski.services.schedule.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView
import pl.edu.uj.ii.mmatuszewski.services.schedule.model.ClassType
import pl.edu.uj.ii.mmatuszewski.services.schedule.model.OccurenceSelections
import pl.edu.uj.ii.mmatuszewski.services.schedule.model.SubjectWithEvent
import pl.edu.uj.ii.mmatuszewski.services.schedule.service.ScheduleService
import java.security.Principal
import java.time.DayOfWeek

@Controller
@RequestMapping("/schedule")
@SessionAttributes("classTypes", "daysOfWeek")
class ScheduleController(private val scheduleService: ScheduleService) {

    @ModelAttribute("classTypes")
    fun classTypes() = ClassType.values()

    @ModelAttribute("daysOfWeek")
    fun daysOfWeek() = DayOfWeek.values().filter { it != DayOfWeek.SATURDAY && it != DayOfWeek.SUNDAY }

    @GetMapping
    fun schedule(principal: Principal): ModelAndView {
        val (subjectViews, calendarClasses) = scheduleService.provideDisplay(principal.name)
        val model = mutableMapOf<String, Any?>(
                "subjectViews" to subjectViews,
                "calendarClasses" to calendarClasses)
        return ModelAndView("schedule", model)
    }

    @PostMapping
    fun schedule(@ModelAttribute payload: OccurenceSelections, principal: Principal): ModelAndView {
        scheduleService.updateSelection(principal.name, payload.subjects.toList())
        return schedule(principal)
    }

    @GetMapping("/edit")
    fun editList(principal: Principal): ModelAndView {
        val model = mutableMapOf<String, Any?>(
                "classes" to scheduleService.retrieveSubjectsWithEvents(principal.name))
        return ModelAndView("schedule_edit", model)
    }

    @GetMapping("/edit/details")
    fun edit(principal: Principal): ModelAndView {
        val (subjectNames, locations, lecturers) = scheduleService.provideDatalists(principal.name)
        val model = mutableMapOf(
                "item" to SubjectWithEvent(),
                "subjectNames" to subjectNames,
                "locations" to locations,
                "lecturers" to lecturers)
        return ModelAndView("schedule_edit_specific", model)
    }

    @GetMapping("/edit/details/{id}")
    fun edit(principal: Principal, @PathVariable("id") eventId: Long): ModelAndView {
        val (subjectNames, locations, lecturers) = scheduleService.provideDatalists(principal.name)
        val model = mutableMapOf(
                "item" to scheduleService.findSubjectWithEventById(principal.name, eventId),
                "subjectNames" to subjectNames,
                "locations" to locations,
                "lecturers" to lecturers)
        return ModelAndView("schedule_edit_specific", model)
    }

    @PostMapping("/edit/details")
    fun save(principal: Principal, subjectWithEvent: SubjectWithEvent): String {
        scheduleService.addOrUpdateSubjectWithEvent(principal.name, subjectWithEvent)
        return "redirect:/schedule/edit"
    }

    @GetMapping("/edit/delete/{id}")
    fun delete(principal: Principal, @PathVariable("id") eventId: Long): String {
        scheduleService.deleteSubjectWithEvent(principal.name, eventId)
        return "redirect:/schedule/edit"
    }

    @GetMapping("/populate")
    fun populate(principal: Principal): String {
        scheduleService.populateDummy(principal.name)
        return "redirect:/schedule"
    }
}
