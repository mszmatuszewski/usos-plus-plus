package pl.edu.uj.ii.mmatuszewski.grades.services

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.grades.Course
import pl.edu.uj.ii.mmatuszewski.grades.controllers.CourseProvider
import java.util.*

@Service
class CourseProviderService(val gradesProviderService: GradesProviderService,
                            val ectsCreditsProviderService: EctsCreditsProviderService) : CourseProvider {

    val courses by lazy { generate() }

    override fun provide(): List<Course> {
        return courses
    }

    fun generate(): List<Course> {
        return (1..10)
                .map(::createName)
                .map { Course(it) }
                .onEach(gradesProviderService::provide)
                .onEach(ectsCreditsProviderService::provide)
    }

    private fun createName(ignored: Int): String {
        return UUID.randomUUID().toString()
    }
}
