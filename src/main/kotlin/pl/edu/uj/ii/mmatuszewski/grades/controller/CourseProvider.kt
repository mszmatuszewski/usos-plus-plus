package pl.edu.uj.ii.mmatuszewski.grades.controller

import pl.edu.uj.ii.mmatuszewski.grades.model.Course

interface CourseProvider {
    fun provide(): List<Course>
}
