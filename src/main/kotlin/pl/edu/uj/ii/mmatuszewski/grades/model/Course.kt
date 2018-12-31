package pl.edu.uj.ii.mmatuszewski.grades.model

data class Course(val name: String,
                  var grade: Double = 0.0,
                  var ects: Int = 0)
