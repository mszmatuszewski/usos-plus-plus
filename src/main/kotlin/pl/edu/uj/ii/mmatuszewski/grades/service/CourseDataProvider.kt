package pl.edu.uj.ii.mmatuszewski.grades.service

import pl.edu.uj.ii.mmatuszewski.grades.model.Course

interface CourseDataProvider {
    fun provide(course: Course)
}
