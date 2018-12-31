package pl.edu.uj.ii.mmatuszewski.calendar.controller

import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import pl.edu.uj.ii.mmatuszewski.calendar.service.CalendarService
import java.security.Principal
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/calendar")
class CalendarController(val calendarService: CalendarService) {

    @GetMapping("/retrieve/{owner}")
    @ResponseBody
    fun retrieve(@PathVariable owner: String): String = calendarService.retrieveAndFilter(owner)

    @GetMapping("/display")
    fun display(principal: Principal, servletRequest: HttpServletRequest): ModelAndView {
        val model = mutableMapOf<String, Any?>()
        val username = principal.name
        model["usosUrl"] = calendarService.getUrl(username)
        model["events"] = calendarService.retrieveAllAsDisplay(username)
        model["link"] = servletRequest.requestURL.replace("/display".toRegex(), "/retrieve/") + username
        return ModelAndView("calendar", model)
    }

    @PostMapping("/update")
    @Transactional
    fun update(principal: Principal, events: Array<String>?): String {
        calendarService.update(principal.name, events?.toList() ?: emptyList())
        return "redirect:/calendar/display"
    }

    @PostMapping("/seturl")
    fun setUrl(principal: Principal, url: String): String {
        calendarService.setUrl(principal.name, url)
        return "redirect:/calendar/display"
    }
}
