package pl.edu.uj.ii.mmatuszewski.grades.controllers

import pl.edu.uj.ii.mmatuszewski.grades.Course

interface CourseProvider {
    fun provide(): List<Course>
}
