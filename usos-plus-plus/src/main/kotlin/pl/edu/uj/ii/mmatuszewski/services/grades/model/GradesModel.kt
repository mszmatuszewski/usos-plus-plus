package pl.edu.uj.ii.mmatuszewski.services.grades.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.intellij.lang.annotations.Language

data class Courses(val courses: List<Course>)

data class Course(val name: String,
                  val grade: String,
                  var ects: Float = 0f,
                  val termId: String,
                  val courseId: String,
                  var countsIntoAverage: Boolean = true)

data class CoursesUserRS(@JsonProperty("course_editions") val courseEditions: Map<String, List<CourseLocator>>? = emptyMap())

data class CourseLocator(@JsonProperty("term_id") val termId: String? = "",
                         @JsonProperty("course_id") val courseId: String? = "")

data class CourseEdition(@JsonProperty("course_id") val courseId: String? = "",
                         @JsonProperty("course_name") val courseName: Map<String, String>? = emptyMap(),
                         @JsonProperty("term_id") val termId: String? = "",
                         @JsonProperty("grades") val grades: GradeWrapper? = null)

data class GradeWrapper(@JsonProperty("course_units_grades") val grades: Map<String, Map<String, Grade>>? = null)

data class Grade(@JsonProperty("value_symbol") val valueSymbol: String? = "",
                 @JsonProperty("exam_id") val examId: String? = "")

data class CourseDetails(@JsonProperty("id") val id: String,
                         @JsonProperty("ects_credits_simplified") val ects: Float? = 0f)

data class ExamReport(@JsonProperty("id") val id: String? = "",
                      @JsonProperty("description") val description: Map<String, String>? = emptyMap(),
                      @JsonProperty("counts_into_average") val countsIntoAverage: String? = "T")

class CourseComparator : Comparator<Course> {
    override fun compare(o1: Course?, o2: Course?): Int {
        if (o1 == null || o2 == null) return -1
        if (o1.termId != o2.termId) {
            val termComparison = compare(o1.termId, o2.termId)
            if (termComparison != 0) return termComparison
        }
        return if (o1.courseId < o2.courseId) -1 else 1
    }

    @Language("RegExp")
    private val termRegex = """(\d\d)/(\d\d)([LZ]?)""".toRegex()

    private fun compare(term1: String, term2: String): Int {
        val term1groups = termRegex.matchEntire(term1) ?: throw IllegalArgumentException("Invalid term")
        val term2groups = termRegex.matchEntire(term2) ?: throw IllegalArgumentException("Invalid term")

        val (year1, _, sem1) = term1groups.destructured
        val (year2, _, sem2) = term2groups.destructured

        if (year1 == year2) {
            if (sem1 == "") return -1
            if (sem2 == "") return 1
            return if (sem1 != sem2) {
                if (sem1 < sem2) -1 else 1
            } else 0
        }
        return if (year1.toInt() < year2.toInt()) 1 else -1
    }
}
