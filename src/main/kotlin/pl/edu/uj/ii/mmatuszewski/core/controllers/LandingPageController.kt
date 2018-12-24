package pl.edu.uj.ii.mmatuszewski.core.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class LandingPageController {

    @GetMapping("/")
    fun homePage(model: ModelAndView): String {
        return "index"
    }
}
