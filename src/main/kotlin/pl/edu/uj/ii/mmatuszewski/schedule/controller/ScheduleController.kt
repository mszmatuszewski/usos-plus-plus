package pl.edu.uj.ii.mmatuszewski.schedule.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pl.edu.uj.ii.mmatuszewski.schedule.model.ClassType
import pl.edu.uj.ii.mmatuszewski.schedule.model.OccurenceSelections
import pl.edu.uj.ii.mmatuszewski.schedule.model.SubjectWithEvent
import pl.edu.uj.ii.mmatuszewski.schedule.service.ScheduleService
import java.security.Principal
import java.time.DayOfWeek

@Controller
@RequestMapping("/schedule")
class ScheduleController(val scheduleService: ScheduleService) {

    @GetMapping
    fun schedule(principal: Principal): ModelAndView {
        val model = mutableMapOf<String, Any?>()

        val (subjectViews, calendarClasses) = scheduleService.provideDisplay(principal.name)
        model["subjectViews"] = subjectViews
        model["calendarClasses"] = calendarClasses

        return ModelAndView("schedule", model)
    }

    @PostMapping
    fun schedule(@ModelAttribute payload: OccurenceSelections, principal: Principal): ModelAndView {
        scheduleService.updateSelection(principal.name, payload.subjects.toList())
        return schedule(principal)
    }

    @GetMapping("/edit")
    fun editList(principal: Principal): ModelAndView {
        val model = mutableMapOf<String, Any?>()

        model["classes"] = scheduleService.retrieveSubjectsWithEvents(principal.name)

        return ModelAndView("schedule_edit", model)
    }

    @GetMapping("/edit/details")
    fun edit(principal: Principal): ModelAndView {
        val model = mutableMapOf<String, Any?>()

        model["item"] = SubjectWithEvent()
        model["classTypes"] = ClassType.values()
        model["daysOfWeek"] = DayOfWeek.values()

        return ModelAndView("schedule_edit_specific", model)
    }

    @GetMapping("/edit/details/{id}")
    fun edit(principal: Principal, @PathVariable("id") eventId: Long): ModelAndView {
        val model = mutableMapOf<String, Any?>()

        model["item"] = scheduleService.findSubjectWithEventById(principal.name, eventId)
        model["classTypes"] = ClassType.values()
        model["daysOfWeek"] = DayOfWeek.values()

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
}
