package pl.edu.uj.ii.mmatuszewski.services.grades.controller

import pl.edu.uj.ii.mmatuszewski.services.grades.model.Course

interface CourseProvider {
    fun provide(owner: String): List<Course>
}
