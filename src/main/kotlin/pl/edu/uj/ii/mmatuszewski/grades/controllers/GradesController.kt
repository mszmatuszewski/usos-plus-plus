package pl.edu.uj.ii.mmatuszewski.grades.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pl.edu.uj.ii.mmatuszewski.grades.services.GradeAverageService
import pl.edu.uj.ii.mmatuszewski.roundToTwoDecimalPlaces

@Controller
@RequestMapping("/grades")
class GradesController(val courseProvider: CourseProvider,
                       val gradeAverageService: GradeAverageService) {

    @GetMapping("/list")
    fun grades(): ModelAndView {
        val model = mutableMapOf<String, Any?>()

        model["courses"] = courseProvider.provide()

        return ModelAndView("grades", model)
    }

    @GetMapping("/calculate")
    fun calculate(@RequestParam("course") courses: Array<String>?): ModelAndView {
        val model = mutableMapOf<String, Any?>()

        val selectedCourses = courses ?: arrayOf()
        val averagedCourses = courseProvider.provide().filter { it.name in selectedCourses }
        model["courses"] = courseProvider.provide()
        model["average"] = gradeAverageService.calculate(averagedCourses).roundToTwoDecimalPlaces()

        return ModelAndView("grades", model)
    }
}
