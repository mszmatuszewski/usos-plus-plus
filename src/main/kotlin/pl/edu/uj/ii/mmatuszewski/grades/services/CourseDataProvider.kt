package pl.edu.uj.ii.mmatuszewski.grades.services

import pl.edu.uj.ii.mmatuszewski.grades.Course

interface CourseDataProvider {
    fun provide(course: Course)
}
