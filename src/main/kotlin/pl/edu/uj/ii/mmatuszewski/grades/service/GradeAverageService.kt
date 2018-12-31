package pl.edu.uj.ii.mmatuszewski.grades.service

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.grades.model.Course

@Service
class GradeAverageService {

    fun calculate(courses: List<Course>): Double {
        return courses.map { it.ects * it.grade }.sum() / courses.map { it.ects }.sum().toDouble()
    }
}
