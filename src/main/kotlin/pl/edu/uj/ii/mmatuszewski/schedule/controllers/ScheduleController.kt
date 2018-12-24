package pl.edu.uj.ii.mmatuszewski.schedule.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pl.edu.uj.ii.mmatuszewski.schedule.Payload
import pl.edu.uj.ii.mmatuszewski.schedule.RequestPayload
import pl.edu.uj.ii.mmatuszewski.schedule.services.ClassesService

@Controller
@RequestMapping("/schedule")
class ScheduleController(val classesService: ClassesService) {

    @GetMapping("/retrieve")
    fun schedule(@ModelAttribute payload: RequestPayload?): ModelAndView {
        val model = mutableMapOf<String, Any?>()

        val classes = classesService.provide()

        val selectedClasses = when {
            payload?.subjects.isNullOrEmpty() -> listOf<Int>()
            else                              -> payload!!.subjects.map { it.occurences }
        }


        model["payload"] = Payload(classes)
        model["calendarClasses"] = classesService.mapToView(classes).onEach {
            it.selected = it.id in selectedClasses
        }.filter { it.selected }

        return ModelAndView("schedule", model)
    }
}
