package pl.edu.uj.ii.mmatuszewski.services.grades.service

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.services.grades.model.Course

@Service
class GradeAverageService {

    fun calculate(courses: List<Course>): Double {
        return courses.map { it.ects * it.grade.parseGrade() }.sum() /
               courses.map { it.ects }.sum()
    }

    private fun String.parseGrade(): Double = replace(",", ".").toDouble()
}
