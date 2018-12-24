package pl.edu.uj.ii.mmatuszewski.grades.services

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.grades.Course
import pl.edu.uj.ii.mmatuszewski.grades.controllers.CourseProvider

@Service
class CourseProviderService(val gradesProviderService: GradesProviderService,
                            val ectsCreditsProviderService: EctsCreditsProviderService) : CourseProvider {

    val courses by lazy { generate() }

    private var nextName: Int = 0

    override fun provide(): List<Course> {
        return courses
    }

    fun generate(): List<Course> {
        return (1..6)
                .map(::createName)
                .map { Course(it) }
                .onEach(gradesProviderService::provide)
                .onEach(ectsCreditsProviderService::provide)
    }

    private fun createName(ignored: Int): String {
        return listOf("Effective Python", "Assembly programming", "Advanced design and architectural patterns",
                "Abstract programming", "Individual project", "IT project management")[(nextName++) % 6]
    }
}
