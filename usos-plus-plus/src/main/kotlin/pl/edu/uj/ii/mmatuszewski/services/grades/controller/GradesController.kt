package pl.edu.uj.ii.mmatuszewski.services.grades.controller

import org.springframework.beans.propertyeditors.StringArrayPropertyEditor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView
import pl.edu.uj.ii.mmatuszewski.services.grades.model.Courses
import pl.edu.uj.ii.mmatuszewski.services.grades.service.GradeAverageService
import java.security.Principal

@Controller
@SessionAttributes("courses")
@RequestMapping("/grades")
class GradesController(private val courseProvider: CourseProvider,
                       private val gradeAverageService: GradeAverageService) {

    @ModelAttribute("courses")
    fun courses() = Courses(mutableListOf())

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(Array<String>::class.java, StringArrayPropertyEditor("@@@"))
    }

    @GetMapping("/list")
    fun grades(principal: Principal,
               @ModelAttribute courses: Courses,
               modelAndView: ModelAndView): ModelAndView {

        modelAndView.model["courses"] = when {
            courses.courses.isEmpty() -> Courses(courseProvider.provide(principal.name))
            else                      -> courses
        }

        modelAndView.viewName = "grades"

        return modelAndView
    }

    @PostMapping("/calculate")
    fun calculate(@RequestParam("course") selection: Array<String>?,
                  @ModelAttribute courses: Courses,
                  modelAndView: ModelAndView): ModelAndView {

        val selectedCourses = selection ?: emptyArray()
        val averagedCourses = courses.courses.filter { it.name in selectedCourses }

        modelAndView.model["average"] = gradeAverageService.calculate(averagedCourses).roundToTwoDecimalPlacesAsString()
        modelAndView.viewName = "grades"
        return modelAndView
    }

    private fun Double.roundToTwoDecimalPlacesAsString(): String = String.format("%.2f", this)
}
