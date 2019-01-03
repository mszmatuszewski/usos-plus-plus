package pl.edu.uj.ii.mmatuszewski.services.calendar.controller

import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MimeType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pl.edu.uj.ii.mmatuszewski.services.calendar.service.CalendarService
import java.security.Principal
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/calendar")
class CalendarController(private val calendarService: CalendarService) {

    @GetMapping("/retrieve/{id}")
    fun retrieve(@PathVariable id: String): ResponseEntity<String> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.asMediaType(MimeType.valueOf("text/calendar; charset=utf-8"))
        headers.contentDisposition = ContentDisposition.parse("attachment; filename=calendar.ics")

        return ResponseEntity.ok().headers(headers).body(calendarService.retrieveAndFilter(id))
    }

    @GetMapping("/display")
    fun display(principal: Principal, servletRequest: HttpServletRequest): ModelAndView {
        val model = mutableMapOf<String, Any?>()
        val username = principal.name
        model["events"] = calendarService.retrieveAllAsDisplay(username)
        model["link"] = servletRequest.requestURL
                .replace("/display".toRegex(), "/retrieve/")
                .replace("http://", "webcal://")
                .replace("https://", "webcal://") +
                calendarService.getIdFromUsername(username)
        return ModelAndView("calendar", model)
    }

    @PostMapping("/update")
    @Transactional
    fun update(principal: Principal, events: Array<String>?): String {
        calendarService.update(principal.name, events?.toList() ?: emptyList())
        return "redirect:/calendar/display"
    }
}
